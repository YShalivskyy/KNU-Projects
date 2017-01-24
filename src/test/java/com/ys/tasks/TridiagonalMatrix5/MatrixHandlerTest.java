package com.ys.tasks.TridiagonalMatrix5;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by PC on 12/26/2016.
 */
public class MatrixHandlerTest {
    @Test
    public void main() throws Exception {
        MatrixHandler mat = new MatrixHandler();

        List<Float[]> system = new ArrayList<>();
        system.add(new Float[]{-0f, 3f, -5f, 7f});
        system.add(new Float[]{-1f, 2f, -7f, 9f});
        system.add(new Float[]{-4f, 5f, -3f, 2f});
        system.add(new Float[]{-5f, 6f, -0f, 7f});
        mat.setSystem(system);

        String[] args = new String[]{"args"};
        mat.main(args);

        assertTrue(mat.getX()[0] == 4f);
        assertTrue(mat.getX()[1] == -1f);
        assertTrue(mat.getX()[2] == 1f);
        assertTrue(mat.getX()[3] == 0.3333333f);

    }

//    @Test
//    public void mainTest() throws Exception {
//        MatrixHandler q = new MatrixHandler();
//        String[] args = {"args"};
//        MatrixHandler.main(args);
//
////        assertTrue(floor(MatrixHandler.getX()[0]) == -3);
////        assertTrue(floor(MatrixHandler.getX()[1]) == 1);
////        assertTrue(floor(MatrixHandler.getX()[2]) == 5);
////        assertTrue(floor(MatrixHandler.getX()[3]) == -8);
//    }

}