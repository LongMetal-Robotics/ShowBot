package org.longmetal;

import edu.wpi.first.wpilibj.Spark;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Shooter
{
    private Spark shooterL, shooterR, singulator;
    private TalonSRX angle;
    public Shooter()
    {
        angle = new TalonSRX(5);
        shooterL = new Spark(1);
        shooterR = new Spark(2);
        singulator = new Spark(3);
    }
    public void setSingFreq(double singNum)
    {
        singulator.set(singNum);
    }
    public void setShootSpeed(double shootSpeed)
    {
        shooterL.set(shootSpeed);
        shooterR.set(shootSpeed);
    }
    
   
}
//work on later
public void teleopPeriodic()
    {
        if (buttonLBPressed && !forwardLimitSwitch.get()) // If the forward limit switch is pressed, we want to keep the values between -1 and 0
            shooter.setAngle(ControlMode.PercentOutput, 0.4);
        else if(buttonLTPressed && !reverseLimitSwitch.get()) // If the reversed limit switch is pressed, we want to keep the values between 0 and 1
            shooter.setAngle(ControlMode.PercentOutput, -0.4);
        shooter.setAngle(ControlMode.PercentOutput, 0.0);
    }
