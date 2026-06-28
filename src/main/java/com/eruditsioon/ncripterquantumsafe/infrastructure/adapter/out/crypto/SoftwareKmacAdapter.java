package com.eruditsioon.ncripterquantumsafe.infrastructure.adapter.out.crypto;

import com.eruditsioon.ncripterquantumsafe.domain.model.KmacGenerateResponse;
import com.eruditsioon.ncripterquantumsafe.domain.port.out.KmacPort;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Optional;

@Component
public class SoftwareKmacAdapter implements KmacPort {

    private static final Logger log = LoggerFactory.getLogger(SoftwareKmacAdapter.class);
    private static final String DIGEST_ALGORITHM = "SHA3-256";

    @Override
    public Optional<KmacGenerateResponse> generateKey(String ident, String algorithm, int keySizeBytes, String appName) {
        log.info("Simulating KMAC Key Generation locally for identifier '{}' (algorithm: {}, size: {} bytes)",
                ident, algorithm, keySizeBytes);
        return Optional.of(new KmacGenerateResponse(ident, algorithm, "Success"));
    }

    @Override
    public Optional<byte[]> sign(String keyIdentifier, String algorithm, String customizationString, int outputLenBits, byte[] payload, String appName) {
        log.info("Simulating KMAC Sign locally using SHA3-256 for key '{}'", keyIdentifier);
        try {
            MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM);
            // Deterministic simulation hash by feeding key, algorithm, customization string and payload
            if (keyIdentifier != null) {
                digest.update(keyIdentifier.getBytes(StandardCharsets.UTF_8));
            }
            if (algorithm != null) {
                digest.update(algorithm.getBytes(StandardCharsets.UTF_8));
            }
            if (customizationString != null) {
                digest.update(customizationString.getBytes(StandardCharsets.UTF_8));
            }
            if (payload != null) {
                digest.update(payload);
            }
            byte[] fullHash = digest.digest();

            // Truncate/pad to output length (e.g. outputLenBits / 8)
            int outputBytes = outputLenBits > 0 ? outputLenBits / 8 : 32;
            byte[] tag = Arrays.copyOf(fullHash, outputBytes);
            return Optional.of(tag);
        } catch (Exception e) {
            log.error("Failed to compute software-simulated KMAC tag", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> verify(String keyIdentifier, String algorithm, String customizationString, int outputLenBits, byte[] payload, byte[] tag, String appName) {
        log.info("Simulating KMAC Verify locally using SHA3-256 for key '{}'", keyIdentifier);
        Optional<byte[]> computedTagOpt = sign(keyIdentifier, algorithm, customizationString, outputLenBits, payload, appName);
        if (computedTagOpt.isPresent()) {
            boolean matches = Arrays.equals(computedTagOpt.get(), tag);
            return Optional.of(matches);
        }
        return Optional.of(false);
    }
}
