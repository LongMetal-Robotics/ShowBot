package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.longmetal.Input;
import org.longmetal.DriveTrain;
import org.longmetal.Constants;

public class Robot extends TimedRobot {
    private Input input;
    private DriveTrain driveTrain;
    SendableChooser chooserQuinnDrive;
    boolean lastQuinnDrive = false;

    @Override
    public void robotInit() {
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
    }

    @Override
	public void teleopPeriodic() {
        driveTrain.curve(input.forwardStick.getY(),
            input.forwardStick.getThrottle(),
            input.turnStick.getTwist(),
            input.turnStick.getThrottle());
	}
}