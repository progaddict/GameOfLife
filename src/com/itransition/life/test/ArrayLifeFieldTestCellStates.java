package com.itransition.life.test;

import com.itransition.life.core.ArrayLifeField;
import java.util.Arrays;
import java.util.Collection;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(value = Parameterized.class)
public class ArrayLifeFieldTestCellStates {
    private int width;
    private int height;
    private int[][] cellsToTest;
    ArrayLifeField field;

    public ArrayLifeFieldTestCellStates(int width, int height, int[][] cellsToTest) {
        this.width = width;
        this.height = height;
        this.cellsToTest = cellsToTest;
        field = new ArrayLifeField(width,height);
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                { 5, 5, new int[][] {{1,1},{1,2},{3,3}} },
                { 1, 1, new int[][] {{0,0}} },
                { 2, 5, new int[][] {{1,1},{0,0},{1,4}} },
                { 10, 10, new int[][] {{1,1},{0,0},{1,4},{9,9},{3,7},{2,1}} },
                { 120, 120, new int[][] {{1,1},{0,0},{1,4},{9,9},{3,7},{2,1},{119,119},{100,100},{37,98}} }
        };
        return Arrays.asList(data);
    }

    @Test
    public void testSetAlive() throws Exception {
        for(int i=0; i< cellsToTest.length; i++) {
            int x = cellsToTest[i][0];
            int y = cellsToTest[i][1];
            field.setAlive(x,y);
            Assert.assertTrue(field.isAlive(x,y));
        }
    }

    @Test
    public void testSetDead() throws Exception {
        for(int i=0; i< cellsToTest.length; i++) {
            int x = cellsToTest[i][0];
            int y = cellsToTest[i][1];
            field.setDead(x,y);
            Assert.assertFalse(field.isAlive(x,y));
        }
    }
}
