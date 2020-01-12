package org.longmetal;

import edu.wpi.first.wpilibj.Spark;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.longmetal.exception.*;
import org.longmetal.util.Math;

public class Shooter {
    private Spark shooterL, shooterR, singulator;
    private TalonSRX angle;
    private static boolean enabled = true;
    private boolean initialized = false;
    private boolean shooting = false;
    private double modifierX = 0;
    private double modifierY = 0;

    public Shooter() {
        this(true);
    }

    public Shooter(boolean setEnabled) {
        enabled = setEnabled;
        if (enabled) {
            init();
        } else {
            System.out.println("[WARN]\tShooter wasn't enabled on startup. You must call init() on it later to use it.");
        }
    }

    public void init() {
        angle = new TalonSRX(Constants.kP_ANGLE);
        shooterL = new Spark(Constants.kP_LSHOOTER);
        shooterR = new Spark(Constants.kP_RSHOOTER);
        singulator = new Spark(Constants.kP_SINGULATOR);
        initialized = true;
    }

    public void setSingFreq(double singNum) throws SubsystemException {
        SubsystemManager.check(enabled, initialized);
        singulator.set(Math.limit(singNum, Constants.kSINGULATOR_MIN, Constants.kSINGULATOR_MAX).doubleValue());
    }

    public void setShootSpeed(double shootSpeed) throws SubsystemException {
        SubsystemManager.check(enabled, initialized);
        shootSpeed = Math.limit(shootSpeed, Constants.kSHOOTER_MIN, Constants.kSHOOTER_MAX).doubleValue();
        shooterL.set(shootSpeed);
        shooterR.set(shootSpeed);
        shooting = shootSpeed > Constants.kSHOOTER_MIN + 0.1;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void modifier(double modifierX, double modifierY) {
        this.modifierX = modifierX;
        this.modifierY = modifierY;
    }

    public void run(double speed) throws SubsystemException {
        run(speed, true);
    }

    public void run(double speed, boolean runSingulator) throws SubsystemException {
        SubsystemManager.check(enabled, initialized);
        if (runSingulator) {
            singulator.set(speed);
        }
        speed *= Constants.kSHOOTER_SPEED_MODIFIER;
        double xModifier = modifierX * Constants.kSHOOTER_X_MODIFIER;
        double yModifier = modifierY * Constants.kSHOOTER_Y_MODIFIER;
        shooterL.set((double)Math.limit(speed - xModifier + yModifier, Constants.kSHOOTER_MIN, Constants.kSHOOTER_MAX));
        shooterR.set((double)Math.limit(speed + xModifier + yModifier, Constants.kSHOOTER_MIN, Constants.kSHOOTER_MAX));
    }

    public void idle() throws SubsystemException {
        SubsystemManager.check(enabled, initialized);
        singulator.set(0);
        run(Constants.kSHOOTER_MIN, false);
    }

    public void angleSpeed(double speed) {
        angle.set(ControlMode.PercentOutput, speed);
    }

    public void setEnabled(boolean newEnabled) {
        enabled = newEnabled;
        if (!enabled) {
            if (!initialized) {
                init();
            }
            angle.set(ControlMode.PercentOutput, 0);
            shooterL.set(0);
            shooterR.set(0);
            singulator.set(0);
        }
    }

    public static void staticSetEnabled(boolean newEnabled) {
        enabled = newEnabled;
    }

    public static boolean getEnabled() {
        return enabled;
    }
}