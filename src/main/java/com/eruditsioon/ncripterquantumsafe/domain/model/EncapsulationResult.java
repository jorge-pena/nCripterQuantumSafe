package com.eruditsioon.ncripterquantumsafe.domain.model;

/**
 * Domain record encapsulating the output of an ML-KEM exchange.
 * 
 * @param encapsulation The ciphertext bound for the receiver.
 * @param sharedSecret  The symmetric AES target key (e.g. 32 bytes for
 *                      AES-256).
 */
public record EncapsulationResult(
        byte[] encapsulation,
        byte[] sharedSecret) {
}
