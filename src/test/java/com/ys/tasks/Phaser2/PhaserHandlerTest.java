package com.ys.tasks.Phaser2;

import org.junit.Test;

import java.util.Map;

/**
 * Created by PC on 12/14/2016.
 */
public class PhaserHandlerTest {

    @Test
    public void testPhaser() throws Exception {
        Phaser phaserTest = new Phaser(3);
        PhaserHandler handler = new PhaserHandler(phaserTest);
        String[] args = new String[]{"args"};
        PhaserHandler.main(args);
        int size = Phaser.getMap().size();
        System.out.println(size);
        System.out.println("Map entries");
        int counter = 0;
        for (Map.Entry<Integer,Integer> i : Phaser.getMap().entrySet()){
            counter++;
            System.out.println(counter + ") " + i.getValue());
        }
    }

}