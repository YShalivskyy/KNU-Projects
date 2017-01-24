package com.ys.tasks.Queue16;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by PC on 12/15/2016.
 */
public class QueueHandlerPriorityTest {
    @Test
    public void testHandler() throws Exception {
        QueueHandler handler = new QueueHandler();
        String[] args = {"args"};
        QueueHandler.main(args);
        assertTrue(QueueHandler.queue.isEmpty());
    }

}