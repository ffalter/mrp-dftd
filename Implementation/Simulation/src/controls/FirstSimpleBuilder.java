package controls;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
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
		StateManager.initialize(context);
		InteractionManager.reset();
		InteractionManager.getInstance();
		
		context.add(new TerminationControl(context));
		/// run for a specific time 
		//if(RunEnvironment.getInstance().isBatch()){
	    RunEnvironment.getInstance().endAt(TickParser.getTicksPerYear()*params.getEndAfterYears());
	    //}
		
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
			TasmanianDevil tmpDevil= new TasmanianDevil(space, grid, new MaleSickState(), context);
			double rnd = RandomHelper.nextDoubleFromTo(0, 1);
			if(rnd<params.getInitDftd1Infected()) {
				tmpDevil.incrementSickDFT1((int)(RandomHelper.nextIntFromTo(1, 380)*TickParser.getTicksPerDay()));
			}else if(rnd<(params.getInitDftd2Infected()+params.getInitDftd1Infected())) {
				tmpDevil.incrementSickDFT2((int)(RandomHelper.nextIntFromTo(1, 380)*TickParser.getTicksPerDay()));
			}else {
				tmpDevil.incrementSickDFT1((int)(RandomHelper.nextIntFromTo(1, 380)*TickParser.getTicksPerDay()));
				tmpDevil.incrementSickDFT2((int)(RandomHelper.nextIntFromTo(1, 380)*TickParser.getTicksPerDay()));
			}
			context.add(tmpDevil);
			if(i<maleSickCount*age0) {
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(0, 1)*TickParser.getTicksPerYear()));
			}else if(i<maleSickCount*age1){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(1, 2)*TickParser.getTicksPerYear()));
			}else if(i<maleSickCount*age2){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(2, 3)*TickParser.getTicksPerYear()));
			}else if(i<maleSickCount*age3){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(3, 4)*TickParser.getTicksPerYear()));
			}else if(i<maleSickCount*age4){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(4, 5)*TickParser.getTicksPerYear()));
			}else{
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(5, 6)*TickParser.getTicksPerYear()));
			}
		}

		//add male healthy individuals
		int maleHealthyCount = (int)(params.getPopulationSize()*(1-params.getFemaleRatio())*(1-params.getInitialSickMale()));
		for(int i = 0; i < maleHealthyCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new MaleHealthyState(), context);
			context.add(tmpDevil);
			if(i<maleHealthyCount*age0) {
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(0, 1)*TickParser.getTicksPerYear()));
			}else if(i<maleHealthyCount*age1){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(1, 2)*TickParser.getTicksPerYear()));
			}else if(i<maleHealthyCount*age2){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(2, 3)*TickParser.getTicksPerYear()));
			}else if(i<maleHealthyCount*age3){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(3, 4)*TickParser.getTicksPerYear()));
			}else if(i<maleHealthyCount*age4){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(4, 5)*TickParser.getTicksPerYear()));
			}else{
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(5, 6)*TickParser.getTicksPerYear()));
			}
		}
		//add male sick individuals
		int femaleSickCount = (int)(params.getPopulationSize()*params.getFemaleRatio()*params.getInitialSickFemale());
		for(int i = 0; i < femaleSickCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new FemaleSickState(), context);
			double rnd = RandomHelper.nextDoubleFromTo(0, 1);
			if(rnd<params.getInitDftd1Infected()) {
				tmpDevil.incrementSickDFT1((int)(RandomHelper.nextIntFromTo(1, 380)*TickParser.getTicksPerDay()));
			}else if(rnd<(params.getInitDftd2Infected()+params.getInitDftd1Infected())) {
				tmpDevil.incrementSickDFT2((int)(RandomHelper.nextIntFromTo(1, 380)*TickParser.getTicksPerDay()));
			}else {
				tmpDevil.incrementSickDFT1((int)(RandomHelper.nextIntFromTo(1, 380)*TickParser.getTicksPerDay()));
				tmpDevil.incrementSickDFT2((int)(RandomHelper.nextIntFromTo(1, 380)*TickParser.getTicksPerDay()));
			}
			context.add(tmpDevil);
			if(i<femaleSickCount*age0) {
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(0, 1)*TickParser.getTicksPerYear()));
			}else if(i<femaleSickCount*age1){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(1, 2)*TickParser.getTicksPerYear()));
			}else if(i<femaleSickCount*age2){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(2, 3)*TickParser.getTicksPerYear()));
			}else if(i<femaleSickCount*age3){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(3, 4)*TickParser.getTicksPerYear()));
			}else if(i<femaleSickCount*age4){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(4, 5)*TickParser.getTicksPerYear()));
			}else{
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(5, 6)*TickParser.getTicksPerYear()));
			}
		}

		//add male healthy individuals
		int femaleHealthyCount = (int)(params.getPopulationSize()*params.getFemaleRatio()*(1-params.getInitialSickFemale()));
		for(int i = 0; i < femaleHealthyCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new FemaleHealthyState(), context);
			context.add(tmpDevil);
			if(i<femaleHealthyCount*age0) {
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(0, 1)*TickParser.getTicksPerYear()));
			}else if(i<femaleHealthyCount*age1){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(1, 2)*TickParser.getTicksPerYear()));
			}else if(i<femaleHealthyCount*age2){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(2, 3)*TickParser.getTicksPerYear()));
			}else if(i<femaleHealthyCount*age3){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(3, 4)*TickParser.getTicksPerYear()));
			}else if(i<femaleHealthyCount*age4){
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(4, 5)*TickParser.getTicksPerYear()));
			}else{
				tmpDevil.setAge((int)(RandomHelper.nextDoubleFromTo(5, 6)*TickParser.getTicksPerYear()));
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

		BirthManager birthManager = new BirthManager(context, grid, space);
		context.add(birthManager);

		VaccinationManager vaccinationManagerDFT1 = new VaccinationManager(context,true);
		context.add(vaccinationManagerDFT1);
		VaccinationManager vaccinationManagerDFT2 = new VaccinationManager(context,false);
		context.add(vaccinationManagerDFT2);
		
		return context;
	}
}