package com.itransition.life.core;

/**
 * Digest cycle detection interface. Helps detect cycles in sequences of any objects that can
 * provide their digest.
 */
public interface DigestCycleDetector {
    /**
     * Get length of the cycle. If there is no cycle zero should be returned.
     * @return cycle length or 0 if there is no cycle.
     */
    public long getCycleLength();

    /**
     * Add digest to existing sequence of digests.
     * @param digest message digest of fixed length such as MD5 or SHA-256.
     * @see java.security.MessageDigest.
     */
    public void addDigest(byte[] digest);
}
