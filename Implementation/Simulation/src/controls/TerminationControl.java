package controls;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.collections.IndexedIterable;
import simulation.Environment;
import simulation.TasmanianDevil;

public class TerminationControl {
	Context<Object> context;
	
	public TerminationControl(Context<Object> context) {
		this.context= context;
	}
	@ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.FIRST_PRIORITY)
	public void testForEnd() {
		IndexedIterable<Object> totalPopulation = context.getObjects(TasmanianDevil.class);
		// Stop the simulator
		if (totalPopulation.size() < Environment.getInstance().getMinPopulationSize()) {
			RunEnvironment.getInstance().endRun();
		}
		
		/*
		for(Object obj: totalPopulation) {
			TasmanianDevil td= (TasmanianDevil)obj;
			if(td.getSickDFT1()>0||td.getSickDFT2()>0) {
				return;
			}
		}
		RunEnvironment.getInstance().endRun();
		*/
		
	}
}
