package com.itransition.life.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Cycle detector implementing stack cycle detection algorithm.
 * Note: up to 2 full cycles might be required to detect the cycle length.
 * @see{http://www.gabrielnivasch.org/fun/cycle-detection}
 */
public class StackCycleDetector implements DigestCycleDetector {
    private static final Log LOGGER = LogFactory.getLog(StackCycleDetector.class);
    private long currentStep = 0;
    private Stack<byte[]> hashStack = new Stack<byte[]>();
    private Stack<Long> stepStack = new Stack<Long>();
    private long cycleLength = 0;

    /**
     * @see com.itransition.life.core.DigestCycleDetector#getCycleLength().
     */
    @Override
    public long getCycleLength() {
        return cycleLength;
    }

    /**
     * If cycle has been already found it does nothing.
     * Do not add different digests that differ in length!
     * @param digest message digest of fixed length such as MD5 or SHA-256.
     * @see DigestCycleDetector#addDigest(byte[]).
     */
    @Override
    public void addDigest(byte[] digest) {
        if (cycleLength > 0) {
            return;
        }
        currentStep++;
        while (true) {
            if(hashStack.isEmpty()) {
                hashStack.push(digest);
                stepStack.push(currentStep);
                return;
            }
            final byte[] lastHash = hashStack.peek();
            final long lastStep = stepStack.peek();
            int compareResult = compareHashes(lastHash,digest);
            if (compareResult == 0) {
                cycleLength = currentStep - lastStep;
                return;
            }
            if (compareResult < 0) {
                hashStack.push(digest);
                stepStack.push(currentStep);
                return;
            }
            hashStack.pop();
            stepStack.pop();
        }
    }
    
    private static int compareHashes(byte[] hashA, byte[] hashB) {
        for (int i=0; i<hashA.length; i++) {
            if (hashA[i] < hashB[i]) {
                return -1;
            }
            if (hashA[i] > hashB[i]) {
                return +1;
            }
        }
        return 0;
    }
}
