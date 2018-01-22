package org.usfirst.frc.team5504.robot;

//All the imports necessary from the Java library to use methods/classes
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	//Type of robot drive method named myRobot.
	MecanumDrive myRobot;
	
	//Joy stick used to drive the robot
	Joystick stick;
	
	//Timer to keep track of the time in the match.
	Timer timer;
	
	//The 4 TalonSRX motors and their corresponding names.
	WPI_TalonSRX frontLeft, rearLeft, frontRight, rearRight, conveyer, liftOne, liftTwo, liftThree;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		//Assigned serial codes for CANTalon motors to control.
		frontLeft = new WPI_TalonSRX(1);
		rearLeft = new WPI_TalonSRX(2);
		frontRight = new WPI_TalonSRX(3);
		rearRight = new WPI_TalonSRX(4);
		liftOne = new WPI_TalonSRX(5);
		liftTwo = new WPI_TalonSRX(6);
		liftThree = new WPI_TalonSRX(7);
		conveyer = new WPI_TalonSRX(8);
		
		
		//Camera service to get the camera image at start.
		CameraServer.getInstance().startAutomaticCapture();
		
		//Assigned motors to corresponding places in the myRobot Mecanum Drive.
		myRobot = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);
		
		//Assigned stick USB port to 0.
		stick = new Joystick(0);
		
		//Created object and reference variable for the timer value.
		timer = new Timer();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		//Reset timer and then started it upon auton bootup
		timer.reset();
		timer.start();

	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		//Loop for auton to follow based on time value.
		while (timer.get() < 9.0){
			myRobot.driveCartesian(0, 0.3, 0);
		}

		}
		
		
	

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		//Variables for movement values.
		double scaledDeadZoneX;
		double scaledDeadZoneY;
		double scaledDeadZoneTwist;
		double throttle;
		double threshHold = 0.1;
				
		throttle = (stick.getThrottle() * -1 + 1) / 2;
		
		if (stick.getX() > threshHold || stick.getX() < threshHold * -1) {
			scaledDeadZoneX = map(stick.getX(), threshHold, 1, 0, 1);
			System.out.println("Scaled: " + scaledDeadZoneX + "\t Raw: " + stick.getX());
		}
		else {
			scaledDeadZoneX = 0;
		}
		
		if (stick.getY() > threshHold || stick.getY() < threshHold * -1) {
			scaledDeadZoneY = map(stick.getY(), threshHold, 1, 0, 1);
		}
		else {
			scaledDeadZoneY = 0;
		}
		
		if (stick.getTwist() > threshHold || stick.getTwist() < threshHold * -1) {
			scaledDeadZoneTwist = map(stick.getTwist(), threshHold, 1, 0, 1);

		}
		else {
			scaledDeadZoneTwist = 0;
		}
		
        myRobot.driveCartesian(
        		(scaledDeadZoneX * throttle),
        		(scaledDeadZoneY * throttle * -1),
        		(scaledDeadZoneTwist * throttle), 0);
		
        if (stick.getRawButton(5)) {
        	liftOne.set(.2);
        	liftTwo.set(.2);
        	liftThree.set(.2);
        }
        else if (stick.getRawButton(3)) {
        	liftOne.set(-.2);
        	liftTwo.set(-.2);
        	liftThree.set(-.2);
        }
        else {
        	liftOne.set(0);
        	liftTwo.set(0);
        	liftThree.set(0);
        }
        
        if (stick.getRawButton(6)) {
        	conveyer.set(0.2);
        }
        else if (stick.getRawButton(4)) {
        	conveyer.set(-0.2);
        }
        else {
        	conveyer.set(0);
        }
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		LiveWindow();
	}

	private void LiveWindow() {
		// TODO Auto-generated method stub
		
	}
	
	double map(double x, double in_min, double in_max, double out_min, double out_max)
	{
	  return (x - in_min) * (out_max - out_min + 1) / (in_max - in_min + 1) + out_min;
	}
}
