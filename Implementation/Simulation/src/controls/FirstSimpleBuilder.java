package controls;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import simulation.Environment;
import simulation.TasmanianDevil;
import states.FemaleHealthyState;
import states.MaleHealthyState;
import states.FemaleSickState;
import states.MaleSickState;

public class FirstSimpleBuilder implements ContextBuilder<Object> {

	@Override
	public Context<Object> build(Context<Object> context) {
		StateManager.initialize(context);
		Environment.reset();
		Environment params= Environment.getInstance();
		double age0 = 0.5735;
		double age1 = 0.1195 + age0;
		double age2 = 0.0992 + age1;
		double age3 = 0.0833 + age2;
		double age4 = 0.0692 + age3;
		
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("infection network", context,true);
		netBuilder.buildNetwork();
		context.setId("simulation");
		ContinuousSpaceFactory spaceFactory =
				ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space",
				context, new RandomCartesianAdder<Object>(),new repast.simphony.space.continuous.WrapAroundBorders(),params.getMapSizeX(),params.getMapSizeY());
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		
		Grid<Object> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(new WrapAroundBorders(),new SimpleGridAdder<Object>(),true,params.getMapSizeX(),params.getMapSizeY()));


		int maleSickCount = (int)(params.getPopulationSize()*(1-params.getFemaleRatio())*params.getInitialSickMale());
		for(int i = 0; i < maleSickCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new MaleSickState());
			context.add(tmpDevil);
			if(i<maleSickCount*age0) {
				tmpDevil.setAge(0);
			}else if(i<maleSickCount*age1){
				tmpDevil.setAge(1);
			}else if(i<maleSickCount*age2){
				tmpDevil.setAge(2);
			}else if(i<maleSickCount*age3){
				tmpDevil.setAge(3);
			}else if(i<maleSickCount*age4){
				tmpDevil.setAge(4);
			}else{
				tmpDevil.setAge(5);
			}
		}

		//add male healthy individuals
		int maleHealthyCount = (int)(params.getPopulationSize()*(1-params.getFemaleRatio())*(1-params.getInitialSickMale()));
		for(int i = 0; i < maleHealthyCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new MaleHealthyState());
			context.add(tmpDevil);
			if(i<maleHealthyCount*age0) {
				tmpDevil.setAge(0);
			}else if(i<maleHealthyCount*age1){
				tmpDevil.setAge(1);
			}else if(i<maleHealthyCount*age2){
				tmpDevil.setAge(2);
			}else if(i<maleHealthyCount*age3){
				tmpDevil.setAge(3);
			}else if(i<maleHealthyCount*age4){
				tmpDevil.setAge(4);
			}else{
				tmpDevil.setAge(5);
			}
		}
		//add male sick individuals
		int femaleSickCount = (int)(params.getPopulationSize()*params.getFemaleRatio()*params.getInitialSickFemale());
		System.out.println("femalesick"+ femaleSickCount);
		for(int i = 0; i < femaleSickCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new FemaleSickState());
			context.add(tmpDevil);
			if(i<femaleSickCount*age0) {
				tmpDevil.setAge(0);
			}else if(i<femaleSickCount*age1){
				tmpDevil.setAge(1);
			}else if(i<femaleSickCount*age2){
				tmpDevil.setAge(2);
			}else if(i<femaleSickCount*age3){
				tmpDevil.setAge(3);
			}else if(i<femaleSickCount*age4){
				tmpDevil.setAge(4);
			}else{
				tmpDevil.setAge(5);
			}
		}

		//add male healthy individuals
		int femaleHealthyCount = (int)(params.getPopulationSize()*params.getFemaleRatio()*(1-params.getInitialSickFemale()));
		System.out.println("femalehealthy"+ femaleHealthyCount);
		for(int i = 0; i < femaleHealthyCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new FemaleHealthyState());
			context.add(tmpDevil);
			if(i<femaleHealthyCount*age0) {
				tmpDevil.setAge(0);
			}else if(i<femaleHealthyCount*age1){
				tmpDevil.setAge(1);
			}else if(i<femaleHealthyCount*age2){
				tmpDevil.setAge(2);
			}else if(i<femaleHealthyCount*age3){
				tmpDevil.setAge(3);
			}else if(i<femaleHealthyCount*age4){
				tmpDevil.setAge(4);
			}else{
				tmpDevil.setAge(5);
			}	
		}		
		
		for(Object obj:context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int)pt.getX(),(int)pt.getY());
		}
		
		for(Object obj : context.getObjects(TasmanianDevil.class)) {
			TasmanianDevil devil = (TasmanianDevil)obj;
			devil.setHome(grid.getLocation(devil));
		}

		BirthManager birthManager = new BirthManager(context);
		context.add(birthManager);
		
		VaccinationManager vaccinationManager = new VaccinationManager(context);
		context.add(vaccinationManager);
		
		return context;
	}

}