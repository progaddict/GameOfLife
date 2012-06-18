package com.itransition.life.test;

import com.itransition.life.core.DigestCycleDetector;
import com.itransition.life.core.StackCycleDetector;
import org.junit.Test;

import java.security.MessageDigest;
import java.util.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class StackCycleDetectorTest {
    private int expectedCycleLength;
    private byte[] sequence;

    public StackCycleDetectorTest(int expectedCycleLength, byte[] sequence) {
        this.expectedCycleLength = expectedCycleLength;
        this.sequence = sequence;
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                { 0, new byte[] {1,2,3,4,5,6,7,8,9,10} },
                { 9, new byte[] {1,2,3,4,5,6,7,8,9,1,2,3,4,5,6,7,8,9} },
                { 10, new byte[] {9,8,7,6,5,4,3,2,1,0,9,8,7,6,5,4,3,2,1,0} },
                { 5, new byte[] {1,0,23,17,-88,2,3,4,5,7,2,3,4,5,7} }
        };
        return Arrays.asList(data);
    }

    @Test
    public void testGetCycleLength() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        DigestCycleDetector cycleDetector = new StackCycleDetector();
        for (int i=0; i<sequence.length; i++) {
            byte[] hash = md.digest(new byte[] {sequence[i]});
            cycleDetector.addDigest(hash);
        }
        Assert.assertEquals(expectedCycleLength,cycleDetector.getCycleLength());
    }
}
