package com.eruditsioon.ncripterquantumsafe.domain.model;

public record KeyGenerationResult(
        boolean success,
        String message,
        String publicKey,
        String format,
        String encoding) {
}
