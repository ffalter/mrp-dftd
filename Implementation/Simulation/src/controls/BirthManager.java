package controls;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import simulation.Environment;
import simulation.TasmanianDevil;
import states.FemaleHealthyState;
import states.FemaleSickState;
import states.MaleHealthyState;
import states.MaleSickState;

import java.util.Random;

import org.apache.commons.math3.geometry.Space;

public class BirthManager {

	/** create random numbers */
	private Random random = new Random();

	/** Start of the birth season in ticks, from beginning of the simulation. */
	private int startOfBirthSeason;

	/** Duration of a birth season. */
	private int durationOfBirthSeason;

	/** Percentage of how many Females will give birth */
	private double percentageOfBirth;

	/** Current context of the animals */
	private Context<Object> context;
	
	/** currently used grid */
	private Grid<Object> grid;
	
	/** currently used space */
	private ContinuousSpace<Object> space;

	/** Age which is needed to get offspring. */
	private final int canGetOffspringAge = 2 * Environment.getInstance().getTicksPerYear(); // tickManager.getTicksPerYear;

	/** Number of offspring. */
	private final int sizeOfOffspring = 4;

	/** Percentage of male offspring. */
	private final double percentagOfMaleOffspring = 0.4;

	/** Offset from mating to generation of independent children */
	private final int offsetFromMatingSeason = (int) (8 * 30 * TickParser.getTicksPerDay()); // tickManager.getTicksPerDay

	/** Number of mothers that should be pregnant after birth season. */
	private int needToBePregnantCountdown = 0;

	/** Number of mothers in total. */
	private int needToBePregnantTotal = 0;

	/** TODO */
	private double pregnantScore = 0;

	public BirthManager(Context<Object> context, Grid<Object> grid, ContinuousSpace<Object> space) {
		startOfBirthSeason = (int) (Environment.getInstance().getMatingSeasonStartDay() * TickParser.getTicksPerDay())
				+ offsetFromMatingSeason;
		durationOfBirthSeason = (int) ((Environment.getInstance().getMatingSeasonEndDay()
				- Environment.getInstance().getMatingSeasonStartDay()) * TickParser.getTicksPerDay());
		percentageOfBirth = 0.75; // TODO add to parameters
		this.context = context;
		this.grid= grid;
		this.space= space;
	}

	/**
	 * This function will be called from the repast-framework.
	 * 
	 * @throws Exception
	 */
	@ScheduledMethod(start = 1, interval = 1) // Call every iteration of the simulation
	public void run() throws Exception {
		if (isBirthInterval(RunEnvironment.getInstance().getCurrentSchedule().getTickCount())) {
			ArrayList<TasmanianDevil> possibleMothers = getPossibleMothers();
			int mothersCount = possibleMothers.size();
			// if no children needs to be born, calculate new value for this season
			if (needToBePregnantCountdown == 0) {
				needToBePregnantTotal = (int) (mothersCount * percentageOfBirth) + 1;
				needToBePregnantCountdown = needToBePregnantTotal;
				// System.out.println("Give birth to "+ needToBePregnant + "in total.");

			}

			// give birth on each day to average number of new children:
			pregnantScore += (double) (needToBePregnantTotal) / durationOfBirthSeason;
			int numberOfNewPregnantAnimals = (int) (pregnantScore);
			pregnantScore -= numberOfNewPregnantAnimals;
			giveBirth(numberOfNewPregnantAnimals, possibleMothers);
			needToBePregnantCountdown -= numberOfNewPregnantAnimals;
		} else {
			if (needToBePregnantCountdown > 0) {
				// spawn rest of animals
				giveBirth(needToBePregnantCountdown, getPossibleMothers());
				needToBePregnantCountdown = 0;
				pregnantScore = 0;
			}
		}
		if(TickParser.getDayInYear((int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount())%365==0) {
			addHealthyIndividuals(Environment.getInstance().getAddHealthy());
		}
	}

	private void addHealthyIndividuals(int num) {

		for (int i = 0; i < num; i++) {
			TasmanianDevil devil;
			double rnd = RandomHelper.nextDouble();
			if (rnd < Environment.getInstance().getFemaleRatio()) {
				devil = new TasmanianDevil(space, grid, new FemaleHealthyState(), context);
			} else {
				devil = new TasmanianDevil(space, grid, new MaleHealthyState(), context);
			}
			devil.setAge((int) (RandomHelper.nextDoubleFromTo(1, 2) * TickParser.getTicksPerYear()));
			context.add(devil);
			NdPoint pt = space.getLocation(devil);
			grid.moveTo(devil, (int) pt.getX(), (int) pt.getY());
			devil.setHome(grid.getLocation(devil));
		}
	}

	private ArrayList<TasmanianDevil> getPossibleMothers() {
		ArrayList<TasmanianDevil> possibleMothers = new ArrayList<>();
		for (Object obj : context.getObjects(TasmanianDevil.class)) {
			TasmanianDevil currDevil = (TasmanianDevil) obj;
			if (currDevil.getDead() == 0 && currDevil.getAge() >= canGetOffspringAge
					&& currDevil.getCurrentState().isFemaleState()) {
				possibleMothers.add(currDevil);
			}
		}
		return possibleMothers;
	}

	private boolean isBirthInterval(double currentTick) {
		int ticksPerYear = (int) TickParser.getTicksPerYear();
		return ((startOfBirthSeason) % ticksPerYear < (currentTick) % ticksPerYear && currentTick % ticksPerYear
				- startOfBirthSeason % ticksPerYear < (durationOfBirthSeason) % ticksPerYear);
	}

	private void giveBirth(int numberOfNewPregnantAnimals, ArrayList<TasmanianDevil> possibleMothers) {
		while (numberOfNewPregnantAnimals != 0 && possibleMothers != null
				&& possibleMothers.size() > numberOfNewPregnantAnimals && possibleMothers.size() > 0) {
			int index = random.nextInt(possibleMothers.size());
			TasmanianDevil mother = possibleMothers.get(index);
			for (int i = 0; i < sizeOfOffspring; i++) {
				TasmanianDevil babyDevil = generateBabyDevil(mother);
				if (!mother.getCurrentState().isSickState()) {
					double resistanceChance = random.nextDouble();
					if (resistanceChance <= Environment.getInstance().getResistanceGainRate()) {
						babyDevil.setResistanceFactor(Environment.getInstance().getResistanceFactor());
					}
				}
				context.add(babyDevil);
				NdPoint pt = babyDevil.getSpace().getLocation(babyDevil);
				babyDevil.getGrid().moveTo(babyDevil, (int) pt.getX(), (int) pt.getY());
				babyDevil.setHome(babyDevil.getGrid().getLocation(babyDevil));
			}
			numberOfNewPregnantAnimals--;
			possibleMothers.remove(index);
		}
	}

	private TasmanianDevil generateBabyDevil(TasmanianDevil mother) {
		double probabilityMale = random.nextDouble();

		if (probabilityMale < percentagOfMaleOffspring) {
			if (mother.getCurrentState().isSickState()) {
				TasmanianDevil offspring = new TasmanianDevil(mother.getSpace(), mother.getGrid(), new MaleSickState(),
						context);
				if (mother.getSickDFT1() > 0) {
					offspring.incrementSickDFT1(1);
				}
				if (mother.getSickDFT2() > 0) {
					offspring.incrementSickDFT2(1);
				}
				return offspring;
			} else
				return new TasmanianDevil(mother.getSpace(), mother.getGrid(), new MaleHealthyState(), context);
		} else if (mother.getCurrentState().isSickState()) {
			TasmanianDevil offspring = new TasmanianDevil(mother.getSpace(), mother.getGrid(), new FemaleSickState(),
					context);
			if (mother.getSickDFT1() > 0) {
				offspring.incrementSickDFT1(1);
			}
			if (mother.getSickDFT2() > 0) {
				offspring.incrementSickDFT2(1);
			}
			return offspring;
		} else
			return new TasmanianDevil(mother.getSpace(), mother.getGrid(), new FemaleHealthyState(), context);

	}

}
