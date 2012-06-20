package com.itransition.life.test;

import com.itransition.life.core.ArrayLifeField;

import java.util.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ArrayLifeFieldTestCellStates {
    private int width;
    private int height;
    private int[][] cellsToTest;

    public ArrayLifeFieldTestCellStates(int width, int height, int[][] cellsToTest) {
        this.width = width;
        this.height = height;
        this.cellsToTest = cellsToTest;
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                { 5, 5, new int[][] {{1,1},{1,2},{3,3}} },
                { 1, 1, new int[][] {{0,0}} },
                { 2, 5, new int[][] {{1,1},{0,0},{1,4}} },
                { 10, 10, new int[][] {{1,1},{0,0},{1,4},{9,9},{3,7},{2,1}} },
                { 120, 120, new int[][] {{1,1},{0,0},{1,4},{9,9},{3,7},{2,1},{119,119},{100,100},{37,98}} },
                { 5, 12, new int[][] {{1,1},{0,0},{1,4},{4,9},{3,7},{2,1},{3,11},{1,10},{3,8}} },
                { 2, 2, new int[][] {{0,0},{1,0},{1,1},{0,1}} },
                { 4, 6, new int[][] {{1,1},{0,0},{3,5},{1,2},{2,1},{2,5},{3,1},{3,3},{0,5}} }
        };
        return Arrays.asList(data);
    }

    @Test
    public void testSetAlive() throws Exception {
        ArrayLifeField field = new ArrayLifeField(width, height);
        for(int i = 0; i < cellsToTest.length; i++) {
            int x = cellsToTest[i][0];
            int y = cellsToTest[i][1];
            field.setState(x, y, true);
            Assert.assertTrue(field.isAlive(x, y));
        }
    }

    @Test
    public void testSetDead() throws Exception {
        ArrayLifeField field = new ArrayLifeField(width,height);
        for(int i=0; i<cellsToTest.length; i++) {
            int x = cellsToTest[i][0];
            int y = cellsToTest[i][1];
            field.setState(x, y, false);
            Assert.assertFalse(field.isAlive(x,y));
        }
    }

    @Test
    public void testNumberOfAliveCells() throws Exception {
        ArrayLifeField field = new ArrayLifeField(width,height);
        for(int i=0; i<cellsToTest.length; i++) {
            int x = cellsToTest[i][0];
            int y = cellsToTest[i][1];
            field.setState(x, y, true);
        }
        Assert.assertEquals(cellsToTest.length,field.getNumberOfAliveCells());
    }

    @Test
    public void testNumberOfAliveCellsIncremetally() throws Exception {
        ArrayLifeField field = new ArrayLifeField(width,height);
        for(int i=0; i<cellsToTest.length; i++) {
            int x = cellsToTest[i][0];
            int y = cellsToTest[i][1];
            field.setState(x, y, true);
            Assert.assertEquals(i+1,field.getNumberOfAliveCells());
        }
        for(int i=cellsToTest.length-1; i>=0; i--) {
            int x = cellsToTest[i][0];
            int y = cellsToTest[i][1];
            field.setState(x, y, false);
            Assert.assertEquals(i,field.getNumberOfAliveCells());
        }
    }
}
