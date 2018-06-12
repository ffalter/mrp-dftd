package simulation;

public class Environment {

	// singleton
	private static Environment instance = new Environment();
	
	private Environment() {}

	public static Environment getInstance() {
		if (Environment.instance == null) {
			Environment.instance = new Environment ();
		    }
		    return Environment.instance;
	}

	public int mapSizeX;
	public int mapSizeY;
	public int populationSize;
	public double femaleRatio;

	public int steprange;
	public int interactionRadius;

	// probability of getting infected during interaction
	public double infectionRate;
	// probability of getting infected male - female (only during mating time)
	public double infectionRateMating;

}
