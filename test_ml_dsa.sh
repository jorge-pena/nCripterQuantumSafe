#!/bin/bash

# Base URL
URL="http://localhost:8834/api/qs-crypto"

# 1. Generate Key Pair
echo "Generating Key Pair..."
curl -s -X POST -H "Content-Type: application/json" \
  -d '{"keyLabel": "testmldsa", "parameterSet": "ML_DSA_44"}' \
  $URL/generate-ml-dsa-key-pair
echo -e "\n"

# 2. Sign Data
# Data: "Hello World" in Base64 -> "SGVsbG8gV29ybGQ="
DATA="SGVsbG8gV29ybGQ="
echo "Signing Data ($DATA)..."
SIGN_RESPONSE=$(curl -s -X POST -H "Content-Type: application/json" \
  -d "{\"keyLabel\": \"testmldsa\", \"data\": \"$DATA\"}" \
  $URL/sign-ml-dsa)
echo $SIGN_RESPONSE
echo -e "\n"

# Extract Signature (using python or jq if available, otherwise simplified extraction)
# Assuming simple json structure, we can try to grep it or just manually verify if previous step worked.
# Ideally we want to automate the verification call.
# Let's try to extract signature using grep/sed
SIGNATURE=$(echo $SIGN_RESPONSE | grep -o '"signature":"[^"]*"' | cut -d'"' -f4)

if [ -z "$SIGNATURE" ]; then
  echo "Failed to extract signature"
  exit 1
fi

echo "Extracted Signature: ${SIGNATURE:0:20}..."

# 3. Verify Signature
echo "Verifying Signature..."
curl -s -X POST -H "Content-Type: application/json" \
  -d "{\"keyLabel\": \"testmldsa\", \"data\": \"$DATA\", \"signature\": \"$SIGNATURE\"}" \
  $URL/verify-ml-dsa
echo -e "\n"
