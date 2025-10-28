package by.pano.contracttests.resource;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.AllowOverridePactUrl;
import au.com.dius.pact.provider.junitsupport.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.spring6.PactVerificationSpring6Provider;
import by.pano.contracttests.PostgresExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;



@PactBroker
@Provider("config-service-public-api")
@IgnoreNoPactsToVerify
@AllowOverridePactUrl
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgresExtension.class)
@Sql("classpath:sql/config-service-public-api.sql")
public class PublicResourceProviderContractTest {

	private static final String STATE_READY = "Ready with data";

	@LocalServerPort
	private int port;

	@BeforeEach
	void before(PactVerificationContext context) {
		if (context == null) {
			return;
		}
		context.setTarget(new HttpTestTarget("localhost", port));
	}

	@TestTemplate
	@ExtendWith(PactVerificationSpring6Provider.class)
	void pactVerificationTestTemplate(PactVerificationContext context) {
		if (context == null) {
			return;
		}
		context.verifyInteraction();
	}

	@State(STATE_READY)
	public void validReadyState() {
	}
}
