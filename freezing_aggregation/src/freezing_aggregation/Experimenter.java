package freezing_aggregation;

import observer.Observer;
import sim.engine.SimState;
import sim.util.Bag;
import sweep.ParameterSweeper;
import sweep.SimStateSweep;

public class Experimenter extends Observer {

    public Experimenter(String fileName, String folderName, SimStateSweep state, ParameterSweeper sweeper,
                        String precision, String[] headers) {
        super(fileName, folderName, state, sweeper, precision, headers);
    }

    public void numberOfFrozenAgents(Environment state) {
        Bag agents = state.sparseSpace.getAllObjects();
        int frozenCount = 0;

        for (int i = 0; i < agents.numObjs; i++) {
            Agent a = (Agent) agents.objs[i];
            if (a.frozen) {
                frozenCount++; // Count frozen agents
            }
        }

        double time = (double) state.schedule.getTime(); // Get the current simulation time
        this.upDateTimeChart(0, time, frozenCount, true, 1000); // Update the chart (1000 ms delay for visualization)
    }

    @Override
    public void step(SimState state) {
        super.step(state);
        if (step % this.state.dataSamplingInterval == 0) { // Record data at sampling intervals
            numberOfFrozenAgents((Environment) state);
        }
    }
}
