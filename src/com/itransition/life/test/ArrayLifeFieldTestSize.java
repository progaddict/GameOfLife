package com.itransition.life.test;

import com.itransition.life.core.ArrayLifeField;
import java.util.Arrays;
import java.util.Collection;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ArrayLifeFieldTestSize {
    private int expectedWidth;
    private int expectedHeight;

    public ArrayLifeFieldTestSize (int width, int height) {
        this.expectedWidth = width;
        this.expectedHeight = height;
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                {1,1}, {2,5}, {10,10}, {120,120}, {5,10},
                {7,5}, {10,22}, {33,80}, {1,100}, {100,1},
                {17,99}, {99,99}, {100,100}, {120,10}, {17,18}
        };
        return Arrays.asList(data);
    }

    @Test
    public void testGetWidth() throws Exception {
        ArrayLifeField field = new ArrayLifeField(expectedWidth,expectedHeight);
        Assert.assertEquals(expectedWidth,field.getWidth());
    }

    @Test
    public void testGetHeight() throws Exception {
        ArrayLifeField field = new ArrayLifeField(expectedWidth,expectedHeight);
        Assert.assertEquals(expectedHeight,field.getHeight());
    }
}
