package org.usfirst.frc.team5173.robot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID.Hand;
/**
 * This is a 
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	RobotDrive Robot = new RobotDrive(1, 2);
	
	Encoder encoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);

	Spark Sparky_ForthBackith = new Spark(3);
	
	XboxController controller = new XboxController(0);
	
	Timer timer = new Timer();
	
	DoubleSolenoid Claw = new DoubleSolenoid(1,2);
	
	Compressor c = new Compressor(1);
	
	Spark Voltage_Lift = new Spark (4);
	{
		
	c.start();
	
	}
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		encoder.setMaxPeriod(.1);
		encoder.setMinRate(10);
		encoder.setDistancePerPulse(1/1024 * 1.625 * Math.PI/35);
	}

	/**
	 * This function is run once each time the robot enters autonomous mode
	 */
	@Override
	public void autonomousInit() {
		
		timer.reset();
		
		timer.start();
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		double distance = encoder.getDistance();
		// Drive for 2 seconds
		if (encoder.getDistance() < 2.0) {
			
			Robot.drive(-0.5, 0.0); // drive forwards half speed
			
		} else {
			
			Robot.drive(0.0, 0.0); // stop robot
			
		}
		//restarts timer
		timer.reset();
		
		timer.start();
		
		// turn one direction for 2 seconds
		if (timer.get()< 2.0) {
			
			Robot.drive(0.0, -0.5);
			
		} else { 
			
			Robot.drive(0, 0);//stop
			
		}
		// Restart Timer
		timer.reset();
		
		timer.start();
		
		// turn opposite direction for 2 seconds
		if (timer.get() < 2.0) {
			
			Robot.drive(0.0, 0.5);
			
		} else {
			
			Robot.drive(0.0, 0.0);// stop
			
		} 
		//Restart Timer
		timer.reset();
		
		timer.start();
		
		//drives backward for 2 seconds
		if (timer.get() < 2.0) {
			
			Robot.drive(0.5, 0.0);
			
		} else {
			
			Robot.drive(0.0, 0.0);//stop
			
		}
	}

	/**
	 * This function is called once each time the robot enters tele-operated
	 * mode
	 */
	@Override
	public void teleopInit() {
	}
	Relay exampleRelay = new Relay(1);
	
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		
		//Robot.arcadeDrive(controller);
		
		//Robot.arcadeDrive(controller.getY(Hand.kLeft), controller.getX(Hand.kLeft));
		
		//Robot.arcadeDrive(controller.getRawAxis(1), controller.getRawAxis(0));
		double distance = encoder.getDistance();
		double rawY = controller.getRawAxis(1);
		rawY = Math.pow(rawY, 3);
		double rawX = controller.getRawAxis(0);
		rawX = Math.pow(rawX, 3);
		Robot.arcadeDrive(rawY, rawX);
		
		if(controller.getBButton() == true) {
			Claw.set(DoubleSolenoid.Value.kForward);
		}
		if(controller.getAButton() == true) {
			Claw.set(DoubleSolenoid.Value.kReverse);
		}
		//double count = 0;
		if(controller.getBumper(Hand.kLeft) /*&& encoder.getDistance() < 5*/) {
			Sparky_ForthBackith.set(0.75);
			//Sparky.set(Math.pow(count, 2));
			//count += 0.5;
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}*/
		} else {
		
		//double count2 = 0;
			if(controller.getBumper(Hand.kRight) /*&& encoder.getDistance() < 5*/) {
			Sparky_ForthBackith.set(-0.75);
			//Sparky.set(Math.pow(count2, 2));
			//count2 -= 0.5;
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}*/
			} else { Sparky_ForthBackith.set(0.0); }
		}
		if(controller.getTriggerAxis(Hand.kLeft) > .7) {
			Voltage_Lift.set(0.5);
		} else {
			if(controller.getTriggerAxis(Hand.kRight) > .7){ Voltage_Lift.set(-0.5);		
				} else { Voltage_Lift.set(0.0);}
		} 
	
	    c.setClosedLoopControl(true);
	}
	@Override
	public void testPeriodic() {
		
		LiveWindow.run();
		//SmartDashboard.putNumber("encoder Distance", Encoder.getDistnace);
	}
}