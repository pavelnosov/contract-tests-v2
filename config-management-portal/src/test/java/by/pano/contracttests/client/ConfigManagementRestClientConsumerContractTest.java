package by.pano.contracttests.client;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.JUnitTestSupport;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import by.pano.contracttests.config.TestWebClientSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;


@PactTestFor(providerName = "config-service-management-api", pactVersion = PactSpecVersion.V3 )
@ExtendWith({PactConsumerTestExt.class, MockitoExtension.class})
class ConfigManagementRestClientConsumerContractTest {

	private static final String CONSUMER = "config-management-portal";
	private static final String STATE_READY = "Ready with data";

	private static final Instant VERSION = Instant.parse("2024-09-30T00:00:00Z");
	private static final String ID = "1";
	private static final String DELETE_ID = "2";

	private static final String CREATE_ID = "3";
	private static final String CREATE_NAME = "name3";
	private static final String CREATE_VALUE = "value3";
	private static final Instant CREATE_VERSION = Instant.parse("2024-09-30T10:13:47Z");

	private static final String UPDATE_ID = "4";
	private static final String UPDATE_NAME = "name4";
	private static final String UPDATE_VALUE = "value4";
	private static final Instant UPDATE_VERSION = Instant.parse("2024-09-30T10:13:47Z");
	private static final Instant UPDATE_VERSION_REMOTE = UPDATE_VERSION.plus(1, ChronoUnit.SECONDS);

	private ConfigManagementClient client;

	@BeforeEach
	void initClient(MockServer mockServer) {
		WebClient webClient = TestWebClientSupport.createWebClient(mockServer.getPort());
		client = new ConfigManagementRestClient(webClient, TestWebClientSupport.getObjectMapper(), new ValidationErrorMapper());
	}

	@AfterEach
	void tearDown(MockServer mockServer) {
		JUnitTestSupport.validateMockServerResult(mockServer.validateMockServerState(null));
	}

	@Pact(consumer = CONSUMER)
	public RequestResponsePact getConfigs(PactDslWithProvider builder) {
		return builder
				.given(STATE_READY)
				.uponReceiving("List of configs")
				.path("/management/v1/configs")
				.method("GET")
				.willRespondWith()
				.status(HttpStatus.OK.value())
				.body(PactDslJsonArray.arrayMinLike(1) // @formatter:off
					.stringType("id", "1")
					.stringType("name", "name2")
					.stringType("value", "value6")
				  	.datetime("version", "yyyy-MM-dd'T'HH:mm:ssXXX", VERSION)
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
					assertThat(item.name()).isEqualTo("name2");
					assertThat(item.value()).isEqualTo("value6");
					assertThat(item.version()).isEqualTo(VERSION);
				});
	}

	@Pact(consumer = CONSUMER)
	public RequestResponsePact getConfigById(PactDslWithProvider builder) {
		return builder
				.given(STATE_READY)
				.uponReceiving("Config with id:1")
				.path("/management/v1/configs/" + ID)
				.method("GET")
				.willRespondWith()
				.status(HttpStatus.OK.value())
				.body(new PactDslJsonBody() // @formatter:off
					.stringType("id", "1")
					.stringType("name", "name1")
					.stringType("value", "value1")
					.datetime("version", "yyyy-MM-dd'T'HH:mm:ssXXX", VERSION)
				) // @formatter:on
				.toPact();
	}

	@PactTestFor(pactMethod = "getConfigById")
	@Test
	void getConfigById() {
		ConfigDTO config = client.getConfigById(ID);

		assertThat(config.id()).isEqualTo("1");
		assertThat(config.name()).isEqualTo("name1");
		assertThat(config.value()).isEqualTo("value1");
		assertThat(config.version()).isEqualTo(VERSION);
	}

	@Pact(consumer = CONSUMER)
	public RequestResponsePact delete(PactDslWithProvider builder) {
		return builder
				.given(STATE_READY)
				.uponReceiving("Config is deleted")
				.path("/management/v1/configs/" + DELETE_ID)
				.method("DELETE")
				.willRespondWith()
				.status(HttpStatus.NO_CONTENT.value())
				.toPact();
	}

	@PactTestFor(pactMethod = "delete")
	@Test
	void delete() {
		client.delete(DELETE_ID);
	}


	@Pact(consumer = CONSUMER)
	public RequestResponsePact create(PactDslWithProvider builder) {
		return builder
				.given(STATE_READY)
				.uponReceiving("Config is created")
				.path("/management/v1/configs")
				.method("POST")
				.headers(Map.of("Content-Type", "application/json"))
				.body(new PactDslJsonBody() // @formatter:off
					.stringType("id", CREATE_ID)
					.stringType("name", CREATE_NAME)
					.stringType("value", CREATE_VALUE)
				) // @formatter:on
				.willRespondWith()
				.status(HttpStatus.CREATED.value())
				.body(new PactDslJsonBody() // @formatter:off
					.stringType("id", CREATE_ID)
					.stringType("name", CREATE_NAME)
					.stringType("value", CREATE_VALUE)
					.datetime("version", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", CREATE_VERSION)
				) // @formatter:on
				.toPact();
	}

	@PactTestFor(pactMethod = "create")
	@Test
	void create() {
		ConfigDTO createdConfig = client.create(new ConfigDTO(CREATE_ID, CREATE_NAME, CREATE_VALUE, null));

		assertThat(createdConfig.id()).isEqualTo(CREATE_ID);
		assertThat(createdConfig.name()).isEqualTo(CREATE_NAME);
		assertThat(createdConfig.value()).isEqualTo(CREATE_VALUE);
		assertThat(createdConfig.version()).isEqualTo(CREATE_VERSION);
	}

	@Pact(consumer = CONSUMER)
	public RequestResponsePact update(PactDslWithProvider builder) {
		return builder
				.given(STATE_READY)
				.uponReceiving("Config is updated")
				.path("/management/v1/configs")
				.method("PUT")
				.headers(Map.of("Content-Type", "application/json"))
				.body(new PactDslJsonBody() // @formatter:off
											.stringType("id", UPDATE_ID)
											.stringType("name", UPDATE_NAME)
											.stringType("value", UPDATE_VALUE)
											.datetime("version", "yyyy-MM-dd'T'HH:mm:ssXXX", UPDATE_VERSION)
				) // @formatter:on
				.willRespondWith()
				.status(HttpStatus.CREATED.value())
				.body(new PactDslJsonBody() // @formatter:off
											.stringType("id", UPDATE_ID)
											.stringType("name", UPDATE_NAME)
											.stringType("value", UPDATE_VALUE)
											.datetime("version", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", UPDATE_VERSION_REMOTE)
				) // @formatter:on
				.toPact();
	}

	@PactTestFor(pactMethod = "update")
	@Test
	void update() {
		ConfigDTO createdConfig = client.update(new ConfigDTO(UPDATE_ID, UPDATE_NAME, UPDATE_VALUE, UPDATE_VERSION));

		assertThat(createdConfig.id()).isEqualTo(UPDATE_ID);
		assertThat(createdConfig.name()).isEqualTo(UPDATE_NAME);
		assertThat(createdConfig.value()).isEqualTo(UPDATE_VALUE);
		assertThat(createdConfig.version()).isEqualTo(UPDATE_VERSION_REMOTE);
	}
}