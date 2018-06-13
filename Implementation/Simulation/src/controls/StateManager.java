package controls;

import repast.simphony.context.Context;
import simulation.TasmanianDevil;
import states.AbstractState;
import states.DeathState;
import states.FemaleSickState;
import states.MaleSickState;

/**
 * The StateManager will be used, to determine the state for the agent.
 * @author dludwig
 *
 */
public class StateManager {
	
	private static StateManager instance;
	private static Context<Object> context;
	
	private StateManager() {}
	
	public static void initialize (Context<Object> theContext) {
		context = theContext;
	}
	
	public static StateManager getInstance() {
		if(instance == null) {
			instance = new StateManager();
		}
		return instance;
	}
	
	public static AbstractState getState(TasmanianDevil devil)
	{
		//remove dead devil after 30 days
		if(devil.getDead() > 0) {
			devil.incrementDead(1);
			if(TickParser.getCompleteDaysFromTicks(devil.getDead()) > 30) {
				context.remove(devil);
			}
		}else if(TickParser.getYearsFromTicks(devil.getAge()) >= 5) {
			// at age of 5, devils die
			if(devil.isInfectiousDFT1() || devil.isInfectiousDFT2()) {
				devil.incrementDead(1);
				return new DeathState();
			} else {
				context.remove(devil);
			}
		}else if(devil.getSickDFT1()>0 || devil.getSickDFT2()>0) {
			if(devil.isFemale()) {
				return new FemaleSickState();
			}else {
				return new MaleSickState();
			}
		}
		return devil.getCurrentState();
	}
	
}
