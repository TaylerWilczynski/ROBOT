package org.usfirst.frc.team5504.robot;

//All the imports necessary from the Java library to use methods/classes
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;

//Beginning class to define what type of robot setup the code should follow.
public class Robot extends IterativeRobot 
{
	
	//Type of robot drive method named myRobot.
	MecanumDrive myRobot;
	
	//Joy stick used to drive the robot
	Joystick stick;
	
	//Timer to keep track of the time in the match.
	Timer timer;
	
	//Compressor object to run/shut off compressor.
	Compressor c = new Compressor(0);
	
	//Double Solenoid object that extends through terminal 0 and retracts through terminal 1.
	DoubleSolenoid grabSolenoid = new DoubleSolenoid(0, 1);
	
	//The 6 TalonSRX motors and their corresponding names.
	WPI_TalonSRX frontLeft, rearLeft, frontRight, rearRight, conveyer, winch;
	
	//Method ran when the robot first boots up. Robot Basic Parameters
	@Override
	public void robotInit() 
	{
		//Display on SmartDashboard the options of Autonomous and initialize it with value 0.
		SmartDashboard.putNumber("(Left = 1, Middle = 2, Right = 3) Autonomous Mode:   ", 0);
						
		//Wheel Motor Controllers
		frontLeft = new WPI_TalonSRX(1);		//Front Left Wheel
		rearLeft = new WPI_TalonSRX(2);			//Rear Left Wheel
		frontRight = new WPI_TalonSRX(3);		//Front Right Wheel
		rearRight = new WPI_TalonSRX(4);		//Rear Right Wheel
		
		//Conveyer Motor Controller
		conveyer = new WPI_TalonSRX(8);
		
		//Winch Motor Controller
		winch = new WPI_TalonSRX(6);
		
		//Camera service to get the camera image at start.
		CameraServer.getInstance().startAutomaticCapture();
		
		//Assigned motors to corresponding places in the myRobot Mecanum Drive.
		myRobot = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);
		
		//Assigned stick USB port to 0.
		stick = new Joystick(0);
		
		//Created object and reference variable for the timer value.
		timer = new Timer();
		
		c.start();
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
		//Initialized string variable to hold FMS value.
		String gameData;
		//Retrieve FMS values to use for Auton mode.
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		//Value 1 assumes we are on the left side of the field.
		if (SmartDashboard.getNumber("(Left = 1, Middle = 2, Right = 3) Autonomous Mode:   "
				, 0) == 1.0) 
		{
			if(gameData.length() > 0)
			{
				if(gameData.charAt(0) == 'L')
				{
					
				} 
				
				else 
				{
					
				}
            }
			
		}
		//Value 2 assumes we in the middle of the field.
		else if (SmartDashboard.getNumber("(Left = 1, Right = 2) Autonomous Mode:   "
				, 0) == 2.0) 
		{
			if(gameData.length() > 0)
			{
				if(gameData.charAt(0) == 'L')
				{
					
				} 
				
				else 
				{
					
				}
            }
		}
		
		else if (SmartDashboard.getNumber("(Left = 1, Middle = 2, Right = 3) Autonomous Mode:   "
				,0) == 3.0) 
		{
			if(gameData.length() > 0)
			{
				if(gameData.charAt(0) == 'L')
				{
					
				} 
				
				else 
				{
					
				}
            }
		}
			
	}
	//When Teleop is Running, this code will run.
	@Override
	public void teleopPeriodic() 
	{
		c.setClosedLoopControl(true);
		
		//Scaled and Dead-zoned Axis Variables
		double scaledDeadZoneX;
		double scaledDeadZoneY;
		double scaledDeadZoneTwist;
		
		//Throttle Variable
		double throttle;
		
		//ThreshHold Variables for removing high-sensitivity
		double threshHoldY = 0.1;
		double threshHoldX = 0.2;
		double threshHoldZ = 0.4;
		
		//Set Throttle to start at 0 (Lowest) and max out at 1 (Highest).
		throttle = (stick.getThrottle() * -1 + 1) / 2;
		
		//Sets power to motors if joystick pushed far enough left/right. 0 power if not.
		if (stick.getX() > threshHoldX || stick.getX() < threshHoldX * -1) 
		{
			scaledDeadZoneX = stick.getX();
		}
		else 
		{
			scaledDeadZoneX = 0;
		}
		
		//Sets power to motors if joystick pushed far enough up/down. 0 power if not.
		if (stick.getY() > threshHoldY || stick.getY() < threshHoldY * -1) 
		{
			scaledDeadZoneY = stick.getY();
		}
		else 
		{
			scaledDeadZoneY = 0;
		}
		
		//Sets power to motors if joystick twisted far enough left/right. 0 power if not.
		if (stick.getTwist() > threshHoldZ || stick.getTwist() < threshHoldZ * -1) 
		{
			scaledDeadZoneTwist = stick.getTwist();
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
		
        /**
         * Lift Pneumatics Buttons
        */
        
        //If top left button is pressed, send air to the lift.
        if (stick.getRawButton(5) && !stick.getRawButton(3)) 
        {
        	//grabSolenoid.set(DoubleSolenoid.Value.kReverse);
        	grabSolenoid.set(DoubleSolenoid.Value.kReverse);
        }
        //If bottom left button is pressed, retract air from the lift.
        else if (stick.getRawButton(3) && !stick.getRawButton(5)) 
        {
        	grabSolenoid.set(DoubleSolenoid.Value.kForward);
        }
        //If neither button is pressed, don't take/send air to the lift.
        else if (!stick.getRawButton(5) && !stick.getRawButton(3))
        {
        	grabSolenoid.set(DoubleSolenoid.Value.kOff);
        }
        
        /**
         * Conveyer Buttons
        */
        
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
        
        /**
         * Winch Buttons
        */
        
        //If button 12 on the left is pressed, set winch motor to spin.
        //Additionally, stop the compressor to save voltage.
        if (stick.getRawButton(12)) 
        {
        	c.stop();
        	winch.set(1);
        }
        
        //If the button is not pressed, set the motor power to 0.
        else 
        {
        	winch.set(0);
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
