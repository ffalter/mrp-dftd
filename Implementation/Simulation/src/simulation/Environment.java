package simulation;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;

public class Environment {

	// singleton
	private static Environment instance;
	
	private Environment() 
	{
		final Parameters params = RunEnvironment.getInstance().getParameters();
		this.infectionRateDFT1 = params.getDouble("infectionRateDFT1");
		this.infectionRateDFT2 = params.getDouble("infectionRateDFT2");
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
		this.vaccinatedInfectionRateDFT1 = params.getDouble("vaccinatedInfectionRateDFT1");
		this.vaccinatedInfectionRateDFT2 = params.getDouble("vaccinatedInfectionRateDFT2");
		this.naturalDeathRate = params.getDouble("naturalDeathRate");
		this.deadRemove = params.getInteger("deadRemove");
		this.endAfterYears = params.getInteger("endAfterYears");
		this.minPopulationSize = params.getInteger("minPopulationSize");
		this.numAddVaccinatedDFT1PerYear = params.getInteger("numAddVaccinatedDFT1PerYear");
		this.numAddVaccinatedDFT2PerYear = params.getInteger("numAddVaccinatedDFT2PerYear");
		this.resistanceGainRate = params.getDouble("resistanceGainRate");
		this.resistanceFactor = params.getDouble("resistanceFactor");
		this.initDFTD1 = params.getDouble("initDftd1infected");
		this.initDFTD2 = params.getDouble("initDftd2infected");
		this.addHealthy = params.getInteger("addHealthy");
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
	private double infectionRateDFT1;
	private double infectionRateDFT2;
	// probability of getting infected male - female (only during mating time)
	private double infectionMatingFactor;
	private double vaccinatedInfectionRateDFT1;
	private double vaccinatedInfectionRateDFT2;
	private double naturalDeathRate;
	//remove agent after x days
	private int deadRemove;
	//end run after x years
	private int endAfterYears;
	private int minPopulationSize;
	
	private int numAddVaccinatedDFT1PerYear;
	private int numAddVaccinatedDFT2PerYear;
	
	private double resistanceGainRate;
	private double resistanceFactor;
	
	private double initDFTD1;
	private double initDFTD2;
	
	private int addHealthy;
	
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

	public double getInfectionRateDFT1() {
		return infectionRateDFT1;
	}
	
	public double getInfectionRateDFT2() {
		return infectionRateDFT2;
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
	
	public double getVaccinatedInfectionRateDFT1() {
		return vaccinatedInfectionRateDFT1;
	}
	
	public double getVaccinatedInfectionRateDFT2() {
		return vaccinatedInfectionRateDFT2;
	}
	
	public double getNaturalDeathRate() {
		return naturalDeathRate;
	}

	public int getDeadRemove() {
		return deadRemove;
	}
	
	public int getEndAfterYears() {
		return endAfterYears;
	}
	
	public int getMinPopulationSize(){
		return minPopulationSize;
	}
	
	public int getNumAddVaccinatedDFT1PerYear() {
		return numAddVaccinatedDFT1PerYear;
	}
	
	public int getNumAddVaccinatedDFT2PerYear() {
		return numAddVaccinatedDFT2PerYear;
	}
	
	public double getResistanceGainRate() {
		return resistanceGainRate;
	}
	
	public double getResistanceFactor() {
		return resistanceFactor;
	}
	
	public double getInitDftd1Infected() {
		return initDFTD1;
	}
	
	public double getInitDftd2Infected() {
		return initDFTD2;
	}
	
	public int getAddHealthy(){
		return addHealthy;
	}
}
