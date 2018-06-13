package simulation;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;

public class Environment {

	// singleton
	private static Environment instance;
	
	private Environment() 
	{
		final Parameters params = RunEnvironment.getInstance().getParameters();
		this.infectionRate = params.getDouble("infectionRate");
		this.infectionMatingFactor = params.getDouble("infectionMatingFactor");
		this.femaleRatio = params.getDouble("femaleRatio");
		this.interactionRadius = params.getInteger("interactionRadius");
		this.mapSizeX = params.getInteger("mapSizeX");
		this.mapSizeY = params.getInteger("mapSizeY");
		this.populationSize = params.getInteger("populationSize");
		this.steprange = params.getInteger("stepRange");
		this.ticksPerYear = params.getInteger("tickValue");
		this.matingSeasonStartDay = params.getInteger("matingSeasonStartDay");
		this.matingSeasonEndDay = params.getInteger("matingSeasonEndDay");
		this.initialSickFemale= params.getDouble("initialSickFemale");
		this.initialSickMale= params.getDouble("initialSickMale");
		this.vaccinatedInfectionRate = params.getDouble("vaccinatedInfectionRate");
		this.naturalDeathRate = params.getDouble("naturalDeathRate");
	}


	public static Environment getInstance() {
		if (instance == null) {
			instance = new Environment ();
		    }
		    return instance;
	}
	
	public static void reset() {
		instance= new Environment();
	}

	private int mapSizeX;
	private int mapSizeY;
	private int populationSize;
	private int ticksPerYear;
	private double femaleRatio;
	private double initialSickFemale;
	private double initialSickMale;
	private int matingSeasonStartDay;
	private int matingSeasonEndDay;

	private int steprange;
	private int interactionRadius;

	// probability of getting infected during interaction
	private double infectionRate;
	// probability of getting infected male - female (only during mating time)
	private double infectionMatingFactor;
	private double vaccinatedInfectionRate;
	private double naturalDeathRate;
	
	public int getMapSizeX() {
		return mapSizeX;
	}

	public int getMapSizeY() {
		return mapSizeY;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public int getTicksPerYear() {
		return ticksPerYear;
	}

	public double getFemaleRatio() {
		return femaleRatio;
	}

	public int getSteprange() {
		return steprange;
	}

	public int getInteractionRadius() {
		return interactionRadius;
	}

	public double getInfectionRate() {
		return infectionRate;
	}

	public double getInfectionMatingFactor() {
		return infectionMatingFactor;
	}
	
	public int getMatingSeasonStartDay() {
		return matingSeasonStartDay;
	}
	
	public int getMatingSeasonEndDay() {
		return matingSeasonEndDay;
	}
	
	public double getInitialSickFemale() {
		return initialSickFemale;
	}

	public double getInitialSickMale() {
		return initialSickMale;
	}
	
	public double getVaccinatedInfectionRate() {
		return vaccinatedInfectionRate;
	}
	
	public double getNaturalDeathRate() {
		return naturalDeathRate;
	}

}
