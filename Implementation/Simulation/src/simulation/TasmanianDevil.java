package simulation;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

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
	
	/** The number of ticks after the agent gets infected with DFT1.*/
	private int sickDFT1;
	
	/** The number of ticks after the agent gets infected with DFT2.*/
	private int sickDFT2;
	
	/** The number of ticks the agent is already dead.*/
	private int dead;
	
	/** True if the agent is vaccinated, false otherwise.*/
	private boolean vaccinated;
	
	/** This is the center around which the devil will move.*/
	private GridPoint home;
	
	/** The current state of the agent. Should only be set via StateManager.*/
	private AbstractState currentState;
	
	public TasmanianDevil(ContinuousSpace<Object> space, Grid<Object> grid, AbstractState startState)
	{
		this(space, grid, false, startState);
	}
	
	public TasmanianDevil(ContinuousSpace<Object> space, Grid<Object> grid, boolean vaccinated, AbstractState startState) {
		this.space = space;
		this.grid = grid;
		this.age = 0;
		this.sickDFT1 = 0;
		this.sickDFT2 = 0;
		if(vaccinated)
			this.vaccinate();
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

	public int getSickDFT1() {
		return sickDFT1;
	}
	
	public int getSickDFT2() {
		return sickDFT2;
	}

	public void incrementSick(int ticks) {
		if(sickDFT1 > 0)
			sickDFT1 += ticks;
		if(sickDFT2 > 0)
			sickDFT2 += ticks;
	}

	public int getDead() {
		return dead;
	}

	public void incrementDead(int ticks) {
		dead += ticks;
	}

	public boolean isVaccinated() {
		return vaccinated;
	}

	public void vaccinate() {
		vaccinated = true;
	}

	public GridPoint getHome() {
		return home;
	}

	public void setHome(GridPoint home) {
		this.home = home;
	}

	public AbstractState getCurrentState() {
		return currentState;
	}
}
