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
		Environment.reset();
		Environment params= Environment.getInstance();
		
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
			if(i<maleSickCount-(maleSickCount/26.*16)) {
				tmpDevil.setAge(1);
			}else if(i<maleSickCount-(maleSickCount/26.*9)){
				tmpDevil.setAge(2);
			}else if(i<maleSickCount-(maleSickCount/26.*4)){
				tmpDevil.setAge(3);
			}else{
				tmpDevil.setAge(4);
			}
		}

		//add male healthy individuals
		int maleHealthyCount = (int)(params.getPopulationSize()*(1-params.getFemaleRatio())*(1-params.getInitialSickMale()));
		for(int i = 0; i < maleHealthyCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new MaleHealthyState());
			context.add(tmpDevil);
			if(i<maleSickCount-(maleHealthyCount/26.*16)) {
				tmpDevil.setAge(1);
			}else if(i<maleSickCount-(maleHealthyCount/26.*9)){
				tmpDevil.setAge(2);
			}else if(i<maleSickCount-(maleHealthyCount/26.*4)){
				tmpDevil.setAge(3);
			}else{
				tmpDevil.setAge(4);
			}
		}
		//add male sick individuals
		int femaleSickCount = (int)(params.getPopulationSize()*params.getFemaleRatio()*params.getInitialSickFemale());
		System.out.println("femalesick"+ femaleSickCount);
		for(int i = 0; i < femaleSickCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new FemaleSickState());
			context.add(tmpDevil);
			if(i<maleSickCount-(femaleSickCount/26.*16)) {
				tmpDevil.setAge(1);
			}else if(i<maleSickCount-(femaleSickCount/26.*9)){
				tmpDevil.setAge(2);
			}else if(i<maleSickCount-(femaleSickCount/26.*4)){
				tmpDevil.setAge(3);
			}else{
				tmpDevil.setAge(4);
			}
		}

		//add male healthy individuals
		int femaleHealthyCount = (int)(params.getPopulationSize()*params.getFemaleRatio()*(1-params.getInitialSickFemale()));
		System.out.println("femalehealthy"+ femaleHealthyCount);
		for(int i = 0; i < femaleHealthyCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new FemaleHealthyState());
			context.add(tmpDevil);
			if(i<maleSickCount-(femaleHealthyCount/26.*16)) {
				tmpDevil.setAge(1);
			}else if(i<maleSickCount-(femaleHealthyCount/26.*9)){
				tmpDevil.setAge(2);
			}else if(i<maleSickCount-(femaleHealthyCount/26.*4)){
				tmpDevil.setAge(3);
			}else{
				tmpDevil.setAge(4);
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