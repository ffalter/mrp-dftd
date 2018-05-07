package simulation;


import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;
import repast.simphony.context.Context;

import java.util.ArrayList;
import java.util.List;

public class Male {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private boolean moved;
	
	public Male(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}
	
	@ScheduledMethod(start = 1, interval = 1) // Call every iteration of the simulation
	public void step() {
		GridPoint pt = grid.getLocation(this);
		
		GridCellNgh <Female> nghCreator = new GridCellNgh<Female>(grid,pt,Female.class,4,4);
		List<GridCell<Female>> gridCells = nghCreator.getNeighborhood(true);
		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
		
		GridPoint pointWithMostFemales = null;
		int maxCount = -1;
		for(GridCell<Female> cell : gridCells) {
			if(cell.size() > maxCount) {
				pointWithMostFemales = cell.getPoint();
				maxCount = cell.size();
			}
		}
		moveTowards(pointWithMostFemales);
	}
	
	public void moveTowards(GridPoint pt) {
		
		if(!pt.equals(grid.getLocation(this) )) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY()); double angle = SpatialMath.calcAngleFor2DMovement(space,
			myPoint , otherPoint );
			space.moveByVector(this, 0.5, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
		}
	}
}

