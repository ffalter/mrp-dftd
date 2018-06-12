package simulation;

import java.awt.Color;
import java.util.List;

import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;

public class FemaleSickState extends AbstractState {

	@Override
	public Color getColor() {
		return Color.YELLOW;
	}
	
	@Override
	public void step(TasmanianDevil devil) {
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
		return true;
	}

	@Override
	public boolean isFemaleState() {
		return true;
	}


	public boolean isInfectiousDFT1(TasmanianDevil devil)
	{
		//TODO getSickDFT1 returns sick - parse to days
		if(devil.getSickDFT1() > 180)
		{
			return true;
		} else {
			return false;
		}
	}

	public boolean isInfectiousDFT2(TasmanianDevil devil)
	{
		//TODO getSickDFT2 returns sick - parse to days
		if(devil.getSickDFT2() > 180)
		{
			return true;
		} else {
			return false;
		}
	}
}
