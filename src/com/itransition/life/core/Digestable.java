package com.itransition.life.core;

/**
 * Interface for entities for which a message digest can be calculated.
 * Typical message digest algorithms are MD5 and SHA.
 * @see java.security.MessageDigest
 */
public interface Digestable {
    /**
     * Get digest of the entity.
     * @return digest of some fixed length (depending on the algorithm).
     */
    public byte[] getDigest();
}
