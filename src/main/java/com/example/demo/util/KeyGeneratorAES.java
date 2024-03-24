package com.example.demo.util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class KeyGeneratorAES {
    private static final String AES_ALGORITHM = "AES";
    private static final String SECRET_KEY_PATH = "secret.key";

    public static void init() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGenerator.init(256); // You can adjust the key size as needed
        Key key = keyGenerator.generateKey();
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(SECRET_KEY_PATH))) {
            outputStream.writeObject(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Key readKeyFromFile(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            return (SecretKey) inputStream.readObject();
        }
    }

}
