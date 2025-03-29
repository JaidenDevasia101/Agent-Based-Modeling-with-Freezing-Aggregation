package freezing_aggregation;

import java.awt.Color;
import spaces.Spaces;
import sweep.GUIStateSweep;
import sweep.SimStateSweep;

public class GUIfreezing extends GUIStateSweep {

    public GUIfreezing(SimStateSweep state, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
                        boolean agentPortrayal) {
        super(state, gridWidth, gridHeight, backdrop, agentDefaultColor, agentPortrayal);
    }

    public static void main(String[] args) {
        // Time series chart setup
        String[] title = {"Agents Frozen"}; // Chart title
        String[] x = {"Time Steps"}; // X-axis label
        String[] y = {"Number"}; // Y-axis label

        // Create time series chart for tracking frozen agents
        GUIfreezing.initializeArrayTimeSeriesChart(1, title, x, y);

        // Initialize simulation environment with Experimenter
        GUIfreezing.initialize(Environment.class, Experimenter.class, GUIfreezing.class, 
                               1000, 1000, Color.WHITE, Color.BLUE, true, Spaces.SPARSE);
    }
}
