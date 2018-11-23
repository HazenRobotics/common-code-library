package com.hazenrobotics.commoncode.models.conditions;

import com.hazenrobotics.commoncode.interfaces.IdleInterface;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConditionTest {

    private Condition condition1;
    private Condition condition2;
    private boolean truth;
    private int count;

    @Before
    public void setUp() {
        truth = false;
        condition1 = new Condition() {
            @Override
            protected boolean condition() {
                return truth;
            }
        };
        condition2 = new Condition(false) {
            @Override
            protected boolean condition() {
                return truth;
            }
        };
        count = 0;
    }

    @Test
    public void isTrue() {
        assertFalse(condition1.isTrue());
        truth = true;
        assertTrue(condition1.isTrue());
        assertTrue(condition2.isTrue());
        truth = false;
        //Checking to see if it remembered that it was true
        assertTrue(condition1.isTrue());
        //Or doesn't set to not in constructor
        assertFalse(condition2.isTrue());
    }

    @Test
    public void doUntilTrue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                count++;
                if (count >= 10)
                    truth = true;
            }
        };
        condition1.doUntilTrue(runnable);
        assertEquals(10, count);
    }

    @Test
    public void idleUntilTrue() {
        IdleInterface idleInterface = new IdleInterface() {
            @Override
            public void idle() {
                count++;
                if (count >= 10)
                    truth = true;
            }

            @Override
            public void sleep(long milliseconds) {
                idle();
            }
        };
        condition1.idleUntilTrue(idleInterface);
        assertEquals(10, count);
    }
}