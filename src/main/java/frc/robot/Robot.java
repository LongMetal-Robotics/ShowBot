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
    private final static String kBRANCH = "arduino-communication";
    private final static String kCOMMIT = "f44a971";

    Input input;
    DriveTrain driveTrain;
    Arduino status;

    SendableChooser<Boolean> chooserQuinnDrive;
    boolean lastQuinnDrive = false;

    @Override
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