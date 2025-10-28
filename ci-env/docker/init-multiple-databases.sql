-- Create databases if they do not exist
SELECT 'CREATE DATABASE gitlabhq_production'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'gitlabhq_production')\gexec

SELECT 'CREATE DATABASE pact_broker'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'pact_broker')\gexec

-- Create users if they do not exist, and grant privileges
DO $$
BEGIN
    -- Create the 'gitlab' user if it doesn't exist
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'gitlab') THEN
        CREATE USER gitlab WITH ENCRYPTED PASSWORD 'gitlab_password';
    END IF;
    -- Grant all privileges on the database to the user
    GRANT ALL PRIVILEGES ON DATABASE gitlabhq_production TO gitlab;

    -- Create the 'pact_broker_user' if it doesn't exist
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'pact_broker_user') THEN
        CREATE USER pact_broker_user WITH ENCRYPTED PASSWORD 'pact_broker_password';
    END IF;
    -- Grant all privileges on the database to the user
    GRANT ALL PRIVILEGES ON DATABASE pact_broker TO pact_broker_user;
END
$$;