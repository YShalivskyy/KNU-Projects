package com.ys.tasks.PriorityQueue20;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by PC on 12/26/2016.
 */
public class QueueHandlerPriority20Test {
    @Test
    public void mainTest() throws Exception {
        QueueHandlerPriority q = new QueueHandlerPriority();
        String[] args = {"args"};
        QueueHandlerPriority.main(args);
        assertTrue(QueueHandlerPriority.counter == 0);
    }

}