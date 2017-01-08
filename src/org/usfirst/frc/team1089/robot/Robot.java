
package org.usfirst.frc.team1089.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private CANTalon rightFront, rightBack, leftFront, leftBack;
	private Joystick turn, throttle;
	private final double DEADZONE = 0.25, TURN_SHARPNESS = 0.5, SHARPNESS_INCREASE_MAX = 0.9;
	
	//TURN_SHARPNESS manipulates the voltage going to the motors, a number nearing 0 would increase
		//sharpness while a number nearing 1 would decrease sharpness.
	
	//private double rightIncreaseFactor, leftIncreaseFactor;
	
	public void robotInit() {
		rightFront = new CANTalon(1);
		leftFront = new CANTalon(3);
		rightBack = new CANTalon(2);
		leftBack = new CANTalon(4);
		
		rightBack.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftBack.changeControlMode(CANTalon.TalonControlMode.Follower);
		
		leftBack.set(leftFront.getDeviceID());
		rightBack.set(rightFront.getDeviceID());
		
		turn = new Joystick(0);
		throttle = new Joystick(1);
	}

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * <pre>
     * public double getThrottle()
     * </pre>
     * Gets the value of the throttle
     * 
     * @return the y-value of the throttle joystick, with deadzone calculated
     */
    public double getThrottle() {
    	return Math.abs(throttle.getY()) > DEADZONE ? throttle.getY() : 0;
    }
    
    
    /**
     * <pre>
     * public double getTurn()
     * </pre>
     * Gets the value of the turn
     * 
     * @return the x-value of the turn joystick, with deadzone calculated
     */
    public double getTurn() {
    	return Math.abs(turn.getX()) > DEADZONE ? turn.getX() : 0;
    }
    
    public void teleopPeriodic() {
    	double rightDecreaseFactor = 1.0;
    	double leftDecreaseFactor = 1.0;
		

		if (getThrottle() == 0 && getTurn() != 0) { // If we are only turning
			leftFront.set(getTurn());			
			rightFront.set(getTurn());
			
		} else if (getThrottle() != 0 && getTurn() != 0) { // If we are doing both turning and moving forward and backwards
			if(getTurn() < 0)
				leftDecreaseFactor = 
					1 + getTurn() * (TURN_SHARPNESS * getThrottle() < SHARPNESS_INCREASE_MAX ? Math.pow(Math.abs(getThrottle() / 10), 2) : 1.0);		//Initially this used only getThrottle(), but
			if(getTurn() > 0)																											//had to be getTurn() for turning.
				rightDecreaseFactor = 
					1 + getTurn() * -(TURN_SHARPNESS * getThrottle() < SHARPNESS_INCREASE_MAX ? Math.pow(Math.abs(getThrottle() / 10), 2) : 1.0);
			
			leftFront.set(getThrottle() * leftDecreaseFactor);				//Initially was getTurn() but had to be getThrottle()
			rightFront.set(getThrottle() * rightDecreaseFactor);				//to properly turn on an axis.
	
		} else { // By default, only try moving forward and backwards
			leftFront.set(-getThrottle());
			rightFront.set(getThrottle());
		}
		
/*		if(!leftXIsInDeadzone()){
			if(!rightYIsInDeadzone()) {									//left is right
				if(throttle.getX() < 0)
					leftDecreaseFactor = 1 + throttle.getX() / 2.0;
				if(throttle.getX() > 0)
					rightDecreaseFactor = 1 + throttle.getX() / -2.0;
			}
		} else {
			leftFront.set(-throttle.getY());
			rightFront.set(throttle.getY());
		}
		
		if (!rightYIsInDeadzone()) {
			// Throttle
	        leftFront.set(-turn.getY() * leftDecreaseFactor);			
	    	rightFront.set(turn.getY() * rightDecreaseFactor);
		} else {
			// Throttle
	        leftFront.set(0);			
	    	rightFront.set(0);
		}*/
    }
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}