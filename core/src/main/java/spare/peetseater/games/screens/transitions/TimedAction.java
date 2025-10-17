package spare.peetseater.games.screens.transitions;

import java.util.function.Supplier;

public class TimedAction {
    private float accum;
    private final Supplier<Boolean> action;
    private float period;
    private boolean done;

    public TimedAction(float period, Supplier<Boolean> action) {
        this.period = period;
        this.accum = 0;
        this.action = action;
        this.done = false;
    }

    public void update(float secondsSince) {
        accum += secondsSince;
        if (accum > period) {
            accum -= period;
            done = action.get();
        }
    }

    public boolean isDone() {
        return done;
    }
}
