package controls;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.scene.VSpatial;
import simulation.TasmanianDevil;

/**
 * simulationVisualizer makes it possible to vary the visualisation for each
 * state.
 * 
 * @author dludwig
 *
 */
public class simulationVisualizer extends DefaultStyleOGL2D {

	/**
	 * This method redirects the visualisation to the different states.
	 */
	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		return ((TasmanianDevil) agent).getCurrentState().getSpatialVisualisation(shapeFactory);
	}

	@Override
	public Color getColor(Object agent) {
		return ((TasmanianDevil) agent).getCurrentState().getColor();
	}
}
