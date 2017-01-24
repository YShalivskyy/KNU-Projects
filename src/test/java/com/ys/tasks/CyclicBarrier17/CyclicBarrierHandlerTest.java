package com.ys.tasks.CyclicBarrier17;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by PC on 12/12/2016.
 */
public class CyclicBarrierHandlerTest {

    @Test
    public void testBarrier() throws InterruptedException {
        CyclicBarrierHandler barrierHandler = new CyclicBarrierHandler(100);
        String[] args = new String[]{"args"};
        barrierHandler.main(args);
        System.out.println(barrierHandler.list.size());
        Integer size = barrierHandler.list.size();

        assertTrue(size.equals(50));

        for (int i = 0; i < size-1; i+=2){
            assertTrue(barrierHandler.list.get(i).equals(barrierHandler.list.get(i+1)));
        }
    }

}