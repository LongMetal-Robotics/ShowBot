package org.longmetal;

import edu.wpi.first.wpilibj.Spark;

public class Collector{
    private Spark collectorMotor;
    public Collector(){
        collectorMotor = new Spark(4);
    }
    public void setMotor(double speed){
        collectorMotor.set(speed);
    }
}