package simulation;

import controls.StateManager;
import controls.TickParser;
import repast.simphony.context.Context;
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
	
	private Context<Object> context;
	
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
	
	private boolean interacted;
	
	private int interactionCounter;
	
	public TasmanianDevil(ContinuousSpace<Object> space, Grid<Object> grid, AbstractState startState, Context<Object> context)
	{
		this(space, grid, false, false, startState, context);
	}
	
	public TasmanianDevil(ContinuousSpace<Object> space, Grid<Object> grid, boolean vaccinatedDFT1,  boolean vaccinatedDFT2, AbstractState startState, Context<Object> context) {
		this.context= context;
		this.space = space;
		this.grid = grid;
		this.age = 0;
		this.sickDFT1 = 0;
		this.sickDFT2 = 0;
		if(vaccinatedDFT1)
			this.vaccinateDFT1();
		if(vaccinatedDFT2)
			this.vaccinateDFT2();
		this.currentState = startState;
		this.interacted=false;
		this.interactionCounter=0;
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
	
	public TasmanianDevil getNeighborInRange() {
		TasmanianDevil nearest= null;
		double dist=-1; 
		for (Object o : context.getObjects(TasmanianDevil.class)) {
			if(o.equals(this)||((TasmanianDevil)o).hasInteracted())
				continue;
			if(dist ==-1||grid.getDistance(grid.getLocation(o), grid.getLocation(this))<dist) {
			 nearest= (TasmanianDevil)o;
			 dist= grid.getDistance(grid.getLocation(o), grid.getLocation(this));
			}
		}
		if(dist<Environment.getInstance().getInteractionRadius()) {
			return nearest;
		}
		return null;
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

	public void vaccinateDFT1() {
		vaccinatedDFT1 = true;
	}
	
	public void vaccinateDFT2()
	{
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
		return this.interactionPartner;
	}
	
	public void setInteracted(boolean interacted) {
		if (interacted)
			interactionCounter++;
		this.interacted = interacted;
	}
	
	public boolean hasInteracted() {
		return this.interacted;
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

	public int infectedNotInfectious() {
		if ((sickDFT1 > 0 || sickDFT2 > 0) && (!isInfectiousDFT1() && !isInfectiousDFT2())) {
			return 1;
		}
		return 0;
	}
	
	public int infectedInfectious() {
		if (isInfectiousDFT1() || isInfectiousDFT2()) {
			return 1;
		}
		return 0;
	}

	public int sickNotDeadCounterHelper() {
		if (dead <= 0 && currentState.isSickState()) {
			return 1;
		}
		return 0;
	}
	
	public int femaleCounter() {
		if(currentState.isFemaleState()) {
			return 1;
		}
		return 0;
	}
	public int maleCounter() {
		if(!currentState.isFemaleState()) {
			return 1;
		}
		return 0;
	}
	
	public int femaleSickCounter() {
		if(currentState.isFemaleState()&&currentState.isSickState()) {
			return 1;
		}
		return 0;
	}
	
	public int maleSickCounter() {
		if(!currentState.isFemaleState()&&currentState.isSickState()) {
			return 1;
		}
		return 0;
	}
	
	public int femaleHealthyCounter() {
		if(currentState.isFemaleState()&&!currentState.isSickState()) {
			return 1;
		}
		return 0;
	}
	
	public int maleHealthyCounter() {
		if(!currentState.isFemaleState()&&!currentState.isSickState()) {
			return 1;
		}
		return 0;
	}
	
	public int interactionCounter() {
		return interactionCounter;
	}
	
	public int vaccinationCounter1() {
		if(isVaccinatedDFT1()) {
			return 1;
		}
		return 0;
	}
	
	public int vaccinationCounter2() {
		if(isVaccinatedDFT2()) {
			return 1;
		}
		return 0;
	}
}
