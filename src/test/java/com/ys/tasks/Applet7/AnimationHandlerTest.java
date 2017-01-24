package com.ys.tasks.Applet7;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by PC on 12/27/2016.
 */
public class AnimationHandlerTest {
    @Test
    public void checkSpanBall() throws Exception {
        String[] a = {"sd"};
        ActionHandler actionHandler = new ActionHandler();
        actionHandler.begin();

        assertTrue(actionHandler.getMainWindow().getAnimationHandler().getBallSize());
    }

}