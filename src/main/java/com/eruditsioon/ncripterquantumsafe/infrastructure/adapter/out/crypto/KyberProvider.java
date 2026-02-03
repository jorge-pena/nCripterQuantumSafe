package com.eruditsioon.ncripterquantumsafe.infrastructure.adapter.out.crypto;

import com.eruditsioon.ncripterquantumsafe.domain.exception.nCripterException;
import com.eruditsioon.ncripterquantumsafe.domain.port.out.CryptoEngine;
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
public class KyberProvider implements CryptoEngine {

    private static final String KEM_ALGORITHM = "ML-KEM";
    private static final String ML_DSA_ALGORITHM = "ML-DSA";

    private static final int AES_KEY_SIZE_BITS = 256;
    private static final String SYMMETRIC_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_NONCE_LENGTH = 12; // 96 bits
    private static final int GCM_TAG_LENGTH = 16; // 128 bits

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KyberProvider.class);

    @org.springframework.beans.factory.annotation.Value("${crypto.keyvault:./KeyVault}")
    private String keyVaultPath;

    private java.nio.file.Path getKeyPath(String keyLabel, String extension) {
        String safePath = (keyVaultPath == null || keyVaultPath.trim().isEmpty()) ? "./KeyVault" : keyVaultPath;
        java.nio.file.Path vaultDir = Paths.get(safePath);
        java.nio.file.Path keyPath = vaultDir.resolve(keyLabel + extension).toAbsolutePath();
        logger.info("Resolved key path for label '{}' to: '{}'", keyLabel, keyPath);
        return keyPath;
    }

    public byte[] getKyberPublicKey(String keyLabel) {
        try {
            java.nio.file.Path publicKeyPath = getKeyPath(keyLabel, ".pub");
            if (!Files.exists(publicKeyPath)) {
                logger.error("Public key file not found at: {}", publicKeyPath);
                throw new nCripterException("Public key not found for label: " + keyLabel + " at " + publicKeyPath);
            }
            return Files.readAllBytes(publicKeyPath);

        } catch (IOException e) {
            throw new nCripterException("Failed to read Kyber public key", e);
        }
    }

    public byte[] decapsulateEncryptionAESGCM(byte[] encapsulation, byte[] initializationVector, byte[] cryptogram,
            String keyLabel) {
        try {
            if (initializationVector.length != GCM_NONCE_LENGTH) {
                throw new IllegalArgumentException("Invalid IV length. Expected " + GCM_NONCE_LENGTH + " bytes.");
            }
            /// Load ML-KEM Private Key from file
            java.nio.file.Path privateKeyPath = getKeyPath(keyLabel, ".prv");
            if (!Files.exists(privateKeyPath)) {
                logger.error("Private key file not found at: {}", privateKeyPath);
                throw new nCripterException("Private key not found for label: " + keyLabel + " at " + privateKeyPath);
            }
            byte[] encodedPrivateKey = Files.readAllBytes(privateKeyPath);
            KeyFactory keyFactory = KeyFactory.getInstance(KEM_ALGORITHM);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            PrivateKey kyberPrivateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            /// Decapsulate KEM encapsulation
            KEM kem = KEM.getInstance(KEM_ALGORITHM);
            KEM.Decapsulator decapsulator = kem.newDecapsulator(kyberPrivateKey);
            SecretKey sharedSecretKey = decapsulator.decapsulate(
                    encapsulation, 0, AES_KEY_SIZE_BITS / 8, "AES");
            Cipher cipherDecrypt = Cipher.getInstance(SYMMETRIC_ALGORITHM);
            GCMParameterSpec gcmSpecDecrypt = new GCMParameterSpec(GCM_TAG_LENGTH * 8, initializationVector);
            cipherDecrypt.init(Cipher.DECRYPT_MODE, sharedSecretKey, gcmSpecDecrypt);
            byte[] clearData = cipherDecrypt.doFinal(cryptogram);
            return clearData;

        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException | InvalidKeyException
                | DecapsulateException | NoSuchPaddingException | InvalidAlgorithmParameterException
                | BadPaddingException | IllegalBlockSizeException e) {
            throw new nCripterException("Decapsulation failed", e);
        }

    }

    @Override
    public void generateMLDSAKeyPair(String keyLabel, String parameterSet) {
        try {
            // Validate and normalize parameter set (e.g., ML_DSA_44 -> ML-DSA-44)
            String normalizedAlgo = parameterSet.replace("_", "-");

            // Generate Key Pair
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(normalizedAlgo);
            KeyPair kp = kpg.generateKeyPair();

            // Ensure KeyVault directory exists
            String safePath = (keyVaultPath == null || keyVaultPath.trim().isEmpty()) ? "./KeyVault" : keyVaultPath;
            java.nio.file.Path vaultDir = Paths.get(safePath);
            if (!Files.exists(vaultDir)) {
                Files.createDirectories(vaultDir);
            }

            // Serialize Public Key (.pub)
            java.nio.file.Path publicKeyPath = getKeyPath(keyLabel, ".pub");
            Files.write(publicKeyPath, kp.getPublic().getEncoded());

            // Serialize Private Key (.prv)
            java.nio.file.Path privateKeyPath = getKeyPath(keyLabel, ".prv");
            Files.write(privateKeyPath, kp.getPrivate().getEncoded());

        } catch (NoSuchAlgorithmException e) {
            throw new nCripterException("Failed to generate ML-DSA key pair: Invalid parameter set " + parameterSet, e);
        } catch (IOException e) {
            throw new nCripterException("Failed to save ML-DSA key pair for label " + keyLabel, e);
        } catch (Exception e) {
            throw new nCripterException("Unexpected error during key generation: " + e.getMessage(), e);
        }
    }

    @Override
    public void generateMLKEMKeyPair(String keyLabel, String parameterSet) {
        try {
            // Validate and normalize parameter set (e.g., ML_KEM_768 -> ML-KEM-768)
            String normalizedAlgo = parameterSet.replace("_", "-");

            // Generate Key Pair
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(normalizedAlgo);
            KeyPair kp = kpg.generateKeyPair();

            // Ensure KeyVault directory exists
            String safePath = (keyVaultPath == null || keyVaultPath.trim().isEmpty()) ? "./KeyVault" : keyVaultPath;
            java.nio.file.Path vaultDir = Paths.get(safePath);
            if (!Files.exists(vaultDir)) {
                Files.createDirectories(vaultDir);
            }

            // Serialize Public Key (.pub)
            java.nio.file.Path publicKeyPath = getKeyPath(keyLabel, ".pub");
            Files.write(publicKeyPath, kp.getPublic().getEncoded());

            // Serialize Private Key (.prv)
            java.nio.file.Path privateKeyPath = getKeyPath(keyLabel, ".prv");
            Files.write(privateKeyPath, kp.getPrivate().getEncoded());

        } catch (NoSuchAlgorithmException e) {
            throw new nCripterException("Failed to generate ML-KEM key pair: Invalid parameter set " + parameterSet, e);
        } catch (IOException e) {
            throw new nCripterException("Failed to save ML-KEM key pair for label " + keyLabel, e);
        } catch (Exception e) {
            throw new nCripterException("Unexpected error during key generation: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] signMLDSA(String keyLabel, byte[] data) {
        try {
            // Read Private Key
            java.nio.file.Path privateKeyPath = getKeyPath(keyLabel, ".prv");
            if (!Files.exists(privateKeyPath)) {
                logger.error("Private key not found at: {}", privateKeyPath);
                throw new nCripterException("Private key not found for label: " + keyLabel);
            }
            byte[] encodedPrivateKey = Files.readAllBytes(privateKeyPath);

            // Reconstruct Private Key
            KeyFactory keyFactory = KeyFactory.getInstance(ML_DSA_ALGORITHM);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // Sign Data
            Signature signer = Signature.getInstance(ML_DSA_ALGORITHM);
            signer.initSign(privateKey);
            signer.update(data);
            return signer.sign();

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException
                | SignatureException e) {
            throw new nCripterException("Signing failed for label: " + keyLabel, e);
        }
    }

    @Override
    public boolean verifyMLDSA(String keyLabel, byte[] data, byte[] signature) {
        try {
            // Read Public Key
            java.nio.file.Path publicKeyPath = getKeyPath(keyLabel, ".pub");
            if (!Files.exists(publicKeyPath)) {
                logger.error("Public key not found at: {}", publicKeyPath);
                throw new nCripterException("Public key not found for label: " + keyLabel);
            }
            byte[] encodedPublicKey = Files.readAllBytes(publicKeyPath);

            // Reconstruct Public Key
            KeyFactory keyFactory = KeyFactory.getInstance(ML_DSA_ALGORITHM);
            java.security.spec.X509EncodedKeySpec keySpec = new java.security.spec.X509EncodedKeySpec(encodedPublicKey);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // Verify Signature
            Signature verifier = Signature.getInstance(ML_DSA_ALGORITHM);
            verifier.initVerify(publicKey);
            verifier.update(data);
            return verifier.verify(signature);

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException
                | SignatureException e) {
            throw new nCripterException("Verification failed for label: " + keyLabel, e);
        }
    }
}
