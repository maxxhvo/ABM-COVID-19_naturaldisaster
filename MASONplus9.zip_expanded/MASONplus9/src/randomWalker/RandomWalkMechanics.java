package randomWalker;

import java.util.ArrayList;
import sim.engine.SimState;
import sim.util.*;

/**
 * 
 * For more theoretical background on the motivation this class see
 * 
 * Smaldino, P. E. and Schank, J. C. (2012) Movement patterns, social dynamics, and the evolution of cooperation.
 * Theoretical Population Biology, 82: 48-58.
 * 
 * Consider an agent  located in the center cell C surround by 8 cells, it can move into with a one-step move:
 * 
 *  <pre>  
 *              _____________________
 *              |      |      |      | 
 *              |  NW  |  N   |  NE  |
 *              |______|______|______|
 *              |      |      |      | 
 *              |  W   |  C   |  E   |
 *              |______|______|______|
 *              |      |      |      | 
 *              |  SW  |  S   |  SE  |
 *              |______|______|______|
 *  </pre>
 * There are eight possible directions the agent can move and we can create a probability matrix for how likely 
 * it is to randomly move into one of the 8 adjacent cells:
 * <pre>
 *               _____________________
 *              |      |      |      | 
 *              |  .5  |  0   |  .5  |
 *              |______|______|______|
 *              |      |      |      | 
 *              |  0   |  C   |  0   |
 *              |______|______|______|
 *              |      |      |      | 
 *              |  0   |  0   |  0   |
 *              |______|______|______|
 *</pre>
 * For this probability matrix, an agent has an equal probability of moving either to the NW (northwest)
 * or NE (northeast).  This agent would randomly zizag through space.
 * 
 * If we wanted our agent to only move forward, then our probability matrix would look like the figure below, which is
 * one of eight deterministic walks.
 * 
 *  <pre>
 *              _____________________
 *              |      |      |      | 
 *              | 0    | 1.0  |  0   |
 *              |______|______|______|
 *              |      |      |      | 
 *              |  0   |  C   |  0   |
 *              |______|______|______|
 *              |      |      |      | 
 *              |  0   |  0   |  0   |
 *              |______|______|______|
 *              
 *   </pre>      
 * A "Brownian motion" particle would result form the probability matrix:
 *<pre>
 *               _____________________
 *              |      |      |      | 
 *              | .125 | .125 | .125 |
 *              |______|______|______|
 *              |      |      |      | 
 *              | .125 |  C   | .125 |
 *              |______|______|______|
 *              |      |      |      | 
 *              | .125 | .125 | .125 |
 *              |______|______|______|
 *              
 * </pre>
 * A convenient way to represent these 3 x 3 square matrices is to map them into vectors. The
 * directions can be represented as
 * <pre>
 *   ________________________________________________________________________  
 *   |       |       |       |       |       |       |       |       |       |
 *   |   N   |   NE  |   E   |   SE  |   S   |   SW  |   W   |   NW  |   C   |
 *   |_______|_______|_______|_______|_______|_______|_______|_______|_______|
 *   </pre>
 *   For zigzag, the probabilities of moving northeast or northwest are:
 *   <pre>
 *   ________________________________________________________________________  
 *   |       |       |       |       |       |       |       |       |       |
 *   |  .5   |   .0  |  .0   |   .0  |  .0   |   .0  |  .0   |  .5   |   C   |
 *   |_______|_______|_______|_______|_______|_______|_______|_______|_______|
 *       ^                                                       ^                  
 *   <pre>
 *   Given this probability vector, there are only two possible directions of movement above,
 *   which correspond to the one-step direction vectors below:
 *   <pre>
 *   ________________________________________________________________________  
 *   |       |       |       |       |       |       |       |       |       |
 *   | (0,1) | (1,1) | (1,0) |(1,-1) | (0,-1)|(-1,-1)| (-1,0)|(-1,1) | (0,0) |
 *   |_______|_______|_______|_______|_______|_______|_______|_______|_______|
 *       ^                                                       ^
 * </pre>
 *   Given that there are eight local cells available to move into, the number of possible one-step movement
 *   rules are 2^8 = 256.  We can take advantage of the mathematical fact that all possible movement rules can
 *   be represented by the conversion of integers 0 to 255 into their binary representations. For example,
 *   the binary representation of 65 is 01000001.  Starting with the first 0 or 1, we map it to the north
 *   position and so on, yielding:
 * <pre>
 *   ________________________________________________________________________  
 *   |       |       |       |       |       |       |       |       |       |
 *   |   0   |   1   |   0   |   0   |   0   |   0   |   0   |   1   |   C   |
 *   |_______|_______|_______|_______|_______|_______|_______|_______|_______|
 *   </pre>
 *   We could divide each position by 2 to get the probability vector above, or for computational efficiency, we
 *   could simply calculate the cumulative sum:
 *   <pre>
 *   i = 1
 *   directions[i] = directions[i] + directions[i-1]
 *   
 *   directions[C] = total
 *   </pre>
 *   which would yield
 * <pre>
 *   ________________________________________________________________________  
 *   |       |       |       |       |       |       |       |       |       |
 *   |   0   |   1   |   1   |   1   |   1   |   1   |   1   |   2   |   2   |
 *   |_______|_______|_______|_______|_______|_______|_______|_______|_______|
 *                                                               ^
 *   </pre>
 *   We can then generate a pseudo random number in the range [0,2) (we use the 8th slot (C) to store the total value.
 *   For example, we might generate the number 1.37829.  For cell i (i = 0..7), when 1.37829 < directions[i], we choose
 *   that cell.  As indicated above, this would correspond to position 7.  We then use i = 7, to obtain the direction
 *   vector from the vector below:
 *   <pre>
 *   ________________________________________________________________________  
 *   |       |       |       |       |       |       |       |       |       |
 *   | (0,1) | (1,1) | (1,0) |(1,-1) | (0,-1)|(-1,-1)| (-1,0)|(-1,1) | (0,0) |
 *   |_______|_______|_______|_______|_______|_______|_______|_______|_______|
 *         </pre>                                                      ^
 *   which is (-1,1) and the agent will move one step in the northwest direction.
 *   
 *   One issue remains.  This handles the case where an agent is facing north, but how do we determine a random
 *   movement strategy when an agent is oriented another direction?
 *   
 *   First, we need to determine its orientation, which can be done from its direction vector (x,y).
 *   If we add 1 to x and y: x+1 and y+1 then we map the direction vectors into directions as shown below.
 *   <pre>
 *              _____________________
 *              |      |      |      | 
 *              |   5  |   6  |   7  |
 *              |______|______|______|
 *              |      |      |      | 
 *              |   4  |  8   |  0   |
 *              |______|______|______|
 *              |      |      |      | 
 *              |  3   |   2  |  1   |
 *              |______|______|______|
 * </pre>
 *  We then take the orientation and use it to rotate the directions as follows: new direction vector = 
 *  (orientation + direction) % 8 (where % is the remainder function.
 * 
  */

public class RandomWalkMechanics  {
	/*
	 * First part generates walk rules and the second part uses them.
	 */
	public final static int NUMBEROFWALKRULES = 256;
	public final static int[] classicRules ={193,65,162,255,34,170,20,96,24};
	public final static String[] classicRuleNames = {"Rule 193: Speedster", "Zigzag", "Forward-Left-Right", "Brownian", "Sidestep", "von Neuman", "Close-to-Home", "Cyclone", "Rule 24: Tail Chaser"};
	public final static int[] antiClassicRules ={62,190,93,0,221,85,235,159,231};
	public final static int[] deterministicRules ={128,64,32,16,8,4,2,1};
	public final static int DIRECTIONS = 8;
	
	public static double[] initRule(int rule) {
		double[] directions = new double[DIRECTIONS]; //Rule matrix

		for (int i = 0; i < 8; i++) {
			directions[7-i] =rule>>i & 1;
		}
		
		return directions;
	}  
    
    /** 
     * This method returns a random walk rule in RandomWalkRules, which contains a double[][] walk rule
	 * and its name. The double[][] rule is accessed from a walkRule by walkRule.rule and
	 * the name by walkRule.name.  It uses the pseudo random number generator provided in the class Math.
	 * An array of all the rules is fetched and then a random integer index is generated within the 
	 * length of the array of random walk rules.  The walk rule corresponding to that index is returned.
	 * 
     * @return
     */
    public static double[] getRandomWalk(){
    	int i = (int) (Math.random() * NUMBEROFWALKRULES);
    	return initRule(i);
    }
    
    /**
     * This method returns a random walk rule in RandomWalkRules, which contains a double[][] walk rule
	 * and its name. The double[][] rule is accessed from a walkRule by walkRule.rule and
	 * the name by walkRule.name.  It uses the pseudo random number generator provided in Mason's SimState class.
	 * An array of all the rules is fetched and then a random integer index is generated within the 
	 * length of the array of random walk rules.  The walk rule corresponding to that index is returned.
	 * 
     * @param state
     * @return
     */
    public static double[] getRandomWalk(SimState state){
    	int i = state.random.nextInt(NUMBEROFWALKRULES);
    	return initRule(i);
    }
    
    public static String getRuleName(int rule){
    	String s = Integer.toBinaryString(rule);
    	if(s.length() == 1)
    		s = "0000000"+s;
    	if(s.length() == 2)
    		s = "000000"+s;
    	if(s.length() == 3)
    		s = "00000"+s;
    	if(s.length() == 4)
    		s = "0000"+s;
    	if(s.length() == 5)
    		s = "000"+s;
    	if(s.length() == 6)
    		s = "00"+s;
    	if(s.length() == 7)
    		s = "0"+s;
    	return "Rule" +rule+" "+s;
    }
    
    public static double[] getWalkRule(String rule){
    	if(rule.length() != 8){
    		System.err.println("String not the correct length.");
    		return null;
    	}
    	double[] d = new double[8];
    	for(int i=0;i<rule.length();i++){
    		int v = Character.getNumericValue(rule.charAt(i));
    		if(v == 0){
    			d[i]=0;
    		}
    		else if(v == 1){
    			d[i]=1;
    		}
    		else{
    			System.err.println("Something is wrong with the string.");
    			return null;
    		}
    	}
    	return d;
    }

    /**
     * This method returns an array of class String of the names of the random
     * walk rules in RandomWalkRules.
     * 
     * @return
     */
    public static String[] names(){
    	
    	String[] s = new String[NUMBEROFWALKRULES];
    	for(int i=0;i<NUMBEROFWALKRULES;i++){
    		s[i]=getRuleName(i);
    	}
    	return s;
    }
    
    /**
     * This method returns an ArrayList of type double[][] for all of the
     * random walk rules in RandomWalkRules.
     * 
     * @return
     */

    public static ArrayList<double[]> getRules(){
    	ArrayList<double[]> listOfRules = new ArrayList<double[]>(NUMBEROFWALKRULES);
    	for(int i=0;i<NUMBEROFWALKRULES;i++){
    		listOfRules.add(initRule(i));
    	}
    	return listOfRules;
    } 
    
    /**
     * This method return the walk rules with a specified numerical range of start to finish.
     * The method checks to see if start and finish are within the range of rules.  If they are
     * not, it returns null with a warning.
     * 
     * @param start
     * @param finish
     * @return
     */
    
    public static ArrayList<double[]> getRulesRange(int start, int finish){
    	if(start < 0 && start < finish && finish > NUMBEROFWALKRULES){
    		System.err.println("Can't start at less than 0, start < finish, or you can't ask for more rules than there are!");
    		return null; //Can't start at less than 0, start < finish, and can't ask for more rules than there are
    	}
    	ArrayList<double[]> listOfRules = new ArrayList<double[]>(finish - start +1);
    	for(int i = start;i<=finish;i++){
    		listOfRules.add(initRule(i));
    	}
    	return listOfRules;
    }
    
    /**
     * This method returns the named rules in
     * Smaldino, P. E. and Schank, J. C. (2012) Movement patterns, social dynamics, and the evolution of cooperation.
     * Theoretical Population Biology, 82: 48-58.
     * @return
     */

    public static ArrayList<double[]> getClassicRules(){
    	ArrayList<double[]> listOfRules = new ArrayList<double[]>(classicRules.length);
    	for(int i=0; i<classicRules.length;i++)
    		listOfRules.add(initRule(classicRules[i]));
    	return listOfRules;
    }
    
    /**
     * This method returns the complements of named rules in
     * Smaldino, P. E. and Schank, J. C. (2012) Movement patterns, social dynamics, and the evolution of cooperation.
     * Theoretical Population Biology, 82: 48-58.
     * 
     * @return
     */

    public static ArrayList<double[]> getAntiClassicRules(){
    	ArrayList<double[]> listOfRules = new ArrayList<double[]>(antiClassicRules.length);
    	for(int i=0; i<antiClassicRules.length;i++)
    		listOfRules.add(initRule(antiClassicRules[i]));
    	return listOfRules;
    }
    
    /**
     * This method returns the 8 deterministic rules.
     * 
     * @return
     */

    public static ArrayList<double[]> getDeterministicRules(){
    	ArrayList<double[]> listOfRules = new ArrayList<double[]>(deterministicRules.length);
    	for(int i=0; i<deterministicRules.length;i++)
    		listOfRules.add(initRule(deterministicRules[i]));
    	return listOfRules;
    }
    
    /*
     * Use walk rules
     */

    public final static int[][] orientationLookUp = {
 		{5,6,7},
 		{4,8,0},
 		{3,2,1}};
 	
 	public final static Int2D[] directionLookUp = {
 		new Int2D(0,1), //north
 		new Int2D(1,1), //northeast
 		new Int2D(1,0), //east
 		new Int2D(1,-1), //southeast
 		new Int2D(0,-1), //south
 		new Int2D(-1,-1), //southwest
 		new Int2D(-1,0), //west
 		new Int2D(-1,1), //northwest
 		new Int2D(0,0) //home, nonemovement
 	};


	public final int lookUpOrientation(final int xdir, final int ydir) {
		return orientationLookUp[xdir+1][ydir+1];
	}
	
    public static int rotate(int orientation, int direction){
    	return (orientation + direction) % 8;
    }


	public final Int2D changeDirection(final int cell) {
		return directionLookUp[cell];
	}
	
	public final int findDirection(SimState state, double[] walkRule) {
		final double x = state.random.nextDouble() * walkRule[walkRule.length-1];

		for(int i=0; i<walkRule.length;i++)
				if(x < walkRule[i]){	
					return i;
				}
		return walkRule.length-1;
		}

	public static ArrayList<Int2D[]> getRules(double[] walkRule){
		ArrayList<Int2D[]> list = new ArrayList<Int2D[]>(8);
		int z = 0;
		for(int x=0;x<walkRule.length;x++){
			if(walkRule[x]>0)
				z++;
		}
		
		for(int i=0;i<walkRule.length;i++){
			Int2D[] next = new Int2D[z]; //number of 1s in the walkRule
			int k = 0;
			for(int j=0;j<walkRule.length;j++){
				int slot = rotate(i,j);//i is the orientation
				if(walkRule[j]>0){
					next[k] = directionLookUp[slot];
					k++;
				}
			}//end j
			list.add(next);
		}
		
		return list;
	}
	
	public static void printList (ArrayList<Int2D[]> list){
		for(int i = 0; i< list.size();i++){
			int k = (list.get(i)).length;
			for(int j=0;j<k;j++){
				System.out.print((list.get(i))[j] + " ");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		/*double[] x = {0,1,0,0,0,0,0,1};
		ArrayList<Int2D[]> list = getRules(x);
		for(int i = 0; i< list.size();i++){
			int k = (list.get(i)).length;
			for(int j=0;j<k;j++){
				System.out.print((list.get(i))[j] + " ");
			}
			System.out.println();
		}*/
		
		 ArrayList<double[]> list = getRulesRange(0, 255);
		 for (int i=0;i< list.size();i++) {
		   for(int j=0; j<list.get(i).length;j++) {
			   double[] d = list.get(i);
			   System.out.print(d[j]+", ");
		   }
		   System.out.println();
		 }
		
	}
	

}
