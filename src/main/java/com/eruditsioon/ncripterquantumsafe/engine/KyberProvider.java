package com.eruditsioon.ncripterquantumsafe.engine;

import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

@Component
public class KyberProvider {

    private static final String KEM_ALGORITHM = "ML-KEM";
    private static final String KYBER_ALGORITHM = "ML-KEM-1024";
    private static final int AES_KEY_SIZE_BITS = 256;
    private static final String SYMMETRIC_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_NONCE_LENGTH = 12; // 96 bits
    private static final int GCM_TAG_LENGTH = 16; // 128 bits

    public byte[] getKyberPublicKey(String  keyLabel){
        try {

            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            System.out.println("Reading file: " + keyLabel+".pub");
            return Files.readAllBytes(Paths.get(keyLabel+".pub"));


//           return Files.readAllBytes(Paths.get(keyLabel+".pub"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] decapsulateEncryptionAESGCM(byte[] encapsulation, byte[]initializationVector, byte[] cryptogram, String keyLabel){
        try {
            /// Load ML-KEM Private Key from file
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            System.out.println("Reading file: " + keyLabel+".prv");
            byte[] encodedPrivateKey = Files.readAllBytes(Paths.get(keyLabel+".prv"));
            KeyFactory keyFactory = KeyFactory.getInstance(KEM_ALGORITHM);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            PrivateKey kyberPrivateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            /// Decapsulate KEM encapsulation
            KEM kem = KEM.getInstance(KEM_ALGORITHM);
            KEM.Decapsulator decapsulator = kem.newDecapsulator(kyberPrivateKey);
            SecretKey sharedSecretKey = decapsulator.decapsulate(
                    encapsulation, 0, AES_KEY_SIZE_BITS / 8, "AES"
            );
            Cipher cipherDecrypt = Cipher.getInstance(SYMMETRIC_ALGORITHM);
            GCMParameterSpec gcmSpecDecrypt = new GCMParameterSpec(GCM_TAG_LENGTH * 8, initializationVector);
            cipherDecrypt.init(Cipher.DECRYPT_MODE, sharedSecretKey, gcmSpecDecrypt);
            byte[] clearData = cipherDecrypt.doFinal(cryptogram);
            return clearData;

        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException | InvalidKeyException |
                 DecapsulateException | NoSuchPaddingException | InvalidAlgorithmParameterException |
                 BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }

    }

}
