package simulation;

import java.awt.Color;
import java.util.List;

import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;

public class FemaleOld extends AbstractState{
	@Override
	public Color getColor() {
		return Color.PINK;
	}
	
	@Override
	public void step(TasmanianDevil devil)  {
		Grid<Object> grid = devil.grid;
		GridPoint pt = grid.getLocation(devil);
		GridCellNgh<TasmanianDevil> nghCreator = new GridCellNgh<TasmanianDevil>(grid, pt,TasmanianDevil.class , 4, 4);
		List<GridCell<TasmanianDevil>> gridCells = nghCreator.getNeighborhood(true); SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
		GridPoint pointWithMostMales = null;
		int maxCount = -1;
		for(GridCell<TasmanianDevil> cell : gridCells) {
			int numOfMales=0;
			for(TasmanianDevil tmpDevil: cell.items())
			{
				if(!tmpDevil.getCurrentState().isFemaleState())
					numOfMales++;
			}
			if(numOfMales > maxCount) {
				pointWithMostMales = cell.getPoint();
				maxCount = cell.size();
			}
		}		
		
		moveTowards(devil, pointWithMostMales, 0);
		doMainStep(devil);
		// this should be implemented in another state like: femalePregnantState
		//if(pregnant) {
		//	pregnant();
		//}
		
	}
	
	@Override
	public boolean isSickState() {
		return false;
	}
	
	@Override
	public boolean isFemaleState()
	{
		return false;
	}

	/*
	
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
	}*/
}
