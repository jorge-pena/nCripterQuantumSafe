package com.eruditsioon.ncripterquantumsafe.infrastructure.adapter.out.crypto;

import com.eruditsioon.ncripterquantumsafe.domain.model.KmacGenerateResponse;
import com.eruditsioon.ncripterquantumsafe.domain.port.out.KmacPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;

import java.util.Base64;
import java.util.Optional;

@Service
@Primary
@ConditionalOnProperty(name = "crypto.engine", havingValue = "nshield")
public class NShieldHardwareKmacAdapter implements KmacPort {

    private static final Logger log = LoggerFactory.getLogger(NShieldHardwareKmacAdapter.class);
    private final RestClient restClient;

    public NShieldHardwareKmacAdapter(
            RestClient.Builder restClientBuilder,
            @Value("${crypto.nshield.url:http://127.0.0.1:8000}") String nshieldApiUrl) {
        this.restClient = restClientBuilder.baseUrl(java.util.Objects.requireNonNull(nshieldApiUrl)).build();
        log.info("Initialized NShieldHardwareKmacAdapter pointing to {}", nshieldApiUrl);
    }

    @Override
    public Optional<KmacGenerateResponse> generateKey(String ident, String algorithm, int keySizeBytes, String appName) {
        try {
            var requestBody = new HardwareKmacGenerateRequest(ident, appName, algorithm, keySizeBytes);

            var response = restClient.post()
                    .uri("/api/v1/kmac/generate-key")
                    .body(requestBody)
                    .retrieve()
                    .body(HardwareGenerateResponse.class);

            if (response != null && response.success()) {
                return Optional.of(new KmacGenerateResponse(ident, algorithm, "Success"));
            }
        } catch (Exception e) {
            log.error("Failed to execute native HSM KMAC Key Generation via Python Sidecar: {}", e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<byte[]> sign(String keyIdentifier, String algorithm, String customizationString, int outputLenBits, byte[] payload, String appName) {
        try {
            String payloadB64 = Base64.getEncoder().encodeToString(payload);
            String customB64 = customizationString != null ? Base64.getEncoder().encodeToString(customizationString.getBytes()) : "";

            var requestBody = new HardwareKmacSignRequest(
                    keyIdentifier, appName, algorithm, customB64, outputLenBits, payloadB64);

            var response = restClient.post()
                    .uri("/api/v1/kmac/sign")
                    .body(requestBody)
                    .retrieve()
                    .body(HardwareKmacSignResponse.class);

            if (response != null) {
                return Optional.of(Base64.getDecoder().decode(response.tag_b64()));
            }
        } catch (Exception e) {
            log.error("Failed to execute native HSM KMAC Sign via Python Sidecar: {}", e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> verify(String keyIdentifier, String algorithm, String customizationString, int outputLenBits, byte[] payload, byte[] tag, String appName) {
        try {
            String payloadB64 = Base64.getEncoder().encodeToString(payload);
            String customB64 = customizationString != null ? Base64.getEncoder().encodeToString(customizationString.getBytes()) : "";
            String tagB64 = Base64.getEncoder().encodeToString(tag);

            var requestBody = new HardwareKmacVerifyRequest(
                    keyIdentifier, appName, algorithm, customB64, outputLenBits, payloadB64, tagB64);

            var response = restClient.post()
                    .uri("/api/v1/kmac/verify")
                    .body(requestBody)
                    .retrieve()
                    .body(HardwareKmacVerifyResponse.class);

            if (response != null) {
                return Optional.of(response.verified());
            }
        } catch (Exception e) {
            log.error("Failed to execute native HSM KMAC Verify via Python Sidecar: {}", e.getMessage());
        }
        return Optional.empty();
    }

    // --- Internal DTOs modeling the FastAPI HTTP contract natively ---

    private record HardwareKmacGenerateRequest(String ident, String app_name, String algorithm, int key_size_bytes) {
    }

    private record HardwareGenerateResponse(boolean success, String message) {
    }

    private record HardwareKmacSignRequest(
            String key_identifier,
            String app_name,
            String algorithm,
            String customization_string_b64,
            int output_len_bits,
            String payload_b64) {
    }

    private record HardwareKmacSignResponse(String tag_b64) {
    }

    private record HardwareKmacVerifyRequest(
            String key_identifier,
            String app_name,
            String algorithm,
            String customization_string_b64,
            int output_len_bits,
            String payload_b64,
            String tag_b64) {
    }

    private record HardwareKmacVerifyResponse(boolean verified) {
    }
}
