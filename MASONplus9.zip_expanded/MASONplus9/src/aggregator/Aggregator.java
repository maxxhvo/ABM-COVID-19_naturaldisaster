package aggregator;


import randomWalker.RandomWalker;
import randomWalker.RandomWalkerEnvironment;
import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Int2D;
import spaces.ObjectGrid2Dex;
import spaces.Spaces;
import spaces.SparseGrid2Dex;
import sweep.SimStateSweep;

/**
 * A mobile agent dependent on the BasicEnvrionment class.  Moves randomly, 
 * aggregates and coordinates with others.
 * 
 * @author Jeff Schank 2017
 *
 */
public class Aggregator extends RandomWalker {
	public int searchRadius;
	public boolean hyperAgg;
	public boolean aggregate;	
	public int degree;


	public Aggregator(SimStateSweep state, int x, int y,int walkRuleNumber) {
		super(state,x,y,walkRuleNumber);
		AggregatorEnvironment ae = (AggregatorEnvironment)state;
		this.uniqueLocation = ae.uniqueLocation;
		this.searchRadius = ae.searchRadius;
		this.hyperAgg = ae.hyperAgg;
		this.aggregate = ae.aggregate;
		this.degree = ae.degree;
	}



	public int decidex(SimStateSweep state, Bag neighbors){
		int posx = 0, negx = 0;
		for(int i=0; i < neighbors.numObjs;i++){
			Aggregator a = (Aggregator) neighbors.objs[i];
			if(a.x > x)
				posx++;
			if(a.x < x)
				negx++;
		}
		if(posx > negx){
			return 1;
		}
		if(negx > posx){
			return -1;
		}
		if(hyperAgg)
			return state.random.nextInt(3)-1;
		else
			return dir.x; //no change

	}

	public int decidey(SimStateSweep state, Bag neighbors){
		int posy = 0, negy = 0;
		for(int i=0; i < neighbors.numObjs;i++){
			Aggregator a = (Aggregator) neighbors.objs[i];
			if(a.y > y)
				posy++;
			if(a.y < y)
				negy++;
		}
		if(posy > negy){
			return 1;
		}
		if(negy > posy){
			return -1;
		}
		if(hyperAgg)
			return state.random.nextInt(3)-1;
		else
			return dir.y;

	}

	public void aggregate(SimStateSweep state, ObjectGrid2Dex space){
		ox = x;
		oy = y;
		Bag neighbors = space.getMooreNeighbors(x, y, searchRadius, mode, false);
		if(neighbors.numObjs >= degree){
				if(hyperAgg)
			move(state, space);//attempt to randomly move into an empty cell
			return;//No need to go further
		}
		Int2D temp = null;
		if(neighbors.numObjs > 0){
			temp = new Int2D(decidex(state,neighbors),decidey(state,neighbors));
			int tx = x+temp.x;
			int ty = y+temp.y;
			if(!toroidal){
				tx = bx(space,tx);
				ty = by(space,ty);
			}
			else{
				tx = space.stx(tx);
				ty = space.sty(ty);
			}
			if(uniqueLocation || openLocation){
				Object b = space.getObjectAtLocation(tx, ty);
				if(b == null){
					x = tx;
					y = ty;
					dir = temp;
					space.setObjectLocation(this, ox, oy, x, y);
				}
				else{
					//if(hyperAgg)
					//move(state,space);//attempt to randomly move into an empty cell
				} //b == null

			}
			else{ //!(uniqueLocation || openLocation)
				x = tx;
				y = ty;
				dir = temp;
				space.setObjectLocation(this, ox, oy, x, y);
			}
		}
		else{
			move(state,space);
		}
	}


	public void aggregate(SimStateSweep state, SparseGrid2Dex space){
		Bag neighbors = space.getMooreNeighbors(x, y, searchRadius, mode, false);
		if(neighbors.numObjs >= degree){
			if(hyperAgg)
				move(state,space);//attempt to randomly move into an empty cell
			return;//No need to go further
		}
		Int2D temp = null;
		if(neighbors.numObjs > 0){
			temp = new Int2D(decidex(state,neighbors),decidey(state,neighbors));
			int tx = x+temp.x;
			int ty = y+temp.y;
			if(!toroidal){
				tx = bx(space,tx);
				ty = by(space,ty);
			}
			else{
				tx = space.stx(tx);
				ty = space.sty(ty);
			}
			if(uniqueLocation || openLocation){
				Bag b = space.getObjectsAtLocation(tx, ty);
				if(b == null){
					x = tx;
					y = ty;
					dir = temp;
					space.setObjectLocation(this, x, y);
				}
				else{
					//if(hyperAgg)
					//move(state,space);//attempt to randomly move into an empty cell
				} //b == null

			}
			else{ //!(uniqueLocation || openLocation)
				x = tx;
				y = ty;
				dir = temp;
				space.setObjectLocation(this, x, y);
			}
		}
		else{
			move(state,space);
		}
	}


	public void die(SimStateSweep state){
		super.die(state);
	}

	public void aggregation(SimStateSweep state){
		if(spaces == Spaces.OBJECT){
			if(aggregate){
				aggregate((SimStateSweep)state, objectSpace);
			}
			else{
				move((SimStateSweep)state, objectSpace);
			}
		}
		else if(spaces == Spaces.SPARSE){
			if(aggregate){
				aggregate((SimStateSweep)state, sparseSpaceGrid);
			}
			else{
				move((SimStateSweep)state, sparseSpaceGrid);
			}
		}
	}

	public void step(SimState state) {
		 aggregation((SimStateSweep) state);
	}
}
