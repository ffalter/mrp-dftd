package states;

import java.awt.Color;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;
import simulation.Environment;
import simulation.TasmanianDevil;

/**
 * As part of the StrategyPattern this is used to
 * - define the interface for a state
 * - provide some useful functions for all states
 * @author dludwig
 *
 */
public abstract class AbstractState {
	
//VISUALISATION METHODS
	/**
	 * This method can be overridden to change the shape of visualisation.
	 * default: circle
	 * @param shapeFactory can be used to create shapes
	 */
	public VSpatial getSpatialVisualisation(ShapeFactory2D shapeFactory)
	{
		return shapeFactory.createCircle(10, 10);
	}
	
	/**
	 * This method can be overridden to change the colour of visualisation.
	 * default: black
	 */
	public Color getColor() {
		return Color.BLACK;
	}
	
//METHODS THAT HAVE TO BE IMPLEMENTED IN THE CONCRETE STATE
	/**
	 * This method has to be implemented, to determine if devils in this state are sick
	 * @return  true 	if devils in this state are sick
	 * 			false	if devils in this state are healthy
	 */
	abstract public boolean isSickState();

	/**
	 * This method has to be implemented, to determine if devils in this state are female
	 * @return  true 	if devils in this state are female
	 * 			false	if devils in this state are male
	 */
	abstract public boolean isFemaleState();

	/**
	 * This method has to be implemented to describe the behaviour of the agent in this state.
	 * @param devil should be modified during the step
	 */
	abstract public void step(TasmanianDevil devil);
	
	
//METHODS THAT HAVE USEFUL FUNCTIONALITY FOR ALL STATES
	/**
	 * This method has to be called in the beginning of each step implementation.
	 * @param devil will be modified during the step
	 */
	public void doMainStep(TasmanianDevil devil)
	{
		//make devil one tick older
		devil.incrementAge(1);
		
		//if devil is sick, increment ticks of sickness
		if(isSickState())
			devil.incrementSick(1);		
	}
	
	/**
	 * let the devil move to a random position within the steprange of its home. 
	 * @param devil
	 */
	public void randomMove(TasmanianDevil devil) {
		Grid<Object> grid = devil.getGrid();
		ContinuousSpace<Object> space = devil.getSpace();
		GridPoint home = devil.getHome();
		space.moveTo(devil, home.getX(), home.getY());
		// select random point within range
		double angle = RandomHelper.nextDoubleFromTo(0, 2*Math.PI);
		//TODO maybe add something like gaussian distribution for the final distance from the home
		space.moveByVector(devil, RandomHelper.nextDoubleFromTo(0, Environment.getInstance().getSteprange()), angle,0);
		NdPoint myPoint = space.getLocation(devil);
		grid.moveTo(devil, (int) myPoint.getX(), (int) myPoint.getY());

	}
	
	public void moveTowards(TasmanianDevil devil, GridPoint pt, int maxCount) {
		Grid<Object> grid = devil.getGrid();
		ContinuousSpace<Object> space = devil.getSpace();
		if(!pt.equals(grid.getLocation(devil) )) {
			NdPoint myPoint = space.getLocation(devil);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY()); double angle = SpatialMath.calcAngleFor2DMovement(space,
			myPoint , otherPoint );
			space.moveByVector(devil, 0.5, angle, 0);
			myPoint = space.getLocation(devil);
			grid.moveTo(devil, (int)myPoint.getX(), (int)myPoint.getY());
		}
		/*
		if(pt.equals(grid.getLocation(this)) && maxCount != 0) {
			Random rand = new Random();
			int x = rand.nextInt(10);
			if(x >5) {
				this.isSickState() = true;
			}
		}*/
	}

	
}
