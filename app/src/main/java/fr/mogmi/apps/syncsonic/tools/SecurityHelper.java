package fr.mogmi.apps.syncsonic.tools;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * A set of methods used for security purposes
 */
public final class SecurityHelper {

    private SecurityHelper() {
    }

    /**
     * Generates the MD5SUM of a {@link String}
     *
     * @param s The {@link String} to hash
     * @return A {@link String} representing the hash
     */
    public static String md5(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        // A bit lazy to come up with a better method here o/
        while (hash.length() < 32) {
            hash = "0" + hash;
        }
        return hash;
    }

    /**
     * Generate a random {@link String} of length maxLength
     *
     * @param maxLength The size of the generated {@link String}
     * @return A String of size maxLength
     */
    public static String randomString(int maxLength) {
        SecureRandom random = new SecureRandom();

        return new BigInteger(130, random).toString(32);
    }
}
