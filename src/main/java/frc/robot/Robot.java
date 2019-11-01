package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.longmetal.Input;
import org.longmetal.DriveTrain;
import org.longmetal.Constants;

public class Robot extends TimedRobot {
    /**
     *
     */

    private static final String DEPRECATION = "deprecation";
    private final static String kBRANCH = "master";
    private final static String kCOMMIT = "e9c4b24";

    Input input;
    DriveTrain driveTrain;
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
        }
        lastForwardDrive = forwardDrive;

        if (reverseDrive && reverseDrive != lastReverseDrive && !forwardDrive) { // If it is pressed and it changed and both aren't pressed
            // Set reverse drive
            driveTrain.setReverseDrive(true);
        }
        lastReverseDrive = reverseDrive;

        SmartDashboard.putBoolean("Reverse Drive", driveTrain.getReverseDrive());
    }

    @Override
	public void teleopPeriodic() {
        driveTrain.curve(input.forwardStick.getY(),
            input.forwardStick.getThrottle(),
            input.turnStick.getTwist(),
            input.turnStick.getThrottle());
	}
}