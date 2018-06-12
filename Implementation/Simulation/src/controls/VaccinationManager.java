package controls;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.NdPoint;
import simulation.Environment;
import simulation.TasmanianDevil;
import states.FemaleHealthyState;
import states.FemaleSickState;
import states.MaleHealthyState;
import states.MaleSickState;

import java.util.Random;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class VaccinationManager {
	
	/** create random numbers */
	private Random random = new Random();
	
	/** Start of the vaccination season in ticks, from beginning of the simulation.*/
	private int startOfVaccinationSeason;
	
	/** Duration of a vaccination season in ticks in this year.*/
	private int durationOfVaccinationSeason;
	
	/** Current context of the animals*/
	private Context<Object> context;
	
	/** Age which is needed to get vaccinated. */
	private final int canBeVaccinatedAge = 0 * Environment.getInstance().getTicksPerYear();

	/** Devils that will be additionally vaccinated in a year */
	private int willBeAddVaccinated = 0;
	
	/** Devils that will be additionally vaccinated in this season*/
	private int numToVaccinateThisSeason = 0;
	
	public VaccinationManager(Context<Object> context) {
		startOfVaccinationSeason 	= 0;
		durationOfVaccinationSeason = (int) TickParser.getTicksPerYear()-1;
		willBeAddVaccinated 		= 5; //Environment.getInstance().getNumAddVaccinatedPerYear();
		this.context = context;
	}

	/**
	 * This function will be called from the repast-framework.
	 * @throws Exception 
	 */
	@ScheduledMethod(start = 1, interval = 1) // Call every iteration of the simulation
	public void run() throws Exception
	{
		int currentTick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		System.out.println("isVaccinationInterval:"+isVaccinationInterval(currentTick));
		if(isVaccinationInterval(currentTick))
		{
			// if no devil needs to be vaccinated, calculate new value for this season
			System.out.println("numToVaccinateThisSeason:"+numToVaccinateThisSeason);
			if(numToVaccinateThisSeason == 0)
			{
				System.out.println("calc:"+ ((int) (willBeAddVaccinated * (durationOfVaccinationSeason+1) / TickParser.getTicksPerYear())));
				numToVaccinateThisSeason = ((int) (willBeAddVaccinated * (durationOfVaccinationSeason+1) / TickParser.getTicksPerYear()));
				System.out.println("numToVaccinateThisSeason1:"+numToVaccinateThisSeason);
			}
			System.out.println("numToVaccinateThisSeason2:"+numToVaccinateThisSeason);
			// if we vaccinate at least one per tick:
			System.out.println("at least one per tick:"+(numToVaccinateThisSeason > durationOfVaccinationSeason/TickParser.getTicksPerDay()));
			if(numToVaccinateThisSeason > durationOfVaccinationSeason/TickParser.getTicksPerDay())
			{
				int numToVaccinatePerTick = numToVaccinateThisSeason/durationOfVaccinationSeason;
				vaccinate(numToVaccinatePerTick, getPossibleVaccinatedAnimals());
				numToVaccinateThisSeason -= numToVaccinatePerTick;
			}else
			{
				//calc tick interval that vaccinates a devil
				int tickInterval= durationOfVaccinationSeason / numToVaccinateThisSeason;
				if(((currentTick%TickParser.getTicksPerYear())-startOfVaccinationSeason)%tickInterval == 0)
					vaccinate(1, getPossibleVaccinatedAnimals());
			}
			
		}
		else
		{
			//vaccinate rest of animals
			vaccinate(numToVaccinateThisSeason, getPossibleVaccinatedAnimals());
			numToVaccinateThisSeason=0;
		}
	}
	
	private ArrayList<TasmanianDevil> getPossibleVaccinatedAnimals()
	{
		ArrayList<TasmanianDevil> possibleVaccinatable = new ArrayList<>();
		for(Object obj : context.getObjects(TasmanianDevil.class))
		{
			TasmanianDevil currDevil = (TasmanianDevil) obj;			
			if(currDevil.getDead()==0 && currDevil.getAge() >= canBeVaccinatedAge && !currDevil.getCurrentState().isSickState() && !currDevil.isVaccinated())
			{
				possibleVaccinatable.add(currDevil);	
			}
		}
		return possibleVaccinatable;
	}
	
	private boolean isVaccinationInterval(double currentTick)
	{
		int ticksPerYear = (int) TickParser.getTicksPerYear(); 
		return ((startOfVaccinationSeason)%ticksPerYear < (currentTick)%ticksPerYear
			&& currentTick%ticksPerYear-startOfVaccinationSeason%ticksPerYear < (durationOfVaccinationSeason)%ticksPerYear);
	}
	
	private void vaccinate(int numToVaccinatePerTick, ArrayList<TasmanianDevil> possiblyVaccinatedDevils)
	{
		if (numToVaccinatePerTick != 0 && possiblyVaccinatedDevils != null && possiblyVaccinatedDevils.size() > numToVaccinatePerTick)
		{
			for(int i=0; i < numToVaccinatePerTick; i++)
			{
				int index = random.nextInt(possiblyVaccinatedDevils.size());
				TasmanianDevil vaccinateDevil = possiblyVaccinatedDevils.get(index);
				vaccinateDevil.vaccinate();
			}
		}
	}
	
}
