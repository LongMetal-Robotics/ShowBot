package frc.robot;

import java.io.File;
import java.util.Scanner;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Preferences;

import org.longmetal.Arduino.Status;
import org.longmetal.*;
import org.longmetal.util.Listener;

public class Robot extends TimedRobot {
    private static final String DEPRECATION = "deprecation";

    Input input;
    DriveTrain driveTrain;
    Arduino status;
    Status currentStatus;
    Shooter shooter;
    Collector collector;
    Preferences prefs;

    SendableChooser<Boolean> chooserQuinnDrive;

    Listener QuinnDrive;
    Listener forwardDrive;
    Listener reverseDrive;

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
        status = new Arduino();
        currentStatus = Status.DISABLED;
        prefs = Preferences.getInstance();

        shooter = new Shooter();
        collector = new Collector();

        QuinnDrive = new Listener(/* onTrue */ new Runnable(){ public void run() { input.setQuinnDrive(true); } },
            /* onFalse */ new Runnable(){ public void run() { input.setQuinnDrive(false); } });

        chooserQuinnDrive = new SendableChooser<>();
        chooserQuinnDrive.addDefault("Disabled", false);
        chooserQuinnDrive.addObject("Enabled", true);
        SmartDashboard.putData("Quinn Drive Chooser", chooserQuinnDrive);

        forwardDrive = new Listener(new Runnable(){ // This looks like a really janky way to do it but it should work well
            public void run() { // Forward
                driveTrain.setReverseDrive(false);
                if (status.isReady()) {
                    status.sendStatus(Status.FORWARD);
                }
            }
        }, new Runnable(){
            public void run() { // Reverse
                driveTrain.setReverseDrive(true);
                if (status.isReady()) {
                    status.sendStatus(Status.BACKWARD);
                }
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
    }

    @Override
    public void disabledInit() {
        currentStatus = Status.DISABLED;
    }

    @Override
    public void disabledPeriodic() {
        if (status.isReady()) {
            status.sendStatus(Status.DISABLED);
        }
    }

    @Override
    public void teleopInit() {
        currentStatus = Status.ENABLED;
    }

    @Override
    public void teleopPeriodic() {
        if (status.isReady()) {
            if (shooter.isShooting()) {
                status.sendStatus(Status.SHOOTING);
            } else {
                status.sendStatus(Status.ENABLED);
            }
        }

        driveTrain.curve(input.forwardStick.getY(),
            input.forwardStick.getThrottle(),
            input.turnStick.getTwist(),
            input.turnStick.getThrottle());
    }

    public Arduino.Status currentStatus() {
        return currentStatus;
    }
}
