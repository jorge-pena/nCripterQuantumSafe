#!/bin/bash

# Configuration
PORT="4434"
KEY_LABEL="test-kyber-full-$(date +%s)"
PARAM_SET="ML_KEM_768"
KEY_VAULT_DIR="./KeyVault" # Assuming default from application.properties

echo "----------------------------------------------------------------"
echo "Starting Kyber Full Lifecycle Test"
echo "Key Label: $KEY_LABEL"
echo "----------------------------------------------------------------"

# 1. Generate ML-KEM Key Pair
echo -e "\n1. Generating ML-KEM Key Pair..."
GEN_RESPONSE=$(curl -s -X POST http://localhost:$PORT/api/qs-crypto/generate-ml-kem-key-pair \
  -H "Content-Type: application/json" \
  -d "{
    \"keyLabel\": \"$KEY_LABEL\",
    \"parameterSet\": \"$PARAM_SET\"
  }")

echo "Response: $GEN_RESPONSE"
if [[ "$GEN_RESPONSE" == *"Success"* ]]; then
    echo "SUCCESS: Key generation API call successful."
else
    echo "FAILURE: Key generation API call failed."
    exit 1
fi

# 2. Check for key files in KeyVault
echo -e "\n2. Checking for key files in $KEY_VAULT_DIR..."
if [ -f "$KEY_VAULT_DIR/$KEY_LABEL.pub" ] && [ -f "$KEY_VAULT_DIR/$KEY_LABEL.prv" ]; then
    echo "SUCCESS: Key files found in KeyVault."
    ls -l $KEY_VAULT_DIR/$KEY_LABEL.*
else
    echo "FAILURE: Key files not found in $KEY_VAULT_DIR."
    echo "Contents of $KEY_VAULT_DIR:"
    ls -l $KEY_VAULT_DIR
    exit 1
fi

# 3. Request Public Key
echo -e "\n3. Requesting Public Key via API..."
PUB_KEY_RESPONSE=$(curl -s -X POST http://localhost:$PORT/api/qs-crypto/request-kyber-public-key \
  -H "Content-Type: application/json" \
  -d "{
    \"keyLabel\": \"$KEY_LABEL\"
  }")

# Check if response contains "encodedPublicKey" (basic check)
if [[ "$PUB_KEY_RESPONSE" == *"encodedPublicKey"* ]]; then
    echo "SUCCESS: Public Key retrieval successful."
    # Optional: Decode base64 and compare with file? strict JSON parsing needed.
    # For now, just ensuring it didn't error 500.
else
    echo "FAILURE: Public Key retrieval failed."
    echo "Response: $PUB_KEY_RESPONSE"
    exit 1
fi

# 4. Decapsulate Encryption (Stubbed)
# We can't easily generate a valid encapsulation/cryptogram without a client library.
# However, we can send dummy data and verify that the server attempts to read the key
# from the correct location. If the key file was not found, we'd get a specific error.
# If we get "Decapsulation failed", it means it likely found the key but failed crypto.

echo -e "\n4. Attempting Decapsulation (Expecting crypto failure, not file failure)..."
# Dummy base64 data
ENCAPSULATION="AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" # Invalid length likely, but let's see
IV="BBBBBBBBBBBBBBBB" # 12 bytes base64? No, needs 12 bytes decoded.
# "BBBBBBBBBBBBBBBB" is 16 chars -> 12 bytes decoded exactly (Base64). Good.

# Send request
DECAP_RESPONSE=$(curl -s -X POST http://localhost:$PORT/api/qs-crypto/decapsulate-encryption-aes-gcm \
  -H "Content-Type: application/json" \
  -d "{
    \"encapsulation\": \"$(echo -n 'A' | base64)\", 
    \"initializationVector\": \"$(echo -n '123456789012' | base64)\",
    \"cryptogram\": \"$(echo -n 'SECRET' | base64)\",
    \"keyLabel\": \"$KEY_LABEL\"
  }")

echo "Response: $DECAP_RESPONSE"

# Analysis
# If server code failed at "Private key not found", we would see that in the logs/exception message usually 
# (if nCripterException message is returned).
# Current Controller returns a wrap. 
# We are looking for anything other than "Private key not found".
# Ideally, we should see "Decapsulation failed".

if [[ "$DECAP_RESPONSE" == *"Decapsulation failed"* ]]; then
    echo "SUCCESS: Received crypto failure as expected (Key file access likely successful)."
elif [[ "$DECAP_RESPONSE" == *"Private key not found"* ]]; then
    echo "FAILURE: Server could not find the private key file."
    exit 1
else
    echo "NOTE: Received unexpected response. Check server logs for confirmation."
    # Depending on how exceptions are handled, we might just get 500.
fi

echo -e "\nFull Test Sequence Completed."
