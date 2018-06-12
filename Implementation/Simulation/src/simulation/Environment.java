package simulation;

public class Environment {
	private int mapSizeX;
	private int mapSizeY;
	private int populationSize;
	private double femaleRatio;
	
	private int steprange;
	private int interactionRadius;
	
	// probability of getting infected during interaction 
	private double infectionRate;
	// probability of getting infected male - female (only during mating time)
	private double infectionRateMating;
	
	public int getMapSizeX() {
		return mapSizeX;
	}
	public void setMapSizeX(int mapSizeX) {
		this.mapSizeX = mapSizeX;
	}
	public int getMapSizeY() {
		return mapSizeY;
	}
	public void setMapSizeY(int mapSizeY) {
		this.mapSizeY = mapSizeY;
	}
	public int getPopulationSize() {
		return populationSize;
	}
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}
	public double getFemaleRatio() {
		return femaleRatio;
	}
	public void setFemaleRatio(double femaleRatio) {
		this.femaleRatio = femaleRatio;
	}
	public int getSteprange() {
		return steprange;
	}
	public void setSteprange(int steprange) {
		this.steprange = steprange;
	}
	public int getInteractionRadius() {
		return interactionRadius;
	}
	public void setInteractionRadius(int interactionRadius) {
		this.interactionRadius = interactionRadius;
	}
	public double getInfectionRate() {
		return infectionRate;
	}
	public void setInfectionRate(double infectionRate) {
		this.infectionRate = infectionRate;
	}
	public double getInfectionRateMating() {
		return infectionRateMating;
	}
	public void setInfectionRateMating(double infectionRateMating) {
		this.infectionRateMating = infectionRateMating;
	}

}
