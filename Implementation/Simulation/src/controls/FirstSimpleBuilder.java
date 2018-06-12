package controls;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
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
import states.FemaleSickState;
import states.MaleSickState;

public class FirstSimpleBuilder implements ContextBuilder<Object> {

	@Override
	public Context<Object> build(Context<Object> context) {
		setParams();
		
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("infection network", context,true);
		netBuilder.buildNetwork();
		context.setId("simulation");
		ContinuousSpaceFactory spaceFactory =
				ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space",
				context, new RandomCartesianAdder<Object>(),new repast.simphony.space.continuous.WrapAroundBorders(),50,50);
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		
		Grid<Object> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(new WrapAroundBorders(),new SimpleGridAdder<Object>(),true,50,50));
		
		//add male individuals
		int maleCount = 15;
		for(int i = 0; i < maleCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new MaleSickState());
			context.add(tmpDevil);
			tmpDevil.setHome(grid.getLocation(this));
		}
		
		//add female individuals
		int femaleCount = 10;
		for(int i = 0; i < femaleCount; i++) {
			context.add(new TasmanianDevil(space, grid, new FemaleSickState()));
			context.add(new TasmanianDevil(space,grid, new FemaleHealthyState()));
		}
		
		for(Object obj:context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int)pt.getX(),(int)pt.getY());
		}
		
		return context;
	}
	
	public void setParams() {
		final Parameters params = RunEnvironment.getInstance().getParameters();
		Environment.getInstance().infectionRate = params.getDouble("infectionRate");
		Environment.getInstance().infectionRateMating = params.getDouble("infectionRateMating");
		Environment.getInstance().femaleRatio = params.getDouble("femaleRatio");
		Environment.getInstance().interactionRadius= params.getInteger("interactionRadius");
		Environment.getInstance().mapSizeX= params.getInteger("mapSizeX");
		Environment.getInstance().mapSizeY= params.getInteger("mapSizeY");
		Environment.getInstance().populationSize= params.getInteger("populationSize");
		Environment.getInstance().steprange= params.getInteger("stepRange");
	}

	
}
