package org.longmetal;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class DriveTrain {
    public DifferentialDrive driveTrain;

    public CANSparkMax mRearLeft,
        mFrontLeft,
        mRearRight,
        mFrontRight;
    public SpeedControllerGroup leftMotors,
        rightMotors;

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

    public void curve(double speedRaw, double speedThrottleRaw, double curvatureRaw, double curvatureThrottleRaw) {
        double modifierX = (Constants.kSPEED_MODIFIER * speedThrottleRaw - Constants.kTHROTTLE_SHIFT) / 2; // Create a speed modifier
		double modifierZ = (curvatureThrottleRaw - 1) * Constants.kCURVE_MODIFIER;   // Create a curvature modifier

		double driveX = speedRaw * modifierX * Constants.kMAX_SPEED_MULT; // Calculate the speed
		double driveZ = curvatureRaw * modifierZ;               // Calculate the curvature

        driveTrain.curvatureDrive(driveX, driveZ, true);    // Drive
    }
}