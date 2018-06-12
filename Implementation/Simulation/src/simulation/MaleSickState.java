package simulation;

import java.awt.Color;
import java.util.List;

import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;

public class MaleSickState extends AbstractState {
	
	@Override
	public Color getColor() {
		return Color.RED;
	}

	@Override
	public void step(TasmanianDevil devil) {
		GridPoint pt = devil.grid.getLocation(devil);
		
		GridCellNgh <TasmanianDevil> nghCreator = new GridCellNgh<TasmanianDevil>(devil.grid,pt,TasmanianDevil.class, 4, 4);
		List<GridCell<TasmanianDevil>> gridCells = nghCreator.getNeighborhood(true);
		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
		
		GridPoint pointWithMostFemales = null;
		int maxCount = -1;
		for(GridCell<TasmanianDevil> cell : gridCells) {
			int numOfFemales=0;
			for(TasmanianDevil tmpDevil: cell.items())
			{
				if(tmpDevil.getCurrentState().isFemaleState())
					numOfFemales++;
			}
			if(numOfFemales > maxCount) {
				pointWithMostFemales = cell.getPoint();
				maxCount = cell.size();
			}
		}
		moveTowards(devil, pointWithMostFemales, 0);
	}

	@Override
	public boolean isSickState() {
		return true;
	}
	
	@Override
	public boolean isFemaleState()
	{
		return false;
	}
	
	public boolean isInfectious(TasmanianDevil devil)
	{
		//TODO getSick returns sick - parse to days
		if(devil.getSick() > 180)
		{
			return true;
		} else {
			return false;
		}
	}
}
