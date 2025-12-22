package com.dentpulse.dentalsystem.service;

import com.google.auth.oauth2.TokenVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GoogleTokenVerifier {

    @Value("${google.client-id}")
    private String googleClientId;

    /**
     * Verify Google ID token and return payload
     */
    public Map<String, Object> verify(String idTokenString) {
        try {
            TokenVerifier verifier = TokenVerifier.newBuilder()
                    .setAudience(googleClientId)
                    .build();

            var jsonWebSignature = verifier.verify(idTokenString);

            if (jsonWebSignature == null) {
                throw new RuntimeException("Invalid Google ID token");
            }

            return jsonWebSignature.getPayload();

        } catch (Exception e) {
            throw new RuntimeException("Google token verification failed", e);
        }
    }


    // SAFE HELPER METHODS


    /** Google unique user ID (MOST IMPORTANT) */
    public String getGoogleId(Map<String, Object> payload) {
        return (String) payload.get("sub");
    }

    /** User email */
    public String getEmail(Map<String, Object> payload) {
        return (String) payload.get("email");
    }

    /** Check email verification */
    public boolean isEmailVerified(Map<String, Object> payload) {
        Object verified = payload.get("email_verified");
        return verified instanceof Boolean && (Boolean) verified;
    }

    /** Full name */
    public String getName(Map<String, Object> payload) {
        return (String) payload.get("name");
    }

    /** Profile picture URL */
    public String getPicture(Map<String, Object> payload) {
        return (String) payload.get("picture");
    }
}
