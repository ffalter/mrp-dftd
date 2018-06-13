package controls;

import simulation.Environment;
import simulation.TasmanianDevil;

public class InteractionManager {

	private static InteractionManager instance;
	
	enum gender {FEMALE, MALE}
	enum states {HEALTHY, VACCINATED, SICK}
	enum dft 	{DFT1, DFT2}
	
	private double [][][][][] interactionMatrix;
	
	private double epsilon;
	private double delta;
	private double x;
	
	private InteractionManager()
	{
		epsilon = Environment.getInstance().getVaccinatedInfectionRate();
		delta = Environment.getInstance().getInfectionMatingFactor();
		x = Environment.getInstance().getInfectionRate();
		interactionMatrix = new double [dft.DFT2.ordinal()+1][gender.MALE.ordinal()+1][states.SICK.ordinal()+1][gender.MALE.ordinal()+1][states.SICK.ordinal()+1];
		
		for (int df=0;df<dft.DFT2.ordinal()+1;df++) {
			for(int gen=0;gen<gender.MALE.ordinal()+1;gen++) {
				for(int state=0;state<states.SICK.ordinal()+1;state++) {
					for(int gen2=0;gen2<gender.MALE.ordinal()+1;gen2++) {
						for(int state2=0;state2<states.SICK.ordinal()+1;state2++) {
							interactionMatrix[df][gen][states.SICK.ordinal()][gen2][state2] = 1;
							interactionMatrix[df][gen][states.VACCINATED.ordinal()][gen2][states.SICK.ordinal()] = epsilon;
							interactionMatrix[df][gen][states.HEALTHY.ordinal()][gen2][states.SICK.ordinal()] = x;
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
	
	public void reset() {
		instance = new InteractionManager();
	}
	
	public double[] getInfectionProbability (TasmanianDevil devil1, TasmanianDevil devil2){
		double[] ret = new double[2];
		
		boolean dft1,dft2;
		
		dft1 = devil2.isInfectiousDFT1();
		dft2 = devil2.isInfectiousDFT2();
		
		int gender1,gender2,state1dft1,state1dft2,state2;
		
		if(devil1.isFemale()) {
			gender1 = 0;
		} else {
			gender1 = 1;
		}
		
		if(devil2.isFemale()) {
			gender2 = 0;
		} else {
			gender2 = 1;
		}

		// !DFT1sick && DFT1vaccinated --> stateDFT1 = vaccinated
		// !DFT2sick && DFT2vaccinated --> stateDFT2 = vaccinated
		if(devil1.getSickDFT1() == 0) {
			if(devil1.isVaccinatedDFT1()) {
				state1dft1 = 1;
			} else {
				state1dft1 = 0;
			}
		} else {
			state1dft1 = 2;
		}
		
		if(devil1.getSickDFT2() == 0) {
			if(devil1.isVaccinatedDFT2()) {
				state1dft2 = 1;
			} else {
				state1dft2 = 0;
			}
		} else {
			state1dft2 = 2;
		}
		
		if(devil2.getSickDFT1() == 0 && devil2.getSickDFT2() == 0) {
			if(devil2.isVaccinatedDFT1() || devil2.isVaccinatedDFT2()) {
				state2 = 1;
			} else {
				state2 = 0;
			}
		} else {
			state2 = 2;
		}
		
		ret[0] = interactionMatrix[dft.DFT1.ordinal()][gender1][state1dft1][gender2][state2];
		ret[1] = interactionMatrix[dft.DFT2.ordinal()][gender1][state1dft2][gender2][state2];
		
		return ret;
		
	}
	
}
