.PHONY: help
help:
	@echo "Please use \`make <target>' where <target> is one of"
	@echo #
	@grep '^.PHONY: .* #' Makefile | sed 's/\.PHONY: \(.*\) # \(.*\)/\1: \2/' | expand -t20

.PHONY: docker-up # Run docker environment
.PHONY: docker-stop # Stop docker environment. Containers still there and will be restarted with next docker-up
.PHONY: docker-down # Down docker environment. Containers are 
.PHONY: copy-public-key # copy pub key to clipboard 
.PHONY: docker-remove-artifacts # remove all docker data
.PHONY: list-environments # list all Pact Broker environments
.PHONY: create-all-environments # Add all environemnts to Pact Broker
.PHONY: create-environemnts # Add all environemnts to Pact Broker

docker-up:
	@docker compose up -d
docker-stop:
	@docker compose stop
docker-down:
	@docker compose down
copy-public-key:
	@cat ~/.ssh/github_id_ed25519.pub | pbcopy
docker-remove-artifacts:
	@rm -rf ./docker/gitlab/config
	@rm -rf ./docker/gitlab/logs
	@rm -rf ./docker/gitlab/data
	@rm -rf ./docker/gitlab/runner
	@rm -rf ./docker/gitlab-runner
	@rm -rf ./docker/postgresql
	@rm -rf ./docker/redis
list-environments:
	@pact-broker list-environments --broker-base-url=http://pact-broker:9292/
create-all-environments:
	@pact-broker create-environment --name=test1 --broker-base-url=http://pact-broker:9292/
	@pact-broker create-environment --name=test2 --broker-base-url=http://pact-broker:9292/
	@pact-broker create-environment --name=test3 --broker-base-url=http://pact-broker:9292/
create-webhooks:
	@chmod +x ./create-webhooks.sh
	@./create-webhooks.sh
create-environments:
	@chmod +x ./create-environments.sh
	@./create-environments.sh


