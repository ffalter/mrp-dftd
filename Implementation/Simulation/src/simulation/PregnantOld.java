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
import repast.simphony.space.projection.Projection;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;


public class PregnantOld {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	int birth = 0;
	boolean done = false;
	
	public PregnantOld(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}
	@ScheduledMethod(start = 1, interval = 1) // Call every iteration of the simulation
	public void run() {
		GridPoint pt = grid.getLocation(this);
		GridCellNgh<MaleOld> nghCreator = new GridCellNgh<MaleOld>(grid, pt,MaleOld.class , 4, 4);
		List<GridCell<MaleOld>> gridCells = nghCreator.getNeighborhood(true); 
		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
		GridPoint pointWithMostSafe = null; int minCount = Integer.MAX_VALUE;
		for(GridCell<MaleOld> cell: gridCells) {
			if(cell.size() < minCount) {
				pointWithMostSafe = cell.getPoint();
				minCount = cell.size();
			}
		}
		
		
		moveTowards(pointWithMostSafe);
		birth++;
		if(birth>20) {
			birth();
		}
		
	
	}
	
	private void moveTowards(GridPoint pt) {
		
		if(!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint,
			otherPoint);
			space.moveByVector(this, 0.5, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
		}
		
	}
	public void birth() {
		GridPoint pt = grid.getLocation(this);
		List<Object> pregs = new ArrayList<Object>();
		Object obj = this;
		pregs.add(obj);
		if(pregs.size() > 0) {
			int index = RandomHelper.nextIntFromTo(0, pregs.size()-1);
			obj = pregs.get(index);
			NdPoint spacePt = space.getLocation(obj);
			Context<Object> context = ContextUtils.getContext(obj);
			context.remove(obj);
			Random rand = new Random();
			int x = rand.nextInt(10);
			if(x>=3) {
				FemaleOld female = new FemaleOld(space, grid);
				context.add(female);
				space.moveTo(female, spacePt.getX(),spacePt.getY());
				grid.moveTo(female, pt.getX(),pt.getY());
			}
			else {
				MaleOld male = new MaleOld(space, grid);
				context.add(male);
				space.moveTo(male, spacePt.getX(),spacePt.getY());
				grid.moveTo(male, pt.getX(),pt.getY());
			}
			
			FemaleOld fem = new FemaleOld(space, grid);
			context.add(fem);
			space.moveTo(fem, spacePt.getX(),spacePt.getY());
			grid.moveTo(fem, pt.getX(),pt.getY());

		}
	}
	
}
