package freezing_aggregation;

import spaces.Spaces;
import sweep.SimStateSweep;

public class Environment extends SimStateSweep {
    public int n = 2000; // Number of agents
    public double p = 1.0; // Probability of movement
    public boolean broadRule = true; // True for broad rule, false for narrow rule
    public boolean bounded = true; // True for bounded space, false for toroidal space

    public Environment(long seed, Class observer) {
        super(seed, observer);
    }

    public int getN() { return n; }
    public void setN(int n) { this.n = n; }

    public double getP() { return p; }
    public void setP(double p) { this.p = p; }

    public boolean isBroadRule() { return broadRule; }
    public void setBroadRule(boolean broadRule) { this.broadRule = broadRule; }

    public boolean isBounded() { return bounded; }
    public void setBounded(boolean bounded) { this.bounded = bounded; }

    public void makeAgents() {
        if (n > (gridWidth * gridHeight)) {
            System.out.println("Too many agents! Reduce n or allow multiple agents per cell.");
            return;
        }

        // Create and place the initial frozen agent at the center.
        int centerX = gridWidth / 2;
        int centerY = gridHeight / 2;
        Agent frozenAgent = new Agent(true, centerX, centerY, 0, 0);
        sparseSpace.setObjectLocation(frozenAgent, centerX, centerY);
        schedule.scheduleRepeating(frozenAgent);

        // Create remaining (n - 1) non-frozen agents at random locations.
        for (int i = 1; i < n; i++) {
            int x = random.nextInt(gridWidth);
            int y = random.nextInt(gridHeight);

            while (sparseSpace.getObjectsAtLocation(x, y) != null) {
                x = random.nextInt(gridWidth);
                y = random.nextInt(gridHeight);
            }

            int dirx = random.nextInt(3) - 1;
            int diry = random.nextInt(3) - 1;
            Agent a = new Agent(false, x, y, dirx, diry);
            sparseSpace.setObjectLocation(a, x, y);
            schedule.scheduleRepeating(a);
        }
    }

    public void start() {
        super.start();
        spaces = Spaces.SPARSE;
        this.make2DSpace(spaces, gridWidth, gridHeight);
        makeAgents();

        if (observer != null) {
            observer.initialize(sparseSpace, spaces); // Initialize the Experimenter
        }
    }
}

