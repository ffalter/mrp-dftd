package simulation;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * This class describes all attributes of an agent in the simulation.
 * Because the behaviour depends on the currentState the agent only knows the currentState and not all the different behaviours.
 * @author dludwig
 *
 */
public class TasmanianDevil {
	/** This is the continuous space, the agents are interacting on.*/
	ContinuousSpace<Object> space;
	
	/** This is the grid, the calculations will be based on.*/
	Grid<Object> grid;
	
	/** The number of ticks the agent is alive.*/
	private int age;
	
	/** The number of ticks after the agent gets infected.*/
	private int sick;
	
	/** The current state of the agent. Should only be set via StateManager.*/
	private AbstractState currentState;

	public TasmanianDevil(ContinuousSpace<Object> space, Grid<Object> grid, AbstractState startState) {
		this.space = space;
		this.grid = grid;
		this.age = 0;
		this.sick = 0;
		this.currentState = startState;
	}
	
	/**
	 * This function will be called from the repast-framework.
	 * @throws Exception 
	 */
	
	
	@ScheduledMethod(start = 1, interval = 1) // Call every iteration of the simulation
	public void run() throws Exception
	{
		System.out.println("Step: " + currentState.getClass().getName());
		if(currentState == null)
			throw new Exception("There is no state defined for this agent! Step cannot be evaluated");
		currentState.step(this);
		StateManager.getState(this);
	}

	public int getAge() {
		return age;
	}

	public void incrementAge(int ticks) {
		age += ticks;
	}

	public int getSick() {
		return sick;
	}

	public void incrementSick(int ticks) {
		ticks += ticks;
	}
	
	public AbstractState getCurrentState() {
		return currentState;
	}
}
