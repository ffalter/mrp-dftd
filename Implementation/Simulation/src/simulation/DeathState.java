package simulation;

import java.awt.Color;

public class DeathState extends AbstractState {

	@Override
	public Color getColor() {
		return Color.BLACK;
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
