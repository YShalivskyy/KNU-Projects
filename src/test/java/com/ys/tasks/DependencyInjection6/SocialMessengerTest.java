package com.ys.tasks.DependencyInjection6;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by PC on 12/27/2016.
 */
public class SocialMessengerTest {
    @Test
    public void testMain() throws Exception {
        SocialMessenger.main(null);
        assertTrue(SocialMessenger.sender.recipient.equals("Yarik"));
        assertTrue(SocialMessenger.sender.textM.equals("Hi Man"));
    }

}