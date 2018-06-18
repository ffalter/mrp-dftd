package controls;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import simulation.TasmanianDevil;

public class TerminationControl {
	Context<Object> context;
	
	public TerminationControl(Context<Object> context) {
		this.context= context;
	}
	@ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.FIRST_PRIORITY)
	public void testForEnd() {
		int totalPopulation = context.getObjects(TasmanianDevil.class).size();
		// Stop the simulator
		if (totalPopulation == 0) {
			RunEnvironment.getInstance().endRun();
		}
	}
}
