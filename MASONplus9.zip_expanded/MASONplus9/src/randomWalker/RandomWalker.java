package randomWalker;

import java.awt.Color;
import java.util.ArrayList;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.field.grid.SparseGrid2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.Bag;
import sim.util.Int2D;
import spaces.ObjectGrid2Dex;
import spaces.Spaces;
import spaces.SparseGrid2Dex;
import sweep.SimStateSweep;
import sweep.GUIStateSweep;

/**
 * Implements the theory of one-step random walks found in Smaldino and Schank (2012)
 * Movement Patterns, Social Dynamics, and the Evolution of Cooperation. 
 * Theor Popul Biol. 2012 Aug; 82(1): 48–58. There are three implementations. First,
 * find a random open location specified by the walk rule. Second, choose one step
 * but only move to the location if empty. For both implementations, if there isn't
 * an empty location, change orientation (dir) according to the walk rule. The third
 * rule moves to the random location irrespective of it being occupied. The order
 * of speed of the three rules is  second > third > first.  The second is about 17% faster 
 * than the third and about 30% faster than the first.
 * 
 * @author jcschank
 * 
 */
@SuppressWarnings("serial")
public class RandomWalker implements Steppable {
	public SparseGrid2Dex sparseSpaceGrid = null; //offspring class of SparseGrid2D
	public ObjectGrid2Dex objectSpace = null; //offspring class of ObjectGrid2D
	public int mode;
	public Spaces spaces;
	public int x; // x location
	public int y; // y location
	public int ox; //for Object spaces
	public int oy;
	public Int2D dir; // change in x and y locations
	public boolean openLocation; // find random open location if possible
	public boolean uniqueLocation; //find a unique location if possible
	public boolean toroidal; //if false, space is bounded: the only two possiblilities allowed
	private int[][] orientations = RandomWalkMechanics.orientationLookUp;
	private ArrayList<Int2D> possibleMoves = new ArrayList<Int2D>(RandomWalkMechanics.DIRECTIONS);
	private Int2D[] listOfMoves;
	ArrayList<Int2D[]> walkRules;
	public double[] walkRule;
	public int walkRuleNumber;//needed for replicaiton
	public Stoppable event;


	public RandomWalker(SimStateSweep state, int x, int y,int walkRuleNumber){
		this.x = x;
		this.y = y;
		this.walkRuleNumber = walkRuleNumber;
		RandomWalkerEnvironment rweState = (RandomWalkerEnvironment) state;
		dir = RandomWalkMechanics.directionLookUp[state.random.nextInt(RandomWalkMechanics.DIRECTIONS)];//get random orientation
		walkRule = RandomWalkMechanics.initRule(walkRuleNumber);
		walkRules = RandomWalkMechanics.getRules(walkRule);
		openLocation = rweState.openLocation;
		uniqueLocation = rweState.uniqueLocation;
		toroidal = rweState.toroidal;
		spaces = state.spaces;
		if(spaces == Spaces.OBJECT){
			objectSpace = state.objectSpace; 
			if(toroidal){
				mode = objectSpace.BOUNDED;
			}
			else{ 
				mode = objectSpace.TOROIDAL;
			}
		}
		else if(spaces == Spaces.SPARSE){
			sparseSpaceGrid = state.sparseSpace;
			if(toroidal){
				mode = SparseGrid2D.BOUNDED;
			}
			else{
				mode = SparseGrid2D.TOROIDAL;
			}
		}

	}

	public void setColor(SimStateSweep state, float red, float green, float blue, float opacity){
		Color c = new Color(red,green,blue,opacity);
		OvalPortrayal2D o = new OvalPortrayal2D(c);
		GUIStateSweep guiState = (GUIStateSweep)state.gui;
		switch(spaces){
		case OBJECT: guiState.agentsPortrayalObject.setPortrayalForObject(this, o);
		break;
		case SPARSE: guiState.agentsPortrayalSparseGrid.setPortrayalForObject(this, o);
		break;
		case CONTINUOUS: guiState.agentsPortrayalContnuous.setPortrayalForObject(this, o);
		}

	}	

	public int getOrientation (SimStateSweep state){
		final int orientation = orientations[dir.x+1][dir.y+1];
		if(orientation < RandomWalkMechanics.DIRECTIONS)
			return orientations[dir.x+1][dir.y+1];
		else
			return state.random.nextInt(RandomWalkMechanics.DIRECTIONS);
	}

	/**
	 * Finds a new random location to move.  If successful, it returns true, else false.  If true, it resets
	 * the x and y coordinates to the new location and set dir to a new direction vector.  It used the walkRules
	 * to determine a new random location.  If openLocation is true, then it finds a random open location if it exits.
	 * This is the most computationally expensive option since it first has to check all possible moves based on
	 * the walkRule, then from the list of empty locations, it chooses and empty location if one exists.  If openLocation
	 * = false but uniqueLocation = true, then it randomly chooses a location and tests to see if the location is empty, if
	 * so it updates x, y, and dir, else it return false. If both openLocation and uniquLocation = false, then it uppdates
	 * x,y, and dir. In all cases, x and y are correct for toroidal and bounded spaces.
	 * @param state --> Takes an instance of the RandomWalkerEnviroment or its descendants.
	 * @param space --> Takes an instance of the Object2DGridex.
	 * @return
	 */
	public boolean findLocation(SimStateSweep state, ObjectGrid2Dex space){
		ox = x;
		oy = y;
		final int orientation = getOrientation (state);
		listOfMoves = walkRules.get(orientation);
		Int2D temp = null;
		if(openLocation){//openLocation assumes unique
			possibleMoves.clear();
			for(int i=0;i<listOfMoves.length;i++){
				temp = listOfMoves[i];
				int tx = x + temp.x;
				int ty = y + temp.y;
				if(toroidal){
					tx = space.stx(tx);
					ty = space.sty(ty);
				}
				else{
					tx = bx(space,tx);
					ty = by(space,ty);
				}
				Object result = space.getObjectAtLocation(tx, ty);
				if(result == null){
					possibleMoves.add(temp);
				}
			}//end for
			if(possibleMoves.size() == 0){
				temp = null;
				//If no empty one is found, choose a new dir, which is a change in orientation
				//otherwise, agents will pile up in large numbers
				dir = listOfMoves[state.random.nextInt(listOfMoves.length)];
			}
			else if(possibleMoves.size() == 1){
				temp = possibleMoves.get(0);
			}
			else{
				temp = possibleMoves.get(state.random.nextInt(possibleMoves.size()));
			}
			if(temp!=null){
				dir = temp;
				x = x + dir.x;
				y = y + dir.y;
				if(toroidal){
					x = space.stx(x);
					y = space.sty(y);
				}
				else{
					x = bx(space,x);
					y = by(space,y);
				}
				return true;
			}
			else{
				return false;
			}

		}
		else{ //not open
			if(uniqueLocation){
				if(listOfMoves.length == 1){
					temp = listOfMoves[0];		
				}
				else{
					temp = listOfMoves[state.random.nextInt(listOfMoves.length)];
				}
				int tx = x + temp.x;
				int ty = y + temp.y;
				if(toroidal){
					tx = space.stx(tx);
					ty = space.sty(ty);
				}
				else{
					tx = bx(space,tx);
					ty = by(space,ty);
				}
				Object result = space.getObjectAtLocation(tx, ty);
				if(result == null){
					dir = temp;
					x = tx;
					y = ty;
					return true;
				}
				else{
					//If no empty one is found, choose a new dir, which is a change in orientation
					//otherwise, agents will pile up in large numbers
					dir = listOfMoves[state.random.nextInt(listOfMoves.length)];
					return false;
				}
			}
			else{//not unique
				if(listOfMoves.length == 1){
					temp = listOfMoves[0];
				}
				else{
					temp = listOfMoves[state.random.nextInt(listOfMoves.length)];
				}

				if(temp!=null){
					dir = temp;
					x = x + dir.x;
					y = y + dir.y;
					if(toroidal){
						x = space.stx(x);
						y = space.sty(y);
					}
					else{
						x = bx(space,x);
						y = by(space,y);
					}
					return true;
				}
				else{
					return false;
				}
			}
		}

	}

	/**
	 * Finds a new random location to move.  If successful, it returns true, else false.  If true, it resets
	 * the x and y coordinates to the new location and set dir to a new direction vector.  It used the walkRules
	 * to determine a new random location.  If openLocation is true, then it finds a random open location if it exits.
	 * This is the most computationally expensive option since it first has to check all possible moves based on
	 * the walkRule, then from the list of empty locations, it chooses and empty location if one exists.  If openLocation
	 * = false but uniqueLocation = true, then it randomly chooses a location and tests to see if the location is empty, if
	 * so it updates x, y, and dir, else it return false. If both openLocation and uniquLocation = false, then it uppdates
	 * x,y, and dir. In all cases, x and y are correct for toroidal and bounded spaces.
	 * @param state
	 * @return
	 */
	public boolean findLocation(SimStateSweep state, SparseGrid2Dex space){
		final int orientation = getOrientation (state);
		listOfMoves = walkRules.get(orientation);
		Int2D temp = null;
		if(openLocation){//openLocation assumes unique
			possibleMoves.clear();
			for(int i=0;i<listOfMoves.length;i++){
				temp = listOfMoves[i];
				int tx = x + temp.x;
				int ty = y + temp.y;
				if(toroidal){
					tx = space.stx(tx);
					ty = space.sty(ty);
				}
				else{
					tx = bx(space,tx);
					ty = by(space,ty);
				}
				Bag result = space.getObjectsAtLocation(tx, ty);
				if(result == null){
					possibleMoves.add(temp);
				}
			}//end for
			if(possibleMoves.size() == 0){
				temp = null;
				//If no empty one is found, choose a new dir, which is a change in orientation
				//otherwise, agents will pile up in large numbers
				dir = listOfMoves[state.random.nextInt(listOfMoves.length)];
			}
			else if(possibleMoves.size() == 1){
				temp = possibleMoves.get(0);
			}
			else{
				temp = possibleMoves.get(state.random.nextInt(possibleMoves.size()));
			}
			if(temp!=null){
				dir = temp;
				x = x + dir.x;
				y = y + dir.y;
				if(toroidal){
					x = space.stx(x);
					y = space.sty(y);
				}
				else{
					x = bx(space,x);
					y = by(space,y);
				}
				return true;
			}
			else{
				return false;
			}

		}
		else{ //not open
			if(uniqueLocation){
				if(listOfMoves.length == 1){
					temp = listOfMoves[0];		
				}
				else{
					temp = listOfMoves[state.random.nextInt(listOfMoves.length)];
				}
				int tx = x + temp.x;
				int ty = y + temp.y;
				if(toroidal){
					tx = space.stx(tx);
					ty = space.sty(ty);
				}
				else{
					tx = bx(space,tx);
					ty = by(space,ty);
				}
				Bag result = space.getObjectsAtLocation(tx, ty);
				if(result == null){
					dir = temp;
					x = tx;
					y = ty;
					return true;
				}
				else{
					//If no empty one is found, choose a new dir, which is a change in orientation
					//otherwise, agents will pile up in large numbers
					dir = listOfMoves[state.random.nextInt(listOfMoves.length)];
					return false;
				}
			}
			else{//not unique
				if(listOfMoves.length == 1){
					temp = listOfMoves[0];
				}
				else{
					temp = listOfMoves[state.random.nextInt(listOfMoves.length)];
				}

				if(temp!=null){
					dir = temp;
					x = x + dir.x;
					y = y + dir.y;
					if(toroidal){
						x = space.stx(x);
						y =space.sty(y);
					}
					else{
						x = bx(space,x);
						y = by(space,y);
					}
					return true;
				}
				else{
					return false;
				}
			}
		}

	}

	public int bx (ObjectGrid2Dex space, int x){
		if(x > space.getWidth()-1)
			return space.getWidth()-1;
		else if( x < 0){
			return 0;
		}
		else{
			return x;
		}
	}

	/**
	 * Moves y back to the boundary edge
	 * @param state
	 * @param y
	 * @return
	 */
	public int by (ObjectGrid2Dex space, int y){
		if(y > space.getHeight()-1)
			return space.getHeight()-1;
		else if( y < 0){
			return 0;
		}
		else{
			return y;
		}
	}


	/**
	 * Move y back to the boundary edge.
	 * @param state
	 * @param x
	 * @return
	 */
	public int bx (SparseGrid2Dex space, int x){
		if(x > space.getWidth()-1)
			return space.getWidth()-1;
		else if( x < 0){
			return 0;
		}
		else{
			return x;
		}
	}

	/**
	 * Moves y back to the boundary edge
	 * @param state
	 * @param y
	 * @return
	 */
	public int by (SparseGrid2Dex space, int y){
		if(y > space.getHeight()-1)
			return space.getHeight()-1;
		else if( y < 0){
			return 0;
		}
		else{
			return y;
		}
	}


	public void move(SimStateSweep state, SparseGrid2Dex space){
		if(findLocation(state, space)){//location is found
			state.sparseSpace.setObjectLocation(this, x, y);
		}
	}

	public void move(SimStateSweep state, ObjectGrid2Dex space){
		if(findLocation(state, space)){//location is found
			space.setObjectLocation(this, ox, oy, x, y);
		}
	}

	public void die(SimStateSweep state){
		if(event != null){
			event.stop();
		}
		else{
			System.out.println("Event is null");
		}
		if(spaces == Spaces.OBJECT){
			state.objectSpace.removeAgentFromLocation(this, x, y);
		}
		else if(spaces == Spaces.SPARSE){
			state.sparseSpace.remove(this);
		}
		orientations = null;
		possibleMoves = null;
		listOfMoves = null;
		walkRules = null;
		walkRule = null;
		sparseSpaceGrid = null;
		objectSpace = null;
		dir = null;
	}

	public void movement(SimStateSweep state){
		if(spaces == Spaces.OBJECT)
			move((SimStateSweep)state, objectSpace);
		else if(spaces == Spaces.SPARSE)
			move((SimStateSweep)state, sparseSpaceGrid);
	}

	public void step(SimState state) {
		movement((SimStateSweep)state);
	}
}