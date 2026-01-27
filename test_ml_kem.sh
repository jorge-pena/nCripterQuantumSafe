#!/bin/bash

# Configuration
KEY_LABEL="test-kem-key"
PARAM_SET="ML_KEM_768"
PORT="4434"

echo "Generating ML-KEM Key Pair..."
curl -X POST http://localhost:$PORT/api/qs-crypto/generate-ml-kem-key-pair \
  -H "Content-Type: application/json" \
  -d "{
    \"keyLabel\": \"$KEY_LABEL\",
    \"parameterSet\": \"$PARAM_SET\"
  }"

echo -e "\n\nChecking if keys exist in KeyVault..."
if [ -f "./KeyVault/$KEY_LABEL.pub" ] && [ -f "./KeyVault/$KEY_LABEL.prv" ]; then
    echo "SUCCESS: Keys found in KeyVault."
    ls -l ./KeyVault/$KEY_LABEL.*
else
    echo "FAILURE: Keys not found."
    exit 1
fi
