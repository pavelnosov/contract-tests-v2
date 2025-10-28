# Contract tests with Pact Broker

## Purpose 

The purpose of this repo is to provide code sample which illustrates adoption of contract tests with Pact Proker in CI process.

## Setup CI environment

1) Run CI environment in Docker
```
cd ./ci-env
make docker-up
```
2) Configure Gitlab Runner, see [here](./ci-env/README.md#configure-gitlab-runner)
3) Add projects, see here [here](./ci-env/README.md#add-projects)
4) Create environments in Pact Broker 
```
cd ./ci-env
make create-environments
```
5) Create webhooks in pact broker, see [here](./ci-env/README.md#create-webhooks-in-pact-broker)

## Integration history

1) Updated contract tests 
    - made tests use `@PactBroker` instead of `@PactFolder`
    - made tests ignore null PactVerificationContext context (case when provider is build before consumers published their pacts)
    - splitted up contract tests into ConsumerContractTest and ProviderContractTest
2) Updated build.gradle
    - updated gradle test task to exclude ProviderContractTest during test run
    - added pact gradle plugin (id 'au.com.dius.pact' version '4.6.14')
        - later I deleted it as plugin required version and branch even for build job.
    - configured pact plugin
        - later I deleted the configuration
3) Updated build image 
    - included pact standalone executables
    - inclided git
4) Updated CI pipeline
    - added jobs to publish pacts and publish verification results
    - added record-deployment job
        - as it was impossible to merge message and request-response interactions in a single contract, I had had to introduce PACTICIPANTS variable. PACTICIPANTS lists all related consumers and providers of the service. Record-deployments and can-i-deploy have to be performed for each pacticipant.
        - I found out that before record-deployment one needed to add environments to Pact Broker. 
        - I found out that it was impossible to record deployment for provider version before it was created. The fix would have required a new CI job (prepare) to create a pacticipant version for each build run, but I did not implement it. I just ignored failures of record-deployment.
    - added can-i-deploy job

