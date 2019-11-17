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
   
}