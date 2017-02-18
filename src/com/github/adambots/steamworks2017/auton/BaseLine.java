package com.github.adambots.steamworks2017.auton;



import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Notifier;
import com.ctre.CANTalon.TalonControlMode;



public class BaseLine {

	private CANTalon.MotionProfileStatus status = new CANTalon.MotionProfileStatus();
	
	private CANTalon _talon;
	
	private int state = 0;
	
	private int LoopTimeout = -1;
	
	private boolean start = false;
	
	private CANTalon.SetValueMotionProfile setvalue = CANTalon.SetValueMotionProfile.Disable;
	
	private static final int minPointsInTalon = 5;
	
	private static final int NumLoopsTimeout = 10;
	
	class PeriodicRunnable implements java.lang.Runnable{
		public void run() { _talon.processMotionProfileBuffer();    }
	}
	
	Notifier notifier = new Notifier(new PeriodicRunnable() );	
		
	
	
	
	public BaseLine(CANTalon talon) {
		_talon = talon; 
		_talon.changeMotionControlFramePeriod(5);
		notifier.startPeriodic(0.005);
		
		
	}











}





