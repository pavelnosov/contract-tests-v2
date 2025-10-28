#!/bin/bash

PACT_BROKER_URL="http://localhost:9292"
WEBHOOK_FILE="webhooks.json"

# Check if jq is installed
if ! command -v jq &> /dev/null; then
    echo "Error: jq is required but not installed. Please install jq first."
    exit 1
fi

# Check if file exists
if [[ ! -f "$WEBHOOK_FILE" ]]; then
    echo "Error: $WEBHOOK_FILE not found!"
    exit 1
fi

# Read and process each webhook
echo "Creating Pact Broker webhooks from $WEBHOOK_FILE..."

jq -c '.[]' "$WEBHOOK_FILE" | while read -r webhook; do
    description=$(echo "$webhook" | jq -r '.description')
    echo "Creating webhook: $description"
    
    curl -X POST \
         -H "Content-Type: application/json" \
         -d "$webhook" \
         "$PACT_BROKER_URL/webhooks"
    
    echo "----------------------------------------"
done

echo "Webhook creation completed!"