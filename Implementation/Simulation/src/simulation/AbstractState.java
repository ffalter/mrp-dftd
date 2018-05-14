package simulation;

import java.awt.Color;

import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

/**
 * As part of the StrategyPattern this is used to
 * - define the interface for a state
 * - provide some useful functions for all states
 * @author dludwig
 *
 */
public abstract class AbstractState {
	
	/**
	 * This method can be overridden to change the shape of visualisation.
	 * default: circle
	 * @param shapeFactory can be used to create shapes
	 */
	public VSpatial getSpatialVisualisation(ShapeFactory2D shapeFactory)
	{
		return shapeFactory.createCircle(10, 10);
	}
	
	/**
	 * This method has to be implemented to describe the behaviour of the agent in this state.
	 * @param devil
	 */
	abstract public void step(TasmanianDevil devil);

	/**
	 * This method can be overridden to change the colour of visualisation.
	 * default: black
	 */
	public Color getColor() {
		return Color.BLACK;
	}

}
