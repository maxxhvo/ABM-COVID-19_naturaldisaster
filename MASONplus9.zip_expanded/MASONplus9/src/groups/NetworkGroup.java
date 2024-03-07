package groups;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.field.network.Edge;
import sim.field.network.Network;
import sim.util.Bag;
import spaces.Spaces;

public class NetworkGroup extends Network implements Steppable {
	int x;
	int y;
	public int size = 0;
	public Stoppable event = null;
	public static int schedule = 2;
	public Groups gDynamics = Groups.FISSION_EXTINCTION;//FISSION_FUSION,FISSION_MIGRATION, FISSION_EXTINCTION
	public Spaces spaces;
	Bag workbag = new Bag();

	public NetworkGroup() {
		super();
	}

	public NetworkGroup(boolean directed) {
		super(directed);
	}

	public NetworkGroup(Network other) {
		super(other);
	}

	public Bag getMembers() {
		return this.allNodes;
	}

	public void addMembers(Bag members) {
		if(members == null || members.numObjs==0) return; //If null or empty stop
		for(int i=0;i<members.numObjs;i++) {
			this.addNode(members.objs[i]);
		}
	}

	public boolean addConnection(final Object from, final Object to, final Object info) {
		Object o = this.getEdge(from, to);
		if(o != null) {
			return false;
		}
		else {
			this.addEdge(from, to, info);
			return true;
		}
	}

	/**
	 * This method assume an undirected graph.  It creates a network in which there are a
	 * minimum of min_K connections from each node to other nodes.  Connections are made at random.
	 * @param state
	 * @param agents
	 * @param min_K
	 * @param info
	 */
	public void randomNetworkMinK(SimState state, Bag agents, int min_K, final Object info) {
		Bag bag = new Bag();
		if(agents == null || agents.numObjs < 2) {
			System.out.println("Random network could not be constructed");
			return;
		}
		for(int i=0;i<agents.numObjs;i++) {
			this.addNode(agents.objs[i]);
		}
		Bag nodes =getAllNodes();
		for(int i=0; i<nodes.numObjs;i++) {
			Object from = nodes.objs[i];
			Bag edges = getEdges(from, bag);
			int done = edges.numObjs;
			while(done < min_K) {
				Object to = nodes.objs[state.random.nextInt(nodes.numObjs)];
				if(!from.equals(to) && addConnection(from,to, info)) {
					done++;
				}
			}
		}
	}
	
	/**
	 * This method constructs a random network with mean_K connections.  The mean number of connections can be
	 * any real number.  The method does not guarantee that the network will have a mean number of connections per
	 * node that is exactly mean_K, but on average, the mean number of connections will be mean_K.
	 * @param state
	 * @param agents
	 * @param mean_K
	 * @param info
	 */

	public void randomNetworkMeanK(SimState state, Bag agents, double mean_K, final Object info) {
		Bag bag = new Bag();
		if(agents == null || agents.numObjs < 2) {
			System.out.println("Random network could not be constructed.");
			return;
		}
		if(mean_K > agents.numObjs) {
			System.out.println("Mean number of connections per node is greater than the number of nodes.");
			return;
		}
		double connections = ((double)mean_K*(double)agents.numObjs)/2;
		int c = (int)connections;
		double remainder = connections - c;
		for(int i=0; i< agents.numObjs;i++) {
			this.addNode(agents.objs[i]);
		}
		Bag theNodes = this.allNodes;
		int start = 0;
		while(start < c) {
			Object to = theNodes.objs[state.random.nextInt(allNodes.numObjs)];
			Object from = theNodes.objs[state.random.nextInt(allNodes.numObjs)];
			while(to.equals(from)) {//make sure not the same
				to = theNodes.objs[state.random.nextInt(allNodes.numObjs)];
				from = theNodes.objs[state.random.nextInt(allNodes.numObjs)];
			}
			while(!addConnection(from,to, info)) {//connect if not connected otherwise tryu again
				to = theNodes.objs[state.random.nextInt(allNodes.numObjs)];
				from = theNodes.objs[state.random.nextInt(allNodes.numObjs)];
			}
			start++;
		}

		if(remainder > 0 && state.random.nextBoolean(remainder)) { //take a look at this
			Object to = theNodes.objs[state.random.nextInt(allNodes.numObjs)];
			Object from = theNodes.objs[state.random.nextInt(allNodes.numObjs)];
			while(to.equals(from)) {
				to = theNodes.objs[state.random.nextInt(allNodes.numObjs)];
				from = theNodes.objs[state.random.nextInt(allNodes.numObjs)];

			}
			while(!addConnection(from,to, info)) {//connect if not connected otherwise tryu again
				to = theNodes.objs[state.random.nextInt(allNodes.numObjs)];
				from = theNodes.objs[state.random.nextInt(allNodes.numObjs)];
			}
		}
	}
	
	public void checkMeanConnectivity(SimState state, double mean_K, final Object info) {
		Bag nodes = getAllNodes();
		int count = 0;
		for(int i=0;i< nodes.numObjs;i++) {
			Object node = nodes.objs[i];
			getEdges(node, workbag);
			count += workbag.numObjs;
			}
		double meanK = count/2.0;
		if(meanK >= mean_K) return;
		double connections = ((double)mean_K*(double)nodes.numObjs)/2;
		int c = (int)connections;
		double remainder = connections - c;
		int start = count;
		while(start < c) {
			Object to = nodes.objs[state.random.nextInt(allNodes.numObjs)];
			Object from = nodes.objs[state.random.nextInt(allNodes.numObjs)];
			while(to.equals(from)) {//make sure not the same
				to = nodes.objs[state.random.nextInt(allNodes.numObjs)];
				from = nodes.objs[state.random.nextInt(allNodes.numObjs)];
			}
			while(!addConnection(from,to, info)) {//connect if not connected otherwise try again
				to = nodes.objs[state.random.nextInt(allNodes.numObjs)];
				from = nodes.objs[state.random.nextInt(allNodes.numObjs)];
				while(to.equals(from)) {//make sure not the same
					to = nodes.objs[state.random.nextInt(allNodes.numObjs)];
					from = nodes.objs[state.random.nextInt(allNodes.numObjs)];
				}
			}
			start++;
		}

	}


	public void step(SimState state) {
	}

	public static void main(String[] args) {
		NetworkGroup ng = new NetworkGroup(false);
		Bag agents = new Bag();
		for(int i=0;i<10;i++) {
			agents.add(i+1);
		}
		SimState state = new SimState(System.currentTimeMillis());
		ng.randomNetworkMeanK(state, agents, 3 , null);
		Bag nodes = ng.allNodes;
			int count = 0;
		for(int i=0;i<nodes.numObjs;i++) {
			Object node = nodes.objs[i];
			Bag edges = ng.getEdges(node,null);
			count += edges.numObjs;
			System.out.print(node + "  ");
			for(int j=0;j<edges.numObjs;j++) {
				System.out.print(edges.objs[j] +"  ");
				Edge e = (Edge)edges.objs[j];
				if(e.to().equals(e.from()))
					System.out.println("TRUE");
			}
			System.out.println();
		}
		
		//System.out.println((double)count/(double)nodes.numObjs);
		/*SimState state = new SimState(System.currentTimeMillis());
		Bag agents = new Bag();
		Bag nodes =null;
		
		for(int i=0;i<10;i++) {
			agents.add(i+1);
		}
		int count = 0;
		for(int j=0; j<1000;j++) {
			NetworkGroup ng = new NetworkGroup(false);
			ng.randomNetworkMeanK(state, agents, 2.75 , null);
			nodes = ng.allNodes;
			for(int i=0;i<nodes.numObjs;i++) {
				Object node = nodes.objs[i];
				Bag edges = ng.getEdges(node,null);
				count += edges.numObjs;
			}
		}
		System.out.println((double)count/((double)nodes.numObjs*1000));*/
	}

}
