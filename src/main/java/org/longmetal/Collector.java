package org.longmetal;

import edu.wpi.first.wpilibj.Spark;

import org.longmetal.exception.*;

public class Collector {
    private Spark collectorMotor;
    private static boolean enabled = true;
    private boolean initialized = false;

    public Collector() {
        this(true);
    }

    public Collector(boolean setEnabled) {
        enabled = setEnabled;
        if (enabled) {
            init();
        } else {
            System.out.println("[WARN]\tCollector wasn't enabled on startup. You must call init() on it later to use it.");
        }
    }

    public void init() {
        collectorMotor = new Spark(Constants.kP_COLLECTOR);
        initialized = true;
    }

    public void setMotor(double speed) throws SubsystemException {
        SubsystemManager.check(enabled, initialized);
        collectorMotor.set(speed);
    }

    public void setEnabled(boolean newEnabled) {
        enabled = newEnabled;
        if (!enabled) {
            if (!initialized) {
                init();
            }
            collectorMotor.set(0);
        }
    }

    public static void staticSetEnabled(boolean newEnabled) {
        enabled = newEnabled;
    }

    public static boolean getEnabled() {
        return enabled;
    }
}