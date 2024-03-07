package replicator;

import aggregator.Aggregator;
import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;
import spaces.Spaces;
import sweep.SimStateSweep;

public class Replicator extends Aggregator {
	public int maxAgents;
	public int reproductionRadius;
	public double reproductionResources; //resources required for reproduction
	public double maxResources; //the maximum resources an agent can carry
	public int averagelifeSpan;
	public int lifeSpanSD;
	public double probabilityOfDying;
	public double parentalInvestment;
	public boolean reproduceUniqueLocation;
	public boolean carryingCapacity;
	public int lifeSpan;
	public double resources;
	public int age;
	public Bag agents = new Bag();


	public Replicator(SimStateSweep state, int x, int y, int age, int walkRuleNumber) {
		super(state,x,y,walkRuleNumber);
		ReplicatorEnvironment lstate = (ReplicatorEnvironment)state;
		maxAgents = lstate.maxAgents;
		reproductionRadius = lstate.reproductionRadius;
		reproductionResources = lstate.reproductionResources;
		maxResources = lstate.maxResources;
		averagelifeSpan = lstate.averagelifeSpan;
		lifeSpanSD = lstate.lifeSpanSD;
		parentalInvestment = lstate.parentalInvestment;
		reproduceUniqueLocation = lstate.reproduceUniqueLocation;
		carryingCapacity = lstate.carryingCapacity;
		probabilityOfDying = lstate.probabilityOfDying;
		lifeSpan = (int)(averagelifeSpan + state.random.nextGaussian() * lifeSpanSD); //agent's lifespan
		this.age = age;
	}

	public double getResources() {
		return resources;
	}

	public Replicator findRandomAgent(SimState state, SparseGrid2D space, int mode, int searchRadius, boolean includeOrigin){
		agents.clear();
		agents = space.getMooreNeighbors(x, y, searchRadius, mode, includeOrigin);
		if(agents != null)
			return (Replicator)agents.objs[state.random.nextInt(agents.numObjs)];
		else
			return null;
	}

	/**
	 * An interface for reproducing agents.  When Replicator is extended, implement your reproduction methods.
	 * @author jcschankadmin
	 *
	 */
	public interface Reproducible{
		public Replicator replicate(SimStateSweep state, Int2D location);
	}

	public boolean reproduceRandom(SimStateSweep state, Reproducible method){
		Int2D location = checkReproduceRandom(state);
		if(location != null){
			Replicator offspring  = method.replicate(state, location); 
			scheduleAndPlace(state, offspring);
			return true;
		}
		else{
			return false;
		}
	}

	public Int2D checkReproduceRandom(SimStateSweep state){
		if(resources < reproductionResources){ //insufficient resources to reproduce
			return null;
		}
		if(spaces == Spaces.OBJECT){
			Int2D location = state.objectSpace.randomEmptyLocation( state, x, y, mode,reproductionRadius, false);
			if(location != null){
				return location; //found one
			}
			else{
				return null;//no empty location
			}

		}
		else if(spaces == Spaces.SPARSE){
			Int2D location = null;
			if(reproduceUniqueLocation){
				location = state.sparseSpace.randomEmptyLocation( state, x, y, mode,reproductionRadius, false);
				if(location != null){
					return location; //found one
				}
				else{
					return null;//no empty location
				}
			}
			else{ //any location withing reproduction radius
				location = state.sparseSpace.randomLocation( state, x,y, mode,reproductionRadius, false);
				if(location != null){
					return location; //found one
				}
				else{
					return null;//no empty location
				}
			}
		}
		else{
			return null;
		}
	}


	/**
	 * Uses a supplied Reproducible method to replicate offspring. Mode of reproduction, parental care, mutation or recombination
	 * are dependent on the specifics of the implementing class.
	 * @param state
	 * @param method
	 * @return
	 */
	public boolean reproduceWait(SimStateSweep state, Reproducible method){
		Int2D location = checkReproduceWait(state);
		if(location != null){
			Replicator offspring  = method.replicate(state, location); 
			scheduleAndPlace(state, offspring);
			return true;
		}
		else{
			return false;
		}
	}


	/**
	 * Determines whether an agent can reproduce and then finds a location if one
	 * is available.  In all other cases it returns null.
	 * @param state
	 * @return
	 */

	public Int2D checkReproduceWait(SimStateSweep state){
		if(resources < reproductionResources){ //insufficient resources to reproduce
			return null;
		}
		if(spaces == Spaces.OBJECT){
			final Bag agentNum = state.objectSpace.getAllObjects();
			if(carryingCapacity && agentNum.numObjs >= maxAgents){
				return null; //too many agents to reproduce
			}
			Int2D location = state.objectSpace.randomEmptyLocation( state, x, y, mode,reproductionRadius, false);
			if(location != null){
				return location; //found one
			}
			else{
				return null;//no empty location
			}

		}
		else if(spaces == Spaces.SPARSE){
			final Bag agentNum = state.sparseSpace.getAllObjects();
			if(carryingCapacity && agentNum.numObjs > maxAgents){
				return null; //too many agents to reproduce
			}
			Int2D location = null;
			if(reproduceUniqueLocation){
				location = state.sparseSpace.randomEmptyLocation( state, x, y, mode,reproductionRadius, false);
				if(location != null){
					return location; //found one
				}
				else{
					return null;//no empty location
				}
			}
			else{ //any location withing reproduction radius
				location = state.sparseSpace.randomLocation( state, x,y, mode,reproductionRadius, false);
				if(location != null){
					return location; //found one
				}
				else{
					return null;//no empty location
				}
			}
		}
		else{
			return null;
		}
	}

	public boolean scheduleAndPlace(SimStateSweep state, Replicator offspring){
		if(offspring != null){
			offspring.event =state.schedule.scheduleRepeating(offspring);
			switch(spaces){
			case OBJECT: state.objectSpace.addAgentToLocation(offspring, offspring.x, offspring.y);
			return true;
			case SPARSE: state.sparseSpace.setObjectLocation(offspring, offspring.x, offspring.y);
			return true;
			}
		}

		return false; //else if offspring null
	}

	public void die(SimStateSweep state){
		super.die(state);
		if(agents != null)
			agents.clear();
		agents = null;

	}

	public void truncateResource(){
		if(resources > maxResources)
			resources = maxResources;
	}


	public void step(SimState state){
		age++;  //updates age
	}
}
