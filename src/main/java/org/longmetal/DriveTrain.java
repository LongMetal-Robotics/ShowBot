package org.longmetal;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.longmetal.util.Math;

public class DriveTrain {
    public DifferentialDrive driveTrain;

    public CANSparkMax mRearLeft,
        mFrontLeft,
        mRearRight,
        mFrontRight;
    public SpeedControllerGroup leftMotors,
        rightMotors;

    private boolean reverseDrive = false;
    private double MAX_SPEED_MULT = 0.5;

    public DriveTrain() {
        mRearLeft = new CANSparkMax(Constants.kREAR_LEFT, MotorType.kBrushless);
        mRearLeft.setIdleMode(IdleMode.kCoast);
        mFrontLeft = new CANSparkMax(Constants.kFRONT_LEFT, MotorType.kBrushless);
        mFrontLeft.setIdleMode(IdleMode.kCoast);
        leftMotors = new SpeedControllerGroup(mRearLeft, mFrontLeft);

        mRearRight = new CANSparkMax(Constants.kREAR_RIGHT, MotorType.kBrushless);
        mRearRight.setIdleMode(IdleMode.kCoast);
        mFrontRight = new CANSparkMax(Constants.kFRONT_RIGHT, MotorType.kBrushless);
        mFrontRight.setIdleMode(IdleMode.kCoast);
        rightMotors = new SpeedControllerGroup(mRearRight, mFrontRight);

        driveTrain = new DifferentialDrive(leftMotors, rightMotors);
    }

    public void setReverseDrive(boolean newReverseDrive) {
        reverseDrive = newReverseDrive;
    }

    public boolean getReverseDrive() {
        return reverseDrive;
    }

    public void setMaxSpeed(double maxSpeed) {
        MAX_SPEED_MULT = maxSpeed;
    }

    public double getMaxSpeed() {
        return MAX_SPEED_MULT;
    }

    public void curve(double speedRaw, double speedThrottleRaw, double curvatureRaw, double curvatureThrottleRaw) {
        double modifierX = (Constants.kSPEED_MODIFIER * speedThrottleRaw - Constants.kTHROTTLE_SHIFT) / 2; // Create a speed modifier
		double modifierZ = (curvatureThrottleRaw - 1) * Constants.kCURVE_MODIFIER;   // Create a curvature modifier

		double driveX = (double)Math.limit(speedRaw * modifierX * MAX_SPEED_MULT, -1.0, 1.0); // Calculate the speed
		double driveZ = curvatureRaw * modifierZ;               // Calculate the curvature

        if (reverseDrive) {
            // Reverse drive values
            driveX *= -1;
        }

        driveTrain.curvatureDrive(driveX, driveZ, true);    // Drive
    }
}