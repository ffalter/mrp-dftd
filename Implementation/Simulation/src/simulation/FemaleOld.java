package simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

public class FemaleOld {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private boolean pregnant = false;
	
	public FemaleOld(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}
	
	@ScheduledMethod(start = 1, interval = 1) // Call every iteration of the simulation
	
	public void run() {
		System.out.println("Step OLD FEMALE.");
		GridPoint pt = grid.getLocation(this);
		GridCellNgh<MaleOld> nghCreator = new GridCellNgh<MaleOld>(grid, pt,MaleOld.class , 1, 1);
		List<GridCell<MaleOld>> gridCells = nghCreator.getNeighborhood(true); SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
		GridPoint pointWithMostMales = null; int maxCount = -1;
		for(GridCell<MaleOld> cell: gridCells) {
			if(cell.size() > maxCount) {
				pointWithMostMales = cell.getPoint();
				maxCount = cell.size();
			}
		}
		
		
		
		
		moveTowards(pointWithMostMales, maxCount);
		if(pregnant) {
			pregnant();
		}
	}

	private void moveTowards(GridPoint pt, int maxCount) {
		if(!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint,
			otherPoint);
			space.moveByVector(this, 0.5, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
		}
		if(pt.equals(grid.getLocation(this)) && maxCount != 0) {
			Random rand = new Random();
			int x = rand.nextInt(10);
			if(x >8) {
				pregnant = true;
			}
		}
	}
	
	public void pregnant() {
		GridPoint pt = grid.getLocation(this);
		List<Object> females = new ArrayList<Object>();
		for(Object obj : grid.getObjectsAt(pt.getX(),pt.getY())) {
			if(obj instanceof FemaleOld) {
				females.add(obj);
			}
		}
		if(females.size() > 0) {
			int index = RandomHelper.nextIntFromTo(0, females.size()-1);
			Object obj = females.get(index);
			NdPoint spacePt = space.getLocation(obj);
			Context<Object> context = ContextUtils.getContext(obj);
			context.remove(obj);
			PregnantOld preg = new PregnantOld(space, grid);
			context.add(preg);
			space.moveTo(preg, spacePt.getX(),spacePt.getY());
			grid.moveTo(preg, pt.getX(),pt.getY());

		}
	}
}
