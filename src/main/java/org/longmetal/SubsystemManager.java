package org.longmetal;

import org.longmetal.exception.SubsystemDisabledException;
import org.longmetal.exception.SubsystemException;
import org.longmetal.exception.SubsystemUninitializedException;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SubsystemManager {
    private SendableChooser<Runnable> shooterEnable;
    private SendableChooser<Runnable> collectorEnable;

    public SubsystemManager() {
        Preferences preferences = Preferences.getInstance();
        boolean shooterEnableValue = false;
        boolean collectorEnableValue = false;

        shooterEnableValue = preferences.getBoolean(Constants.kSHOOTER_KEY, false) /* Shooter enabled */;

        Runnable enableShooter = new Runnable() {
            
            @Override
            public void run() {
                SubsystemManager.setSubsystem(Subsystem.SHOOTER, true);
            }
        };
        Runnable disableShooter = new Runnable(){
            
            @Override
            public void run() {
                SubsystemManager.setSubsystem(Subsystem.SHOOTER, false);
            }
        };

        shooterEnable = new SendableChooser<>();
        if (shooterEnableValue) {   // (hopefully) set the order of the options in the menu so enabled is always first but the
                                    // initially selected option indicates whether the subsystem is actually enabled or not
                                    // based on previously-saved preferences
            shooterEnable.setDefaultOption(Constants.kENABLED, enableShooter);
            shooterEnable.addOption(Constants.kDISABLED, disableShooter);
        } else {
            shooterEnable.addOption(Constants.kENABLED, enableShooter);
            shooterEnable.setDefaultOption(Constants.kDISABLED, disableShooter);
        }
        SmartDashboard.putData(Constants.kSHOOTER_ENABLER_KEY, shooterEnable);

        setSubsystem(Subsystem.SHOOTER, shooterEnableValue);


        collectorEnableValue = preferences.getBoolean(Constants.kCOLLECTOR_KEY, false) /* Collector enabled */;

        Runnable enableCollector = new Runnable() {
            
            @Override
            public void run() {
                SubsystemManager.setSubsystem(Subsystem.COLLECTOR, true);
            }
        };

        Runnable disableCollector = new Runnable(){
            
            @Override
            public void run() {
                SubsystemManager.setSubsystem(Subsystem.COLLECTOR, false);
            }
        };

        collectorEnable = new SendableChooser<>();
        if (collectorEnableValue) {
            collectorEnable.setDefaultOption(Constants.kENABLED, enableCollector);
            collectorEnable.addOption(Constants.kDISABLED, disableCollector );
        } else {
            collectorEnable.addOption(Constants.kENABLED, enableCollector);
            collectorEnable.setDefaultOption(Constants.kDISABLED, disableCollector);
        }
        SmartDashboard.putData(Constants.kCOLLECTOR_ENABLER_KEY, collectorEnable);
        
        setSubsystem(Subsystem.COLLECTOR, collectorEnableValue);
    }

    public void checkSendables() {
        shooterEnable.getSelected().run();
        collectorEnable.getSelected().run();
    }

    public static void setSubsystem(Subsystem subsystem, boolean enabled) {
        Preferences preferences = Preferences.getInstance();
        switch (subsystem) {
            case SHOOTER:
                Shooter.staticSetEnabled(enabled);
                preferences.putBoolean(Constants.kSHOOTER_KEY, enabled);
                SmartDashboard.putBoolean(Constants.kSHOOTER_STATE_KEY, enabled);
                break;

            case COLLECTOR:
                Collector.staticSetEnabled(enabled);
                preferences.putBoolean(Constants.kCOLLECTOR_KEY, enabled);
                SmartDashboard.putBoolean(Constants.kCOLLECTOR_STATE_KEY, enabled);
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