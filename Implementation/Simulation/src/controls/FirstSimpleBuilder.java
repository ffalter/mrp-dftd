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

		//add male sick individuals
		int maleSickCount = 2;
		for(int i = 0; i < maleSickCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new MaleSickState());
			context.add(tmpDevil);
			tmpDevil.setHome(grid.getLocation(this));
		}

		//add male healthy individuals
		int maleHealthyCount = 1;
		for(int i = 0; i < maleHealthyCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new MaleHealthyState());
			context.add(tmpDevil);
			tmpDevil.setHome(grid.getLocation(this));
		}
		//add male sick individuals
		int femaleSickCount = 3;
		for(int i = 0; i < femaleSickCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new FemaleSickState());
			context.add(tmpDevil);
			tmpDevil.setHome(grid.getLocation(this));
		}

		//add male healthy individuals
		int femaleHealthyCount = 4;
		for(int i = 0; i < femaleHealthyCount; i++  ) {
			TasmanianDevil tmpDevil = new TasmanianDevil(space, grid, new FemaleHealthyState());
			context.add(tmpDevil);
			tmpDevil.setHome(grid.getLocation(this));
		}		
		
		for(Object obj:context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int)pt.getX(),(int)pt.getY());
		}
		
		return context;
	}
	
	public void setParams() {
		// trigger first initialization
		Environment.getInstance();
	}

	
}
