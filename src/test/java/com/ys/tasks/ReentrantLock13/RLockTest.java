package com.ys.tasks.ReentrantLock13;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by PC on 12/22/2016.
 */
public class RLockTest {
    @Test
    public void testLock() throws Exception {
        RLock rlock = new RLock();
        String[] args = {"args"};
        rlock.main(args);
        assertTrue(rlock.mainCounter == 645);
    }

}