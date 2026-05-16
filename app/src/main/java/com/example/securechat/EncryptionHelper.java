package com.example.securechat;

import android.util.Base64;
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * BLUECRYPT ENCRYPTION CORE
 * 
 * SECURITY NOTE:
 * This implementation uses a hardcoded key for demonstration purposes.
 * For a professional production application, ALWAYS use the Android Keystore System
 * to generate and store cryptographic keys securely.
 * 
 * AES-128 is used here (16-byte key).
 */
public class EncryptionHelper {

    // 16-byte key for AES-128
    private static final String SECRET_KEY = "BLU3-CRYP7-K3Y-Z"; 

    public static String encrypt(String message) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP);
        } catch (Exception e) {
            return "ERR_ENC_FAIL";
        }
    }

    public static String decrypt(String encryptedMessage) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes = Base64.decode(encryptedMessage, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "ERR_DEC_FAIL";
        }
    }
}
