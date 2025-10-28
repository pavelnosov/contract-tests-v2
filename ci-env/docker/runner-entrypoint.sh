#!/bin/bash
set -e

echo "Waiting for GitLab to be ready..."
while ! curl -s http://gitlab:8000 > /dev/null; do
  echo "GitLab API not ready yet, waiting..."
  sleep 30
done

if [ ! -f /etc/gitlab-runner/config.toml ]; then
  echo "Registering GitLab Runner..."
  gitlab-runner register \
    --non-interactive \
    --url http://gitlab:8000 \
    --registration-token GR1348941nitialToken \
    --executor docker \
    --docker-image docker:latest \
    --description "Docker Runner" \
    --tag-list "docker,linux" \
    --run-untagged=true \
    --locked=false
fi

echo "Starting GitLab Runner..."
exec gitlab-runner run "$@"