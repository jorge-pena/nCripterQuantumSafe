#!/bin/bash
set -e

PORT="4434"
ENDPOINT_BASE="http://localhost:$PORT/api/qs-crypto/kmac"

KEY_LABEL="testkmackey-$(date +%s)"
ALGO="KMAC128"
PAYLOAD_B64=$(echo -n "Hello KMAC World" | base64)
CUSTOMIZATION="CustomString"

echo "--------------------------------------------------------"
echo "Starting KMAC Integration Test"
echo "Key Label: $KEY_LABEL"
echo "--------------------------------------------------------"

# 1. Generate KMAC Key
echo -e "\n[1] Testing KMAC Key Generation..."
GEN_RESP=$(curl -s -X POST "$ENDPOINT_BASE/generate-key" \
  -H "Content-Type: application/json" \
  -d "{\"keyLabel\": \"$KEY_LABEL\", \"algorithm\": \"$ALGO\", \"keySizeBytes\": 16}")

echo "Response: $GEN_RESP"
if [[ "$GEN_RESP" == *"Success"* ]]; then
    echo "  -> KMAC Key Generation OK"
else
    echo "  -> KMAC Key Generation FAILED"
    exit 1
fi

# 2. Compute KMAC Sign
echo -e "\n[2] Testing KMAC Signing..."
SIGN_RESP=$(curl -s -X POST "$ENDPOINT_BASE/sign" \
  -H "Content-Type: application/json" \
  -d "{\"keyLabel\": \"$KEY_LABEL\", \"algorithm\": \"$ALGO\", \"customizationString\": \"$CUSTOMIZATION\", \"outputLenBits\": 256, \"payload\": \"$PAYLOAD_B64\"}")

echo "Response: $SIGN_RESP"
TAG=$(echo "$SIGN_RESP" | grep -o '"tag":"[^"]*' | awk -F '"' '{print $4}' || true)

if [ -n "$TAG" ] && [[ "$SIGN_RESP" == *"Success"* ]]; then
    echo "  -> KMAC Signing OK (Tag: $TAG)"
else
    echo "  -> KMAC Signing FAILED"
    exit 1
fi

# 3. Verify KMAC Sign
echo -e "\n[3] Testing KMAC Verification..."
VERIFY_RESP=$(curl -s -X POST "$ENDPOINT_BASE/verify" \
  -H "Content-Type: application/json" \
  -d "{\"keyLabel\": \"$KEY_LABEL\", \"algorithm\": \"$ALGO\", \"customizationString\": \"$CUSTOMIZATION\", \"outputLenBits\": 256, \"payload\": \"$PAYLOAD_B64\", \"tag\": \"$TAG\"}")

echo "Response: $VERIFY_RESP"
if [[ "$VERIFY_RESP" == *"\"verified\":true"* ]]; then
    echo "  -> KMAC Verification OK"
else
    echo "  -> KMAC Verification FAILED"
    exit 1
fi

echo -e "\n--------------------------------------------------------"
echo "KMAC Lifecycle Test Sequence Completed Successfully"
echo "--------------------------------------------------------"
