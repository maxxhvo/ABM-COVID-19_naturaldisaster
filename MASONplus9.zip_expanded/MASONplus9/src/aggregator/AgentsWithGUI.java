package aggregator; 

import java.awt.Color;

import observer.Observer;
import spaces.Spaces;
import sweep.SimStateSweep;
import sweep.GUIStateSweep;

public class AgentsWithGUI extends GUIStateSweep {
	

	public static void main(String[] args) {
		initialize (Environment.class,Observer.class,AgentsWithGUI.class, 400, 400, Color.WHITE, Color.BLUE,true,Spaces.SPARSE);
	}

	public AgentsWithGUI(SimStateSweep state, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal) {
		super(state, gridWidth, gridHeight, backdrop, agentDefaultColor, agentPortrayal);
	}
}
