package controls;

import java.util.Random;

import repast.simphony.context.Context;
import simulation.Environment;
import simulation.TasmanianDevil;
import states.AbstractState;
import states.DeathState;

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
		// despawn corpse after 30 days
		if(devil.getDead() > 0) {
			devil.incrementDead(1);
			if(TickParser.getCompleteDaysFromTicks(devil.getDead()) > 30) {
				context.remove(devil);
			}
		}
		
		// at age of 5, devils die
		if(TickParser.getYearsFromTicks(devil.getAge()) >= 5) {
			if(devil.isInfectiousDFT1() || devil.isInfectiousDFT2()) {
				devil.incrementDead(1);
				return new DeathState();
			} else {
				context.remove(devil);
			}
		}
		
		if(TickParser.getYearsFromTicks(devil.getAge()) > 0) {
			double naturalDeathRate = Environment.getInstance().getNaturalDeathRate();
			double deathRatePerDevil = Math.pow(1 - naturalDeathRate, 1./Environment.getInstance().getTicksPerYear());
		
			Random rnd = new Random();
			double rate = rnd.nextDouble();
		
			if (rate >= deathRatePerDevil) {
				if(devil.isInfectiousDFT1() || devil.isInfectiousDFT2()) {
					devil.incrementDead(1);
					return new DeathState();
				} else {
					context.remove(devil);
				}
				System.out.println("Devil was killed by chance! Generated Number: " + rate + " Age of devil: " + devil.getAge() + " ticks");
			}
		}
		return devil.getCurrentState();
	}
	
}
