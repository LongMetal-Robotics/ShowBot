package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.longmetal.Input;
import org.longmetal.Arduino.Status;
import org.longmetal.DriveTrain;
import org.longmetal.Constants;
import org.longmetal.Arduino;

public class Robot extends TimedRobot {
    private static final String DEPRECATION = "deprecation";
    private final static String kBRANCH = "master";
    private final static String kCOMMIT = "c64c2f1";

    Input input;
    DriveTrain driveTrain;
    Arduino status;

    SendableChooser<Boolean> chooserQuinnDrive;

    boolean lastQuinnDrive = false;
    boolean lastForwardDrive = false;
    boolean lastReverseDrive = false;

    @Override
    @SuppressWarnings(DEPRECATION)
    public void robotInit() {
        System.out.println("Commit " + kCOMMIT + " or later (branch '" + kBRANCH + "')");

        input = new Input(Constants.kLEFT_STICK, Constants.kRIGHT_STICK);
        driveTrain = new DriveTrain();
        status = new Arduino();

        chooserQuinnDrive = new SendableChooser<>();
        chooserQuinnDrive.addDefault("Disabled", false);
        chooserQuinnDrive.addObject("Enabled", true);
        SmartDashboard.putData("Quinn Drive Chooser", chooserQuinnDrive);
    }
    
    @Override
    public void robotPeriodic() {
        SmartDashboard.putData("Drive Train", driveTrain.driveTrain);
        SmartDashboard.putBoolean("Quinn Drive", input.isQuinnDrive());
        boolean quinnDrive = (Boolean)chooserQuinnDrive.getSelected();
        if (quinnDrive != lastQuinnDrive) {
            input.setQuinnDrive(quinnDrive);
        }
        lastQuinnDrive = quinnDrive;

        // Reverse Drive mode

        boolean forwardDrive = input.forwardStick.getRawButtonPressed(Constants.kFORWARD_BUTTON);
        boolean reverseDrive = input.forwardStick.getRawButton(Constants.kREVERSE_BUTTON);

        if (forwardDrive && forwardDrive != lastForwardDrive && !reverseDrive) { // If it is pressed and it changed and both aren't pressed
            // Set forward drive
            driveTrain.setReverseDrive(false);
	    if (status.isReady()) {
	        status.sendStatus(Status.FORWARD); // HEY! IF SOMETHINGS NOT WORKING ITS PROBABLY THIS
	    }
        }
        lastForwardDrive = forwardDrive;

        if (reverseDrive && reverseDrive != lastReverseDrive && !forwardDrive) { // If it is pressed and it changed and both aren't pressed
            // Set reverse drive
            driveTrain.setReverseDrive(true);
	    if (status.isReady()) {
                status.sendStatus(Status.BACKWARD);
            }
        }
        lastReverseDrive = reverseDrive;

        SmartDashboard.putBoolean("Reverse Drive", driveTrain.getReverseDrive());
    }

    @Override
    public void disabledPeriodic() {
        if (status.isReady()) {
            status.sendStatus(Status.DISABLED);
        }
    }

    @Override
    public void teleopPeriodic() {
        if (status.isReady()) {
            status.sendStatus(Status.ENABLED);
        }

        driveTrain.curve(input.forwardStick.getY(),
            input.forwardStick.getThrottle(),
            input.turnStick.getTwist(),
            input.turnStick.getThrottle());
    }
}
