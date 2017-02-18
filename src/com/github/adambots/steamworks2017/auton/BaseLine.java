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
	public void reset() {
		_talon.clearMotionProfileTrajectories();
		setvalue = CANTalon.SetValueMotionProfile.Disable;
		state = 0;
		LoopTimeout = -1;
	    start = false;
	
	}
	
	
	
	public void control() {
		_talon.getMotionProfileStatus(status);
		
		if (LoopTimeout < 0) {
		} else { 
			
			if (LoopTimeout == 0) {
				
				instrumentation.OnNoProgress();
			} else {		
				
			--LoopTimeout;
			
			}
		}
		
	

	if (_talon.getControlMode() != TalonControlMode.MotionProfile) {
		state = 0;
		LoopTimeout = -1;
	} else {
		switch (state) {
		  case 0:
			  if (start) { 
				  start = false;
				  setvalue = CANTalon.SetValueMotionProfile.Disable;
				  startFilling();
				  state = 1;
				  LoopTimeout = NumLoopsTimeout;
			      }			
			  
			  break;
		  case 1:
			  if (status.btmBufferCnt > minPointsInTalon) {
				  
				  setvalue = CANTalon.SetValueMotionProfile.Enable;
				  state = 2;
				  LoopTimeout = NumLoopsTimeout;
			  }
				  
		  case 2: if (status.isUnderrun == false) { 
			  		LoopTimeout = NumLoopsTimeout;
				  
			  }
		
		  		if (status.activePointValid && status.activePoint.isLastPoint) {
		
		  			setvalue = CANTalon.SetValueMotionProfile.Hold;
		  			state = 0;
		  			LoopTimeout = -1; 
		  		}
		  		break;
		  	}

		}
			instrumentation.process(status);
}                                                     
		

	private void StartFilling() {
	
		startFilling(BaseLinePoints.Points, BaseLinePoints.kNumPoints);
}
	
private void startFilling(double[][] profile, int totalCnt){
	CANTalon.TrajectoryPoint point = new CANTalon.TrajectoryPoint();
	if(status.hasUnderrun){
		intrumentation.OnUnderrun();
		_talon.clearMotionProfileHasUnderrun();
	}
	_talon.clearMotionProfileTrajectories();
	for(int i = 0; i < totalCnt; ++i){
		point.position = profile[i][0];
		point.velocity = profile[i][1];
		point.timeDurMs = (int) profile[i][2];
		point.profileSlotSelect = 0;
		point.velocityOnly = false;
		point.zeroPos = false;
		if(i == 0)
			point.zeroPos = true;
		
		point.isLastPoint = false;
		if((i + 1) == totalCnt)
			point.isLastPoint = true;
		
		_talon.pushMotionProfileTrajectory(point);
	}
}
void startMotorProfile() {
	start = true;

}
	
CANTalon.SetValueMotionProfile getsetvalue () {
	return setvalue;
}

}





