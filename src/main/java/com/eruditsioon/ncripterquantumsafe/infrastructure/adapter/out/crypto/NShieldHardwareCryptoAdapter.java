package com.eruditsioon.ncripterquantumsafe.infrastructure.adapter.out.crypto;

import com.eruditsioon.ncripterquantumsafe.domain.model.EncapsulationResult;
import com.eruditsioon.ncripterquantumsafe.domain.model.KeyGenerationResult;
import com.eruditsioon.ncripterquantumsafe.domain.model.PublicKeyResult;
import com.eruditsioon.ncripterquantumsafe.domain.port.out.MlKemKeyExchangePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import org.springframework.context.annotation.Primary;
import java.util.Optional;

@Service
@Primary
@ConditionalOnProperty(name = "crypto.engine", havingValue = "nshield")
public class NShieldHardwareCryptoAdapter implements MlKemKeyExchangePort {

    private static final Logger log = LoggerFactory.getLogger(NShieldHardwareCryptoAdapter.class);
    private final RestClient restClient;

    public NShieldHardwareCryptoAdapter(
            RestClient.Builder restClientBuilder,
            @Value("${crypto.nshield.url:http://127.0.0.1:8000}") String nshieldApiUrl) {
        this.restClient = restClientBuilder.baseUrl(nshieldApiUrl).build();
        log.info("Initialized NShieldHardwareCryptoAdapter pointing to {}", nshieldApiUrl);
    }

    @Override
    public Optional<EncapsulationResult> encapsulate(String pubKeyIdentifier) {
        try {
            var requestBody = new EncapsulateRequest(pubKeyIdentifier);

            var response = restClient.post()
                    .uri("/encapsulate")
                    .body(requestBody)
                    .retrieve()
                    .body(EncapsulateResponse.class);

            if (response != null) {
                byte[] encap = Base64.getDecoder().decode(response.encapsulation_b64());
                byte[] secret = Base64.getDecoder().decode(response.shared_secret_b64());
                return Optional.of(new EncapsulationResult(encap, secret));
            }
        } catch (Exception e) {
            log.error("Failed to execute native HSM Encapsulation via Python Sidecar: {}", e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<byte[]> decapsulateAndDecrypt(String privKeyIdentifier, byte[] encapsulation, byte[] iv,
            byte[] cryptogram) {
        try {
            var requestBody = new DecapsulateAndDecryptRequest(
                    privKeyIdentifier,
                    Base64.getEncoder().encodeToString(encapsulation),
                    Base64.getEncoder().encodeToString(iv),
                    Base64.getEncoder().encodeToString(cryptogram));

            var response = restClient.post()
                    .uri("/decapsulate-decrypt")
                    .body(requestBody)
                    .retrieve()
                    .body(DecryptResponse.class);

            if (response != null) {
                return Optional.of(Base64.getDecoder().decode(response.plaintext_b64()));
            }
        } catch (Exception e) {
            log.error("Failed to execute native HSM Decapsulation and Decryption via Python Sidecar: {}",
                    e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<KeyGenerationResult> generateKeyPair(String ident, String appName, String outFormat) {
        try {
            var requestBody = new GenerateRequest(ident, appName, outFormat);

            var response = restClient.post()
                    .uri("/generate-mlkem-keypair")
                    .body(requestBody)
                    .retrieve()
                    .body(GenerateResponse.class);

            if (response != null && response.success()) {
                return Optional.of(new KeyGenerationResult(
                        response.success(),
                        response.message(),
                        response.public_key(),
                        response.format(),
                        response.encoding()));
            }
        } catch (Exception e) {
            log.error("Failed to execute native HSM Key Generation via Python Sidecar: {}", e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<PublicKeyResult> getPublicKey(String ident, String appName, String outFormat) {
        try {
            var requestBody = new ExportPublicKeyRequest(ident, appName, outFormat);

            var response = restClient.post()
                    .uri("/get-public-mlkem-key")
                    .body(requestBody)
                    .retrieve()
                    .body(ExportPublicKeyResponse.class);

            if (response != null) {
                return Optional.of(new PublicKeyResult(
                        response.public_key(),
                        response.format(),
                        response.encoding()));
            }
        } catch (Exception e) {
            log.error("Failed to execute native HSM Public Key Export via Python Sidecar: {}", e.getMessage());
        }
        return Optional.empty();
    }

    // --- Internal DTOs modeling the FastAPI HTTP contract natively ---

    private record EncapsulateRequest(String pubKeyIdentifier) {
    }

    private record EncapsulateResponse(String encapsulation_b64, String shared_secret_b64) {
    }

    private record DecapsulateAndDecryptRequest(
            String privKeyIdentifier,
            String encapsulation_b64,
            String iv_b64,
            String cryptogram_b64) {
    }

    private record DecryptResponse(String plaintext_b64) {
    }

    private record GenerateRequest(String ident, String app_name, String out_format) {
    }

    private record GenerateResponse(boolean success, String message, String public_key, String format,
            String encoding) {
    }

    private record ExportPublicKeyRequest(String ident, String app_name, String out_format) {
    }

    private record ExportPublicKeyResponse(String public_key, String format, String encoding) {
    }
}
