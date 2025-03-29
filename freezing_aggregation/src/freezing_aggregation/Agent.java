package freezing_aggregation;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;

public class Agent implements Steppable {
    boolean frozen = false;
    int x, y;
    int dirx, diry;

    public Agent(boolean frozen, int x, int y, int dirx, int diry) {
        this.frozen = frozen;
        this.x = x;
        this.y = y;
        this.dirx = dirx;
        this.diry = diry;
    }

    public void move(Environment state) {
        if (frozen) return; // Frozen agents do not move.

        if (state.random.nextBoolean(state.p)) {
            dirx = state.random.nextInt(3) - 1;
            diry = state.random.nextInt(3) - 1;
        }

        placeAgent(state);
    }

    public void placeAgent(Environment state) {
        int tempx, tempy;

        if (state.bounded) {
            tempx = bx(x + dirx, state);
            tempy = by(y + diry, state);
        } else {
            tempx = state.sparseSpace.stx(x + dirx);
            tempy = state.sparseSpace.sty(y + diry);
        }

        Bag b = state.sparseSpace.getObjectsAtLocation(tempx, tempy);

        if (b == null) {
            x = tempx;
            y = tempy;
            state.sparseSpace.setObjectLocation(this, x, y);
        }

        if (state.broadRule) {
            if (state.bounded) {
                b = state.sparseSpace.getMooreNeighbors(x, y, 1, state.sparseSpace.BOUNDED, false);
            } else {
                b = state.sparseSpace.getMooreNeighbors(x, y, 1, state.sparseSpace.TOROIDAL, false);
            }
            testFrozen(state, b);
        } else if (b != null) {
            testFrozen(state, b);
        }
    }

    public void testFrozen(Environment state, Bag neighbors) {
        if (neighbors == null) return;

        for (Object obj : neighbors) {
            Agent neighbor = (Agent) obj;
            if (neighbor.frozen) {
                frozen = true;
                return;
            }
        }
    }

    public int bx(int x, Environment state) {
        if (x < 0) return 0;
        if (x >= state.gridWidth) return state.gridWidth - 1;
        return x;
    }

    public int by(int y, Environment state) {
        if (y < 0) return 0;
        if (y >= state.gridHeight) return state.gridHeight - 1;
        return y;
    }

    @Override
    public void step(SimState state) {
        if (frozen) return;
        move((Environment) state);
    }
}
