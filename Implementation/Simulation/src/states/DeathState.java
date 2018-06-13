package states;

import java.awt.Color;

import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VShape;
import saf.v3d.scene.VSpatial;
import simulation.TasmanianDevil;

public class DeathState extends AbstractState {

	@Override
	public Color getColor() {
		return Color.BLACK;
	}
	
	@Override
	public VSpatial getSpatialVisualisation(ShapeFactory2D shapeFactory)
	{
		VShape circle = shapeFactory.createCircle(10, 10);
		circle.setBorderColor(Color.YELLOW);
		circle.setBorderStrokeSize(5);
		return circle;
	}
	
	@Override
	public boolean isSickState() {
		return true;
	}

	@Override
	public boolean isFemaleState() {
		return false;
	}

	@Override
	public void step(TasmanianDevil devil) {
		
	}

}
