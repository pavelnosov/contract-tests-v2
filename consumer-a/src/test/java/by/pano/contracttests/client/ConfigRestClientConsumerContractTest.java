package by.pano.contracttests.client;

import java.util.List;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.JUnitTestSupport;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import by.pano.contracttests.config.TestWebClientFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;


@PactTestFor(providerName = "config-service-public-api", pactVersion = PactSpecVersion.V3 )
@ExtendWith({PactConsumerTestExt.class, MockitoExtension.class})
class ConfigRestClientConsumerContractTest {

	private ConfigRestClient client;

	@BeforeEach
	void initClient(MockServer mockServer) {
		WebClient webClient = TestWebClientFactory.createWebClient(mockServer.getPort());
		client = new ConfigRestClient(webClient);
	}

	@AfterEach
	void tearDown(MockServer mockServer) {
		JUnitTestSupport.validateMockServerResult(mockServer.validateMockServerState(null));
	}

	@Pact(consumer = "consumer-a")
	public RequestResponsePact getConfigs(PactDslWithProvider builder) {
		return builder
				.given("Ready with data")
				.uponReceiving("List of configs")
				.path("/public/v1/configs")
				.method("GET")
				.willRespondWith()
				.status(HttpStatus.OK.value())
				.body(PactDslJsonArray.arrayMinLike(1) // @formatter:off
					.stringType("id", "1")
					.stringType("name", "name1")
					.stringType("value", "value1")
				) // @formatter:on
				.toPact();
	}

	@PactTestFor(pactMethod = "getConfigs")
	@Test
	void getConfigs() {
		List<ConfigDTO> configs = client.getConfigs();

		assertThat(configs)
				.hasSize(1)
				.satisfiesExactly(item -> {
					assertThat(item.id()).isEqualTo("1");
					assertThat(item.name()).isEqualTo("name1");
					assertThat(item.value()).isEqualTo("value1");
				});
	}
}