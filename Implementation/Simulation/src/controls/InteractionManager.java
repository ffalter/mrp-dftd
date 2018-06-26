package controls;

import simulation.Environment;
import simulation.TasmanianDevil;

public class InteractionManager {

	private static InteractionManager instance;
	
	enum gender {FEMALE, MALE}
	enum states {HEALTHY, VACCINATED, SICK}
	enum dft 	{DFT1, DFT2}
	
	private double [][][][][] interactionMatrix;
	
	private double epsilon1;
	private double epsilon2;
	private double delta;
	private double x1;
	private double x2;
	
	private InteractionManager()
	{
		epsilon1 = Environment.getInstance().getVaccinatedInfectionRateDFT1();
		epsilon2 = Environment.getInstance().getVaccinatedInfectionRateDFT2();
		delta = Environment.getInstance().getInfectionMatingFactor();
		x1 = Environment.getInstance().getInfectionRateDFT1();
		x2 = Environment.getInstance().getInfectionRateDFT2();
		interactionMatrix = new double [dft.DFT2.ordinal()+1][gender.MALE.ordinal()+1][states.SICK.ordinal()+1][gender.MALE.ordinal()+1][states.SICK.ordinal()+1];
		
		for (int df=0;df<dft.DFT2.ordinal()+1;df++) {
			for(int gen=0;gen<gender.MALE.ordinal()+1;gen++) {
				for(int state=0;state<states.SICK.ordinal()+1;state++) {
					for(int gen2=0;gen2<gender.MALE.ordinal()+1;gen2++) {
						for(int state2=0;state2<states.SICK.ordinal()+1;state2++) {
							interactionMatrix[df][gen][states.SICK.ordinal()][gen2][state2] = 1;
							if(df == dft.DFT1.ordinal()) {
								interactionMatrix[df][gen][states.HEALTHY.ordinal()][gen2][states.SICK.ordinal()] = x1;
								interactionMatrix[df][gen][states.VACCINATED.ordinal()][gen2][states.SICK.ordinal()] = epsilon1;
							} else {
								interactionMatrix[df][gen][states.HEALTHY.ordinal()][gen2][states.SICK.ordinal()] = x2;
								interactionMatrix[df][gen][states.VACCINATED.ordinal()][gen2][states.SICK.ordinal()] = epsilon2;
							}
							interactionMatrix[df][gen][states.VACCINATED.ordinal()][gen2][states.VACCINATED.ordinal()] = 0;
							interactionMatrix[df][gen][states.HEALTHY.ordinal()][gen2][states.HEALTHY.ordinal()] = 0;
							interactionMatrix[df][gen][states.VACCINATED.ordinal()][gen2][states.HEALTHY.ordinal()] = 0;
							interactionMatrix[df][gen][states.HEALTHY.ordinal()][gen2][states.VACCINATED.ordinal()] = 0;
						}
					}
				}
			}
			interactionMatrix[df][gender.MALE.ordinal()][states.VACCINATED.ordinal()][gender.FEMALE.ordinal()][states.SICK.ordinal()] *= delta;
			interactionMatrix[df][gender.MALE.ordinal()][states.HEALTHY.ordinal()][gender.FEMALE.ordinal()][states.SICK.ordinal()] *= delta;
			interactionMatrix[df][gender.FEMALE.ordinal()][states.VACCINATED.ordinal()][gender.MALE.ordinal()][states.SICK.ordinal()] *= delta;
			interactionMatrix[df][gender.FEMALE.ordinal()][states.HEALTHY.ordinal()][gender.MALE.ordinal()][states.SICK.ordinal()] *= delta; 
		}
		
	}
	
	public static InteractionManager getInstance() {
		if (instance == null) {
			instance = new InteractionManager();
		}
		return instance;
	}
	
	public static void reset() {
		instance = new InteractionManager();
	}
	
	public double[] getInfectionProbability (TasmanianDevil devil1, TasmanianDevil devil2){
		double[] ret = new double[2];
		
		int gender1,gender2,state1dft1,state1dft2,state2dft1,state2dft2;
		
		if(devil1.isFemale()) {
			gender1 = gender.FEMALE.ordinal();
		} else {
			gender1 = gender.MALE.ordinal();
		}
		
		if(devil2.isFemale()) {
			gender2 = gender.FEMALE.ordinal();
		} else {
			gender2 = gender.MALE.ordinal();
		}

		//first devil states
		if(devil1.getSickDFT1() > 0)
			state1dft1=states.SICK.ordinal();
		else if(devil1.isVaccinatedDFT1())
				state1dft1=states.VACCINATED.ordinal();
		else
			state1dft1=states.HEALTHY.ordinal();
		
		if(devil1.getSickDFT2() > 0)
			state1dft2=states.SICK.ordinal();
		else if(devil1.isVaccinatedDFT2())
				state1dft2=states.VACCINATED.ordinal();
		else
			state1dft2=states.HEALTHY.ordinal();
		
		
		//second devil states

		if(devil2.isInfectiousDFT1())
			state2dft1=states.SICK.ordinal();
		else if(devil2.isVaccinatedDFT1())
				state2dft1=states.VACCINATED.ordinal();
		else
			state2dft1=states.HEALTHY.ordinal();
		
		if(devil2.isInfectiousDFT2())
			state2dft2=states.SICK.ordinal();
		else if(devil2.isVaccinatedDFT2())
				state2dft2=states.VACCINATED.ordinal();
		else
			state2dft2=states.HEALTHY.ordinal();
			
		ret[0] = interactionMatrix[dft.DFT1.ordinal()][gender1][state1dft1][gender2][state2dft1];
		ret[1] = interactionMatrix[dft.DFT2.ordinal()][gender1][state1dft2][gender2][state2dft2];
		
		return ret;
		
	}
	
}
