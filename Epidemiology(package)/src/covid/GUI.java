package covid;

import java.awt.Color;
import spaces.Spaces;
import sweep.GUIStateSweep;
import sweep.SimStateSweep;

public class GUI extends GUIStateSweep {

	public GUI(SimStateSweep state, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor, boolean agentPortrayal) {
		super(state, gridWidth, gridHeight, backdrop, agentDefaultColor, agentPortrayal);
	}
	public GUI(SimStateSweep state) {
		super(state);
		// TODO Auto-generated constructor stub
	}
	
	
//TODO implement color s
	public static void main(String[] args) {
		//TODO GUI.initialize(Environment.class,Experimenter.class, AgentsGUI.class, )
				
	}
}

//TODO time-series charts?