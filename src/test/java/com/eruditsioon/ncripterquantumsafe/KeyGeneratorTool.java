package com.eruditsioon.ncripterquantumsafe;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeyGeneratorTool {
    public static void main(String[] args) throws Exception {
        String keyLabel = "kem-cipher-768";
        String algo = "ML-KEM-768";
        String vaultPath = "./KeyVault";

        System.out.println("Generating key: " + keyLabel + " with algo: " + algo);

        // Generate Key Pair
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(algo);
        KeyPair kp = kpg.generateKeyPair();

        // Ensure KeyVault directory exists
        java.nio.file.Path vaultDir = Paths.get(vaultPath);
        if (!Files.exists(vaultDir)) {
            Files.createDirectories(vaultDir);
        }

        // Serialize Public Key (.pub)
        java.nio.file.Path publicKeyPath = vaultDir.resolve(keyLabel + ".pub");
        Files.write(publicKeyPath, kp.getPublic().getEncoded());
        System.out.println("Written: " + publicKeyPath.toAbsolutePath());

        // Serialize Private Key (.prv)
        java.nio.file.Path privateKeyPath = vaultDir.resolve(keyLabel + ".prv");
        Files.write(privateKeyPath, kp.getPrivate().getEncoded());
        System.out.println("Written: " + privateKeyPath.toAbsolutePath());
    }
}
