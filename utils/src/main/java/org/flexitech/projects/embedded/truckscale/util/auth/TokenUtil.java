package org.flexitech.projects.embedded.truckscale.util.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class TokenUtil {

    /**
     * Generates a unique session ID based on user data.
     * 
     * @param loginUserName The unique identifier of the user.
     * @param username The username of the user.
     * @return A unique session ID as a string.
     */
    public static String generateSessionToken(String loginUserName) {
        try {
        	String crrentMillisecond = System.currentTimeMillis() + "";
            String rawData = loginUserName + crrentMillisecond + UUID.randomUUID().toString();

            // Hash the combined data using SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawData.getBytes(StandardCharsets.UTF_8));

            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating session ID", e);
        }
    }
}
