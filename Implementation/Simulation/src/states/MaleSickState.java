package states;

import java.awt.Color;
import java.util.List;

import controls.TickParser;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VShape;
import saf.v3d.scene.VSpatial;
import simulation.TasmanianDevil;

public class MaleSickState extends AbstractState {
	
	@Override
	public Color getColor() {
		return Color.BLUE;
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
		doMainStep(devil);
		randomMove(devil);
//		GridPoint pt = devil.getGrid().getLocation(devil);
//		
//		GridCellNgh <TasmanianDevil> nghCreator = new GridCellNgh<TasmanianDevil>(devil.getGrid(),pt,TasmanianDevil.class, 4, 4);
//		List<GridCell<TasmanianDevil>> gridCells = nghCreator.getNeighborhood(true);
//		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
//		
//		GridPoint pointWithMostFemales = null;
//		int maxCount = -1;
//		for(GridCell<TasmanianDevil> cell : gridCells) {
//			int numOfFemales=0;
//			for(TasmanianDevil tmpDevil: cell.items())
//			{
//				if(tmpDevil.getCurrentState().isFemaleState())
//					numOfFemales++;
//			}
//			if(numOfFemales > maxCount) {
//				pointWithMostFemales = cell.getPoint();
//				maxCount = cell.size();
//			}
//		}
//		moveTowards(devil, pointWithMostFemales, 0);
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
}
