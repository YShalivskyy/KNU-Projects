package com.ys.tasks.ReadWriteLock10;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by PC on 12/19/2016.
 */
public class ReadWriteLockHandlerTest {
    @Test
    public void testWriteLock() throws Exception {
        ReadWriteLockHandler h = new ReadWriteLockHandler();
        String[] args = {"args"};
        h.main(args);
        assertTrue(h.writeCounter == 3);
    }

}