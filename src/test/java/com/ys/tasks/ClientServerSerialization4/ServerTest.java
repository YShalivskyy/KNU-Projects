package com.ys.tasks.ClientServerSerialization4;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by PC on 12/27/2016.
 */
public class ServerTest {
    @Test
    public void main() throws Exception {
        Server.main(null);
        assertTrue(Server.data.message.equals("Hi, how are you?"));
        assertTrue(Server.data.username.equals("Yarik"));
    }
}