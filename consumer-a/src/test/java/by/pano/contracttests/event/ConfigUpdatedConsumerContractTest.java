package by.pano.contracttests.event;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.Message;
import au.com.dius.pact.core.model.messaging.MessagePact;
import by.pano.contracttests.config.rabbit.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "config-service-public-notifications", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V3)
class ConfigUpdatedConsumerContractTest {

	private static final ObjectMapper OBJECT_MAPPER = ObjectMapperFactory.createObjectMapper();

	@Pact(consumer = "consumer-a")
	MessagePact configUpdated(MessagePactBuilder builder) {
		return builder
				.given("config is updated")
				.expectsToReceive("an id of updated config")
				.withMetadata(Map.of(
						"x-rabbit-exchange", "configservice",
						"x-rabbit-routingkey", "config_updated",
						"content-type", "application/json"
				))
				.withContent(new PactDslJsonBody() // @formatter:off
					.stringType("id", "1")
					// @formatter:on
				)
				.toPact();
	}

	@Test
	@PactTestFor(pactMethod = "configUpdated")
	void configUpdated(List<Message> messages) throws IOException {
		String contents = messages.get(0).getContents().valueAsString();

		ConfigUpdated event = OBJECT_MAPPER.readValue(contents, ConfigUpdated.class);

		assertThat(event.id()).isEqualTo("1");
	}
}