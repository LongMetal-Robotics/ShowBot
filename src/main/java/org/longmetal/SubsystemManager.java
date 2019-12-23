package org.longmetal;

import org.longmetal.exception.SubsystemDisabledException;
import org.longmetal.exception.SubsystemException;
import org.longmetal.exception.SubsystemUninitializedException;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class SubsystemManager {

    public SubsystemManager() {
        Preferences preferences = Preferences.getInstance();
        boolean shooterEnableValue = false;
        boolean collectorEnableValue = false;

        if (preferences.containsKey(Constants.kSHOOTER_KEY) /* Includes shooter data */
          && preferences.getBoolean(Constants.kSHOOTER_KEY, false) /* Shooter disabled */) {
            shooterEnableValue = true;
        }

        SendableChooser<Runnable> shooterEnable = new SendableChooser<>();
        if (shooterEnableValue) {    // (hopefully) set the order of the options in the menu so enabled is always first but the
                                    // initially selected option indicates whether the subsystem is actually enabled or not
                                    // based on previously-saved preferences
            shooterEnable.setDefaultOption("Enabled", new Runnable() {
            
                @Override
                public void run() {
                    SubsystemManager.setSubsystem(Subsystem.SHOOTER, true);
                }
            });
            shooterEnable.addOption("Disabled", new Runnable(){
            
                @Override
                public void run() {
                    SubsystemManager.setSubsystem(Subsystem.SHOOTER, false);
                }
            });
        } else {
            shooterEnable.addOption("Enabled", new Runnable(){
            
                @Override
                public void run() {
                    SubsystemManager.setSubsystem(Subsystem.SHOOTER, true);
                }
            });
            shooterEnable.setDefaultOption("Disabled", new Runnable() {
            
                @Override
                public void run() {
                    SubsystemManager.setSubsystem(Subsystem.SHOOTER, false);
                }
            });
        }

        setSubsystem(Subsystem.SHOOTER, shooterEnableValue);


        if (preferences.containsKey(Constants.kCOLLECTOR_KEY) /* Includes collector data */
          && preferences.getBoolean(Constants.kCOLLECTOR_KEY, false) /* Shooter disabled */) {
            collectorEnableValue = true;
        }

        SendableChooser<Runnable> collectorEnable = new SendableChooser<>();
        if (collectorEnableValue) {
            collectorEnable.setDefaultOption("Enabled", new Runnable() {
            
                @Override
                public void run() {
                    SubsystemManager.setSubsystem(Subsystem.COLLECTOR, true);
                }
            });
            collectorEnable.addOption("Disabled", new Runnable(){
            
                @Override
                public void run() {
                    SubsystemManager.setSubsystem(Subsystem.COLLECTOR, false);
                }
            });
        } else {
            collectorEnable.addOption("Enabled", new Runnable(){
            
                @Override
                public void run() {
                    SubsystemManager.setSubsystem(Subsystem.COLLECTOR, true);
                }
            });
            collectorEnable.setDefaultOption("Disabled", new Runnable() {
            
                @Override
                public void run() {
                    SubsystemManager.setSubsystem(Subsystem.COLLECTOR, false);
                }
            });
        }
        
        setSubsystem(Subsystem.COLLECTOR, collectorEnableValue);
    }

    public static void setSubsystem(Subsystem subsystem, boolean enabled) {
        Preferences preferences = Preferences.getInstance();
        switch (subsystem) {
            case SHOOTER:
                Shooter.staticSetEnabled(enabled);
                preferences.putBoolean(Constants.kSHOOTER_KEY, enabled);
                break;

            case COLLECTOR:
                Collector.staticSetEnabled(enabled);
                preferences.putBoolean(Constants.kCOLLECTOR_KEY, enabled);
                break;
        }
    }

    public static void check(boolean enabled, boolean initialized) throws SubsystemException {
        if (!initialized) {
            throw new SubsystemUninitializedException();
        }
        if (!enabled) {
            throw new SubsystemDisabledException();
        }
    }
    
    public enum Subsystem {
        SHOOTER,
        COLLECTOR
    }
}