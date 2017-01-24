package com.ys.tasks.PoolCallable19;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by PC on 12/19/2016.
 */
public class ThreadPoolHandlerTest {
    @Test
    public void testCalculation() throws Exception {
        ThreadPoolHandler handler = new ThreadPoolHandler();
        String[] args = new String[]{"args"};
        handler.main(args);
        System.out.println(ThreadPoolHandler.iterationsCounter);
        assertTrue(ThreadPoolHandler.iterationsCounter == 10);
    }

}