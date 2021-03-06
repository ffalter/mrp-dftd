package states;

import java.awt.Color;
import java.util.List;

import cern.jet.random.Normal;
import controls.InteractionManager;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
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
 * As part of the StrategyPattern this is used to - define the interface for a
 * state - provide some useful functions for all states
 * 
 * @author dludwig
 *
 */
public abstract class AbstractState {

	// VISUALISATION METHODS
	/**
	 * This method can be overridden to change the shape of visualisation. default:
	 * circle
	 * 
	 * @param shapeFactory
	 *            can be used to create shapes
	 */
	public VSpatial getSpatialVisualisation(ShapeFactory2D shapeFactory) {
		return shapeFactory.createCircle(10, 10);
	}

	/**
	 * This method can be overridden to change the colour of visualisation. default:
	 * black
	 */
	public Color getColor() {
		return Color.BLACK;
	}

	// METHODS THAT HAVE TO BE IMPLEMENTED IN THE CONCRETE STATE
	/**
	 * This method has to be implemented, to determine if devils in this state are
	 * sick
	 * 
	 * @return true if devils in this state are sick false if devils in this state
	 *         are healthy
	 */
	abstract public boolean isSickState();

	/**
	 * This method has to be implemented, to determine if devils in this state are
	 * female
	 * 
	 * @return true if devils in this state are female false if devils in this state
	 *         are male
	 */
	abstract public boolean isFemaleState();

	/**
	 * This method has to be implemented to describe the behaviour of the agent in
	 * this state.
	 * 
	 * @param devil
	 *            should be modified during the step
	 */
	abstract public void step(TasmanianDevil devil);

	// METHODS THAT HAVE USEFUL FUNCTIONALITY FOR ALL STATES
	/**
	 * This method has to be called in the beginning of each step implementation.
	 * 
	 * @param devil
	 *            will be modified during the step
	 */
	public void doMainStep(TasmanianDevil devil) {
		TasmanianDevil nn = devil.getInteractionPartner();
		if (nn == null) {
			nn = getClosestDevil(devil);
		}

		// make devil one tick older
		devil.incrementAge(1);

		if (devil.getSickDFT1() > 0) {
			devil.incrementSickDFT1(1);
		}
		if (devil.getSickDFT2() > 0) {
			devil.incrementSickDFT2(1);
		}

		if (nn != null) {
			nn.setInteractionPartner(devil);
			double[] infectionProb = InteractionManager.getInstance().getInfectionProbability(devil, nn);
			// consider possible resistance for DFT1
			infectionProb[0] *= devil.getResistanceFactor();
			if (devil.getSickDFT1() == 0 && RandomHelper.nextDoubleFromTo(0, 1) < infectionProb[0]) {
				devil.incrementSickDFT1(1);
			}
			if (devil.getSickDFT2() == 0 && RandomHelper.nextDoubleFromTo(0, 1) < infectionProb[1]) {
				devil.incrementSickDFT2(1);
			}
			devil.setInteractionPartner(null);
			devil.setInteracted(true);
			if (nn.getDead() > 0) {
				// dead devil should be removed
				nn.incrementDead(Environment.getInstance().getDeadRemove());
			}
		}
	}

	/**
	 * let the devil move to a random position within the steprange of its home.
	 * 
	 * @param devil
	 */
	public void randomMove(TasmanianDevil devil) {
		Grid<Object> grid = devil.getGrid();
		ContinuousSpace<Object> space = devil.getSpace();
		GridPoint home = devil.getHome();
		space.moveTo(devil, home.getX(), home.getY());
		// select random point within range
		double angle = RandomHelper.nextDoubleFromTo(0, 2 * Math.PI);

		int steprange = Environment.getInstance().getSteprange();
		// should not move as far as without interaction
		if (devil.hasInteracted()) {
			steprange /= 3;
			devil.setInteracted(false);
		}
		Normal distribution = RandomHelper.createNormal(steprange * 0.5, steprange * 0.3);
		space.moveByVector(devil, distribution.nextInt(), angle, 0);
		NdPoint myPoint = space.getLocation(devil);
		grid.moveTo(devil, (int) myPoint.getX(), (int) myPoint.getY());

	}

	public void moveTowards(TasmanianDevil devil, GridPoint pt, int maxCount) {
		Grid<Object> grid = devil.getGrid();
		ContinuousSpace<Object> space = devil.getSpace();
		if (!pt.equals(grid.getLocation(devil))) {
			NdPoint myPoint = space.getLocation(devil);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
			space.moveByVector(devil, 0.5, angle, 0);
			myPoint = space.getLocation(devil);
			grid.moveTo(devil, (int) myPoint.getX(), (int) myPoint.getY());
		}
		/*
		 * if(pt.equals(grid.getLocation(this)) && maxCount != 0) { Random rand = new
		 * Random(); int x = rand.nextInt(10); if(x >5) { this.isSickState() = true; } }
		 */
	}

	/**
	 * search for the nearest neighbor within the interaction range.
	 * 
	 * @return
	 */
	public TasmanianDevil getClosestDevil(TasmanianDevil devil) {
		return devil.getNeighborInRange();
		// GridPoint currentPosition = devil.getGrid().getLocation(devil);
		// GridCellNgh<TasmanianDevil> nghCreator = new
		// GridCellNgh<TasmanianDevil>(devil.getGrid(), currentPosition,
		// TasmanianDevil.class,
		// Environment.getInstance().getInteractionRadius(),Environment.getInstance().getInteractionRadius());
		// List<GridCell<TasmanianDevil>> gridCells = nghCreator.getNeighborhood(false);
		// if (!gridCells.isEmpty()) {
		// double minDist= -1;
		// GridCell<TasmanianDevil> nearest=null;
		// for(GridCell<TasmanianDevil> cell : gridCells) {
		// double dist= devil.getGrid().getDistance(currentPosition, cell.getPoint());
		// if(dist <Environment.getInstance().getInteractionRadius() && ( minDist ==-1||
		// minDist>dist )) {
		// minDist = dist;
		// nearest = cell;
		// }
		// }
		// if (nearest != null) {
		// for (TasmanianDevil t : nearest.items()) {
		// return t;
		// }
		// }
		// }
		// return null;
	}
}
