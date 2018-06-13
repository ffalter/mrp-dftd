package simulation;

import controls.StateManager;
import controls.TickParser;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import states.AbstractState;

/**
 * This class describes all attributes of an agent in the simulation.
 * Because the behaviour depends on the currentState the agent only knows the currentState and not all the different behaviours.
 * @author dludwig
 *
 */
public class TasmanianDevil {
	/** This is the continuous space, the agents are interacting on.*/
	private ContinuousSpace<Object> space;
	
	/** This is the grid, the calculations will be based on.*/
	private Grid<Object> grid;
	
	/** The number of ticks the agent is alive.*/
	private int age;
	
	/** The number of ticks after the agent gets infected with DFT1.*/
	private int sickDFT1;
	
	/** The number of ticks after the agent gets infected with DFT2.*/
	private int sickDFT2;
	
	/** The number of ticks the agent is already dead.*/
	private int dead;
	
	/** True if the agent is vaccinated, false otherwise.*/
	private boolean vaccinatedDFT1;
	private boolean vaccinatedDFT2;
	
	/** This is the center around which the devil will move.*/
	private GridPoint home;
	
	/** The current state of the agent. Should only be set via StateManager.*/
	private AbstractState currentState;
	
	/** Interaction partner for pairwise interactions. */
	private TasmanianDevil interactionPartner;
	
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
		//System.out.println("Step: " + currentState.getClass().getName());
		if(currentState == null)
			throw new Exception("There is no state defined for this agent! Step cannot be evaluated");
		currentState.step(this);
		currentState = StateManager.getState(this);
	}

	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
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

	public void incrementSickDFT1(int ticks) {
		sickDFT1 += ticks;
	}

	public void incrementSickDFT2(int ticks) {
		sickDFT2 += ticks;
	}
	
	public int getDead() {
		return dead;
	}

	public void incrementDead(int ticks) {
		dead += ticks;
	}

	public boolean isVaccinatedDFT1() {
		return vaccinatedDFT1;
	}
	
	public boolean isVaccinatedDFT2() {
		return vaccinatedDFT2;
	}

	public void vaccinate() {
		vaccinatedDFT1 = true;
		vaccinatedDFT2 = true;
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
	
	public Grid<Object> getGrid()
	{
		return this.grid;
	}
	
	public ContinuousSpace<Object> getSpace()
	{
		return this.space;
	}
	
	public boolean isFemale() {
		return this.currentState.isFemaleState();
	}
	
	public boolean isInfectiousDFT1() {
		if(TickParser.getCompleteDaysFromTicks(this.getSickDFT1()) > 180)
		{
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isInfectiousDFT2()
	{
		if(TickParser.getCompleteDaysFromTicks(this.getSickDFT2()) > 180)
		{
			return true;
		} else {
			return false;
		}
	}
	
	public void setInteractionPartner(TasmanianDevil partner) {
		this.interactionPartner= partner;
	}
	
	public TasmanianDevil getInteractionPartner() {
		return interactionPartner;
	}
	
	public int sickCounterHelper() {
		if (currentState.isSickState()) {
			return 1;
		}
		return 0;
	}

	public int healthyCounterHelper() {
		if (!currentState.isSickState()) {
			return 1;
		}
		return 0;
	}

	public int deadCounterHelper() {
		if (dead > 0) {
			return 1;
		}
		return 0;
	}
}
