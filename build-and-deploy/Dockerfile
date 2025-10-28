# Dockerfile
FROM openjdk:17-jdk-slim

# Install Node.js, npm, Grunt, Git, and Gradle
RUN apt-get update && \
    apt-get install -y curl wget unzip tar git && \
    curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g grunt grunt-cli && \
    wget https://services.gradle.org/distributions/gradle-8.10.2-bin.zip -P /tmp && \
    unzip /tmp/gradle-8.10.2-bin.zip -d /opt && \
    ln -s /opt/gradle-8.10.2 /opt/gradle && \
    apt-get clean && \
    curl -fsSL https://raw.githubusercontent.com/pact-foundation/pact-standalone/master/install.sh | PACT_CLI_VERSION=v2.5.2 bash - && \
    rm -rf /var/lib/apt/lists/* /tmp/*

ENV PATH="/opt/gradle/bin:/pact/bin:${PATH}"

# Verify installations
RUN java -version
RUN node --version
RUN npm --version
RUN grunt --version
RUN git --version
RUN gradle --version
RUN pact-broker version

# Set working directory
WORKDIR /app