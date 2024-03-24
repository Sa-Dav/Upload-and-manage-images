package com.example.demo.service;

import com.example.demo.util.KeyGeneratorAES;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Service
public class AESEncryption {
    private static Key secretKey;
    private static final String SECRET_KEY_PATH = "secret.key";


    public byte[] encrypt(byte[] imageBytes) throws IOException, ClassNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (secretKey == null) {
            secretKey = KeyGeneratorAES.readKeyFromFile(SECRET_KEY_PATH);
        }
        Cipher encryptionCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = encryptionCipher.doFinal(imageBytes);
        return encryptedBytes;
    }

    public byte[] decrypt(byte[] encryptedData) throws Exception {
        if (secretKey == null) {
            secretKey = KeyGeneratorAES.readKeyFromFile(SECRET_KEY_PATH);
        }
        Cipher decryptionCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = decryptionCipher.doFinal(encryptedData);
        return decryptedBytes;
    }
}
