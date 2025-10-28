# CI environment

## Gitlab Creds
user: root
pass: R6VVykB2DSQA/qYE7daQXiYRh8WhD7vTVS/d5VQtFrA=

## Get password
To get root password run `cat /etc/gitlab/initial_root_password` in gitlab container

## Configure gitlab-runner
After registration of gitlab-runner which should happen automatically one needs to do next:
1) from `./docker/gitlab-runner/config/config.toml` copy to `./docker/config.toml`
```
  token = "glrtr-GgSJsyveRehyzxQhWhu-"
  token_obtained_at = 2025-10-04T18:45:46Z
  token_expires_at = 0001-01-01T00:00:00Z
```
2) replace `./docker/gitlab-runner/config/config.toml` with `./docker/config.toml`
3) restart gitlab-runner container

## Add projects
1) add group `contract-tests` in gitlab 
2) add project `build-and-deploy` w/o readme in gitlab
    - Settings > Access tokens > Add new token >
        - Role: Developer
        - Scopes: read_registry, write_registry
    - Settings > CI/CD > Variables > Add variable
        - Name: CI_REGISTRY_PASSWORD
        - value: token created on previous step
    - Settings > CI/CD > Job token permissions > Add 
        - add all projects which needs to use `gitlab:5005/contract-tests/build-and-deploy/java-node-grunt:latest` image
    - on local env: init git, add remote per instructions on gitlab project page, commit/push
3) add `config-service`, `config-management-portal` and `consumer-a` projects w/o readme in gitlab
    - on local env: init git, add remote per instructions on gitlab project page, commit/push

## Create webhooks in Pact Broker

1) Adjust project Id and token in webhooks.json
2) Run make command
```
cd ./ci-env
make create-webhooks
```


