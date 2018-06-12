package states;

import java.awt.Color;
import java.util.List;

import controls.TickParser;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VShape;
import saf.v3d.scene.VSpatial;
import simulation.TasmanianDevil;

public class FemaleSickState extends AbstractState {

	@Override
	public Color getColor() {
		return Color.RED;
	}
	
	@Override
	public VSpatial getSpatialVisualisation(ShapeFactory2D shapeFactory)
	{
		VShape circle = shapeFactory.createCircle(10, 10);
		circle.setBorderColor(Color.BLACK);
		circle.setBorderStrokeSize(5);
		return circle;
	}
	
	@Override
	public void step(TasmanianDevil devil) {
		randomMove(devil);
		doMainStep(devil);
//		Grid<Object> grid = devil.getGrid();
//		GridPoint pt = grid.getLocation(devil);
//		GridCellNgh<TasmanianDevil> nghCreator = new GridCellNgh<TasmanianDevil>(grid, pt,TasmanianDevil.class , 4, 4);
//		List<GridCell<TasmanianDevil>> gridCells = nghCreator.getNeighborhood(true); SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
//		GridPoint pointWithMostMales = null;
//		int maxCount = -1;
//		for(GridCell<TasmanianDevil> cell : gridCells) {
//			int numOfMales=0;
//			for(TasmanianDevil tmpDevil: cell.items())
//			{
//				if(!tmpDevil.getCurrentState().isFemaleState())
//					numOfMales++;
//			}
//			if(numOfMales > maxCount) {
//				pointWithMostMales = cell.getPoint();
//				maxCount = cell.size();
//			}
//		}		
//		
//		moveTowards(devil, pointWithMostMales, 0);
//		doMainStep(devil);
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
}
