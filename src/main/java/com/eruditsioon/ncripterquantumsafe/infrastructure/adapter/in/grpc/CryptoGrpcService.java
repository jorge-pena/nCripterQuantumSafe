package com.eruditsioon.ncripterquantumsafe.infrastructure.adapter.in.grpc;

import com.eruditsioon.ncripterquantumsafe.domain.port.in.KeyEncapsulationUseCase;
import com.eruditsioon.ncripterquantumsafe.domain.model.EncapsulationResult;
import com.eruditsioon.ncripterquantumsafe.infrastructure.config.HSMKeyLabelMapper;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Base64;

@GrpcService
public class CryptoGrpcService extends CryptoServiceGrpc.CryptoServiceImplBase {

    private final KeyEncapsulationUseCase keyEncapsulationUseCase;

    @Autowired
    public CryptoGrpcService(KeyEncapsulationUseCase keyEncapsulationUseCase) {
        this.keyEncapsulationUseCase = keyEncapsulationUseCase;
    }

    @Override
    public void generateKeyPair(GenerateKeyPairRequest request, StreamObserver<GenerateKeyPairResponse> responseObserver) {
        try {
            // Apply HSMKeyLabelMapper to isolate multi-tenant keys strictly for gRPC gateway client
            String mappedLabel = HSMKeyLabelMapper.mapLabel(request.getKeyLabel());
            
            String pubKey = keyEncapsulationUseCase.generateMLKEMKeyPair(
                    mappedLabel,
                    request.getParameterSet(),
                    request.getOutFormat() != null && !request.getOutFormat().isEmpty() ? request.getOutFormat() : "x509-pem"
            );
            
            GenerateKeyPairResponse response = GenerateKeyPairResponse.newBuilder()
                    .setKeyLabel(request.getKeyLabel()) // Return original label for tracking
                    .setParameterSet(request.getParameterSet())
                    .setStatus("Success")
                    .setPublicKey(pubKey)
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Failed to generate ML-KEM key pair: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void getPublicKey(GetPublicKeyRequest request, StreamObserver<GetPublicKeyResponse> responseObserver) {
        try {
            String format = request.getOutFormat() != null && !request.getOutFormat().isEmpty() ? request.getOutFormat() : "x509-pem";
            String mappedLabel = HSMKeyLabelMapper.mapLabel(request.getKeyLabel());
            
            String pubKey = keyEncapsulationUseCase.requestKyberPublicKey(mappedLabel, format);
            
            GetPublicKeyResponse response = GetPublicKeyResponse.newBuilder()
                    .setPublicKey(pubKey)
                    .setFormat(format)
                    .setEncoding("base64")
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Failed to export public key: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void encapsulate(EncapsulateRequest request, StreamObserver<EncapsulateResponse> responseObserver) {
        try {
            String mappedLabel = HSMKeyLabelMapper.mapLabel(request.getKeyLabel());
            
            EncapsulationResult result = keyEncapsulationUseCase.encapsulateKyber(mappedLabel);
            
            String encapsulationB64 = Base64.getEncoder().encodeToString(result.encapsulation());
            String sharedSecretB64 = Base64.getEncoder().encodeToString(result.sharedSecret());
            
            EncapsulateResponse response = EncapsulateResponse.newBuilder()
                    .setEncapsulationB64(encapsulationB64)
                    .setSharedSecretB64(sharedSecretB64)
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Failed to execute ML-KEM encapsulation: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void decapsulateAndDecrypt(DecapsulateAndDecryptRequest request, StreamObserver<DecapsulateAndDecryptResponse> responseObserver) {
        try {
            byte[] encapsulation = Base64.getDecoder().decode(request.getEncapsulationB64());
            byte[] iv = Base64.getDecoder().decode(request.getIvB64());
            byte[] cryptogram = Base64.getDecoder().decode(request.getCryptogramB64());
            
            String mappedLabel = HSMKeyLabelMapper.mapLabel(request.getKeyLabel());
            
            byte[] plaintext = keyEncapsulationUseCase.decapsulateEncryptionAESGCM(
                    encapsulation,
                    iv,
                    cryptogram,
                    mappedLabel
            );
            
            String plaintextB64 = Base64.getEncoder().encodeToString(plaintext);
            
            DecapsulateAndDecryptResponse response = DecapsulateAndDecryptResponse.newBuilder()
                    .setPlaintextB64(plaintextB64)
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Failed to execute ML-KEM decapsulation and decryption: " + e.getMessage())
                    .asRuntimeException());
        }
    }
}
