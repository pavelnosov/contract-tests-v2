#!/bin/bash

PACT_BROKER_URL="http://localhost:9292"
ENVIRONMENT_FILE="environments.json"

# Check if jq is installed
if ! command -v jq &> /dev/null; then
    echo "Error: jq is required but not installed. Please install jq first."
    exit 1
fi

# Check if file exists
if [[ ! -f "$ENVIRONMENT_FILE" ]]; then
    echo "Error: $ENVIRONMENT_FILE not found!"
    exit 1
fi

# Read and process each webhook
echo "Creating Pact Broker environments from $ENVIRONMENT_FILE..."

jq -c '.[]' "$ENVIRONMENT_FILE" | while read -r myenv; do
    
    curl -X POST \
         -H "Content-Type: application/json" \
         -d "$myenv" \
         "$PACT_BROKER_URL/environments"
    
    echo "----------------------------------------"
done

echo "Environments creation completed!"