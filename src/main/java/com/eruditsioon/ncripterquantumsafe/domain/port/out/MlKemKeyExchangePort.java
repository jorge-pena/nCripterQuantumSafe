package com.eruditsioon.ncripterquantumsafe.domain.port.out;

import java.util.Optional;
import com.eruditsioon.ncripterquantumsafe.domain.model.EncapsulationResult;
import com.eruditsioon.ncripterquantumsafe.domain.model.KeyGenerationResult;
import com.eruditsioon.ncripterquantumsafe.domain.model.PublicKeyResult;

public interface MlKemKeyExchangePort {
    /**
     * Encapsulates a shared secret utilizing an ML-KEM Public Key.
     */
    Optional<EncapsulationResult> encapsulate(String pubKeyIdentifier);

    /**
     * Decapsulates a shared AES key natively using an ML-KEM Private Key and
     * immediately decrypts the provided AES-256-GCM cryptogram payload.
     */
    Optional<byte[]> decapsulateAndDecrypt(String privKeyIdentifier, byte[] encapsulation, byte[] iv,
            byte[] cryptogram);

    /**
     * Generates a new ML-KEM key pair natively within the HSM.
     */
    Optional<KeyGenerationResult> generateKeyPair(String ident, String appName, String outFormat);

    /**
     * Extracts the ML-KEM public key material for a specified key pair.
     */
    Optional<PublicKeyResult> getPublicKey(String ident, String appName, String outFormat);
}
