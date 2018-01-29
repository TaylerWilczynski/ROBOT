package org.usfirst.frc.team5504.robot;

//All the imports necessary from the Java library to use methods/classes
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;

//Beginning class to define what type of robot setup the code should follow.
public class Robot extends IterativeRobot 
{
	
	//Type of robot drive method named myRobot.
	MecanumDrive myRobot;
	
	//Joy stick used to drive the robot
	Joystick stick;
	
	//Timer to keep track of the time in the match.
	Timer timer;
	
	//The 4 TalonSRX motors and their corresponding names.
	WPI_TalonSRX frontLeft, rearLeft, frontRight, rearRight, conveyer, liftOne, liftTwo, liftThree;
	
	//Method ran when the robot first boots up. Robot Basic Parameters
	@Override
	public void robotInit() 
	{
		//Display on SmartDashboard the options of Autonomous and initialize it with value 0.
		SmartDashboard.putNumber("(Left = 1, Right = 2) Autonomous Mode:   ", 0);
		
		//Assigned serial codes for CANTalon motors to control.
		
		//Wheel Motor Controllers
		frontLeft = new WPI_TalonSRX(1);		//Front Left Wheel
		rearLeft = new WPI_TalonSRX(2);			//Rear Left Wheel
		frontRight = new WPI_TalonSRX(3);		//Front Right Wheel
		rearRight = new WPI_TalonSRX(4);		//Rear Right Wheel
		
		//Lift Motor Controllers
		liftOne = new WPI_TalonSRX(5);			
		liftTwo = new WPI_TalonSRX(6);
		liftThree = new WPI_TalonSRX(7);
		
		//Conveyer Motor Controller
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
	public void autonomousInit() 
	{
		//Reset timer
		timer.reset();
		//Start timer
		timer.start();

	}

	//When Autonomous is Running, this code will run.
	@Override
	public void autonomousPeriodic() 
	{
		//If value 1 entered, do this.
		if (SmartDashboard.getNumber("(Left = 1, Right = 2) Autonomous Mode:   ", 0) == 1.0) 
		{
			while (timer.get() < 8.5) 
			{
				myRobot.driveCartesian(0, -0.3, 0);
			}
			while (timer.get() < 10.9) 
			{
				myRobot.driveCartesian(0, 0, 0.3);
			}
			while (timer.get() < 11.67) 
			{
				myRobot.driveCartesian(0, -0.3, 0);
			}
		}
		//If value 2 entered, do this.
		else if (SmartDashboard.getNumber("(Left = 1, Right = 2) Autonomous Mode:   ", 0) == 2.0) 
		{
			while (timer.get() < 8.5) 
			{
				myRobot.driveCartesian(0, -0.3, 0);
			}
			while (timer.get() < 10.9) 
			{
				myRobot.driveCartesian(0, 0, -0.3);
			}
			while (timer.get() < 11.67) 
			{
				myRobot.driveCartesian(0, -0.3, 0);
			}		
		}
			
	}
	//When Teleop is Running, this code will run.
	@Override
	public void teleopPeriodic() 
	{
		//Scaled and Dead-zoned Axis Variables
		double scaledDeadZoneX;
		double scaledDeadZoneY;
		double scaledDeadZoneTwist;
		
		//Throttle Variable
		double throttle;
		
		//ThreshHold Variables for removing high-sensitivity
		double threshHoldY = 0.1;
		double threshHoldX = 0.2;
		double threshHoldZ = 0.1;
		
		//Set Throttle to start at 0 (Lowest) and max out at 1 (Highest).
		throttle = (stick.getThrottle() * -1 + 1) / 2;
		
		//Sets power to motors if joystick pushed far enough left/right. 0 power if not.
		if (stick.getX() > threshHoldX || stick.getX() < threshHoldX * -1) 
		{
			scaledDeadZoneX = map(stick.getX(), threshHoldX, 1, 0, 1);
			System.out.println("Scaled: " + scaledDeadZoneX + "\t Raw: " + stick.getX());
		}
		else 
		{
			scaledDeadZoneX = 0;
		}
		
		//Sets power to motors if joystick pushed far enough up/down. 0 power if not.
		if (stick.getY() > threshHoldY || stick.getY() < threshHoldY * -1) 
		{
			scaledDeadZoneY = map(stick.getY(), threshHoldY, 1, 0, 1);
		}
		else 
		{
			scaledDeadZoneY = 0;
		}
		
		//Sets power to motors if joystick twisted far enough left/right. 0 power if not.
		if (stick.getTwist() > threshHoldZ || stick.getTwist() < threshHoldZ * -1) 
		{
			scaledDeadZoneTwist = map(stick.getTwist(), threshHoldZ, 1, 0, 1);
		}
		else 
		{
			scaledDeadZoneTwist = 0;
		}
		
		//Sets X, Y, and Z axis power values to the scaled and deadzoned axis variables multiplied by the throttle.
        myRobot.driveCartesian(
        		(scaledDeadZoneX * throttle),
        		(scaledDeadZoneY * throttle * -1),
        		(scaledDeadZoneTwist * throttle), 0);
		
        //If top left button is pressed, set lift motors to go forward.
        if (stick.getRawButton(5)) 
        {
        	liftOne.set(.2);
        	liftTwo.set(.2);
        	liftThree.set(.2);
        }
        //If botton left button is pressed, set lift motors to go backwards.
        else if (stick.getRawButton(3)) 
        {
        	liftOne.set(-.2);
        	liftTwo.set(-.2);
        	liftThree.set(-.2);
        }
        //If neither button is pressed, set power to 0.
        else 
        {
        	liftOne.set(0);
        	liftTwo.set(0);
        	liftThree.set(0);
        }
        
        //If top right button is pressed, set conveyer motor to go forward.
        if (stick.getRawButton(6)) 
        {
        	conveyer.set(-1);
        }
        //If bottom right button is pressed, set conveyer motor to go backwards.
        else if (stick.getRawButton(4)) 
        {
        	conveyer.set(1);
        }
        //If neither button is pressed, set power to 0.
        else 
        {
        	conveyer.set(0);
        }
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() 
	{
		LiveWindow();
	}

	private void LiveWindow() 
	{
		// TODO Auto-generated method stub	
	}
	
	//Mapping function to prevent motors from starting at 10% power by default. Scales variables.
	double map(double x, double in_min, double in_max, double out_min, double out_max)
	{
	  return (x - in_min) * (out_max - out_min + 1) / (in_max - in_min + 1) + out_min;
	}
}
