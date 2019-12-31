package frc.robot;

import java.io.File;
import java.util.Scanner;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Preferences;

import org.longmetal.*;
import org.longmetal.Arduino.Status;
import org.longmetal.util.Listener;
import org.longmetal.exception.*;

public class Robot extends TimedRobot {
    private static final String DEPRECATION = "deprecation";

    Input input;
    DriveTrain driveTrain;
    Arduino status;
    Shooter shooter;
    Collector collector;
    Preferences prefs;
    SubsystemManager subsystemManager;

    SendableChooser<Boolean> chooserQuinnDrive;

    Listener QuinnDrive;
    Listener forwardDrive;

    @Override
    @SuppressWarnings(DEPRECATION)
    public void robotInit() {
        try {   // Automatic, Git-based Version Tracking
            File file = new File(Filesystem.getDeployDirectory(), "branch.txt");    // Open the file that contains the current branch
            Scanner fs = new Scanner(file);
            String branch = "unknown",
                commit = "unknown";

            if (fs.hasNextLine()) {
                branch = fs.nextLine(); // Read the current branch from file
            }

            file = new File(Filesystem.getDeployDirectory(), "commit.txt"); // Open the file that contains the current commit
            fs.close();
            fs = new Scanner(file);

            if (fs.hasNextLine()) {
                commit = fs.nextLine(); // Read the current commit from file
            }

            System.out.println("Commit " + commit + " or later (branch '" + branch + "')");
            SmartDashboard.putString("Commit", commit);
            SmartDashboard.putString("Branch", branch);
            fs.close();
        } catch (Exception e) {
            System.out.println("Could not determine commit or branch. (" + e.getLocalizedMessage() + ") Trace:");
            e.printStackTrace();
        }

        input = new Input();
        driveTrain = new DriveTrain();
        driveTrain.setReverseDrive(true);   // Default to shooting mode
        status = new Arduino();
        prefs = Preferences.getInstance();

        shooter = new Shooter();
        collector = new Collector();
        subsystemManager = new SubsystemManager();

        QuinnDrive = new Listener(/* onTrue */ new Runnable(){ public void run() { input.setQuinnDrive(true); } },
            /* onFalse */ new Runnable(){ public void run() { input.setQuinnDrive(false); } });

        chooserQuinnDrive = new SendableChooser<>();
        chooserQuinnDrive.addDefault("Disabled", false);
        chooserQuinnDrive.addObject("Enabled", true);
        SmartDashboard.putData("Quinn Drive Chooser", chooserQuinnDrive);

        forwardDrive = new Listener(new Runnable(){ // This looks like a really janky way to do it but it should work well
            public void run() { // Forward
                driveTrain.setReverseDrive(false);
                status.sendStatus(Status.FORWARD);
            }
        }, new Runnable(){
            public void run() { // Reverse
                driveTrain.setReverseDrive(true);
                status.sendStatus(Status.BACKWARD);
            }
        });
    }
    
    @Override
    public void robotPeriodic() {
        SmartDashboard.putData("Drive Train", driveTrain.driveTrain);
        QuinnDrive.update(chooserQuinnDrive.getSelected());
        SmartDashboard.putBoolean("Quinn Drive", input.isQuinnDrive());
        // Reverse Drive mode

        boolean forwardDriveVal = input.forwardStick.getRawButtonPressed(Constants.kFORWARD_BUTTON);
        boolean reverseDriveVal = input.forwardStick.getRawButton(Constants.kREVERSE_BUTTON);

        if (!(forwardDriveVal && reverseDriveVal) || (!forwardDriveVal && !reverseDriveVal)) {  // One or the other
            forwardDrive.update(forwardDriveVal);   // If this is false the other must be true so this works
        }

        SmartDashboard.putBoolean("Reverse Drive", driveTrain.getReverseDrive());
        subsystemManager.checkSendables();
    }

    @Override
    public void disabledInit() {
        status.sendStatus(Status.DISABLED);
    }

    private void sendStandardStatus() {
        Status mode = Status.ENABLED;
        if (driveTrain.getReverseDrive()) {
            mode = Status.BACKWARD;
        } else {
            mode = Status.FORWARD;
        }
        status.sendStatus(mode);
    }

    @Override
    public void teleopInit() {
        sendStandardStatus();
    }

    @Override
    public void teleopPeriodic() {

        driveTrain.curve(input.forwardStick.getY(),
            input.forwardStick.getThrottle(),
            input.turnStick.getTwist(),
            input.turnStick.getThrottle());

        double trigger = input.gamepad.getRawAxis(Constants.kA_TRIGGER);

        String currentSubsystem = "Subsystem";
        try {
            if (driveTrain.getReverseDrive()) { // Shooting mode
                System.out.println("Shooting mode");
                currentSubsystem = "Shooter";
                if (Shooter.getEnabled()) {
                    double modifierX = input.gamepad.getRawAxis(Constants.kA_LS_X);
                    double modifierY = input.gamepad.getRawAxis(Constants.kA_LS_Y) * Constants.kY_AXIS_MODIFIER;

                    shooter.modifier(modifierX, modifierY); // Set shooter modifiers
                    System.out.println("Shooter modifiers: (" + modifierX + ", " + modifierY + ")");
                    if (trigger > Constants.kINPUT_DEADBAND) {    // Right trigger has passed deadband
                        System.out.println("Trigger past deadband (value: " + trigger + ")");
                        status.sendStatus(Status.SHOOTING);
                        shooter.run(trigger);
                    } else {
                        System.out.println("Trigger in deadband");
                        sendStandardStatus();
                        shooter.idle();
                    }

                    double angleSpeed = input.gamepad.getRawAxis(Constants.kA_RS_Y) * Constants.kY_AXIS_MODIFIER;
                    shooter.angleSpeed(angleSpeed * Constants.kANGLE_SPEED_MODIFIER);
                    System.out.println("Angle Speed: " + (angleSpeed * Constants.kANGLE_SPEED_MODIFIER));
                }

                currentSubsystem = "Collector";
                if (Collector.getEnabled()) {
                    collector.setMotor(0);
                }
            } else {    // Collecting mode
                System.out.println("Collecting mode");
                currentSubsystem = "Collector";
                if (Collector.getEnabled()) {
                    if (trigger > Constants.kINPUT_DEADBAND) {
                        System.out.println("Trigger past deadband (value: " + trigger + ")");
                        status.sendStatus(Status.SHOOTING);
                        collector.setMotor(trigger);
                    } else {
                        System.out.println("Trigger in deadband");
                        sendStandardStatus();
                        collector.setMotor(0);
                    }
                }

                currentSubsystem = "Shooter";
                if (Shooter.getEnabled()) {
                    shooter.modifier(0, 0); // Clear shooter modifiers
                    shooter.idle();
                }
            }


        } catch (SubsystemException e) {
            status.sendStatus(Status.PROBLEM);
            System.out.println(currentSubsystem + " Problem: " + problemName(e) + ". Stack Trace:");
            e.printStackTrace();

            boolean isUninitialized = e.getClass().isInstance(SubsystemUninitializedException.class);
            if (currentSubsystem.equals("Shooter")
                && Shooter.getEnabled() && isUninitialized) {

                shooter.init();
            } else if (currentSubsystem.equals("Collector")
                && Collector.getEnabled() && isUninitialized) {
                    
                collector.init();
            }
        }
    }

    private String problemName(SubsystemException e) {
        if (e.getClass().isInstance(SubsystemDisabledException.class)) {
            return "Subsystem Disabled";
        } else if (e.getClass().isInstance(SubsystemUninitializedException.class)) {
            return "Subsystem Unitialized";
        } else {
            return "Generic Subsystem Problem";
        }
    }
}
