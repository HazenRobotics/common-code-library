package com.hazenrobotics.commoncode.models.conditions;

import com.hazenrobotics.commoncode.interfaces.IdleInterface;

/**
 * A Condition an abstract class that is used to tell a function when some parameter has been met.
 * The user can either define their own condition as an anonymous class, or use one of the many subclasses that extend it
 * @see RangeDistance
 * @see Timer
 * @see GyroAngle
 * @see ColorMatch
 */
@SuppressWarnings("unused,WeakerAccess")
public abstract class Condition {
    private boolean wasTrue;
    private boolean rememberTrue;

    public Condition() {
        this(true);
    }

    public Condition(boolean rememberTrue) {
        this.rememberTrue = rememberTrue;
        wasTrue = false;
    }
    /**
     * This abstract function can be overridden to allow a custom condition to be made
     * @return The result of the condition operation
     */
    protected abstract boolean condition();

    /**
     * This is the control function for a condition
     * @return If the condition is true/the object it in a true state
     */
    public boolean isTrue() {
        /*
        Equivalent to code below, only works because of || operator short-circuiting, where if the
        first argument is true, the second is never checked (so therefore "wasTrue" is not updated.)
         */
        return wasTrue && rememberTrue || (wasTrue = condition());
        /*
        if (wasTrue && rememberTrue) {
            return true;
        } else {
            wasTrue = condition();
            return wasTrue;
        }
        */
    }

    /**
     * Continually runs something until this condition {@link Condition#isTrue()}
     * @param runnable What will be run
     */
    public void doUntilTrue(Runnable runnable) {
        while (!isTrue()) {
            runnable.run();
        }
    }

    /**
     * Continually idles until this condition {@link Condition#isTrue()}
     * @param idleInterface The interface through which idling can occur
     */
    public void idleUntilTrue(final IdleInterface idleInterface) {
        doUntilTrue(new Runnable() {
            @Override
            public void run() {
                idleInterface.idle();
            }
        });
    }
}
