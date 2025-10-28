package by.pano.contracttests.event;

import java.util.Collections;
import java.util.Map;


import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@PactBroker
@Provider("config-service-public-notifications")
@IgnoreNoPactsToVerify
@AllowOverridePactUrl
@ExtendWith(PostgresExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConfigUpdatePublisherProviderContractTest {

	@Autowired
	private ConfigUpdatePublisher producer;

	@MockBean
	private RabbitTemplate rabbitTemplateMock;

	@BeforeEach
	void setup(PactVerificationContext context) {
		if (context == null) {
			return;
		}
		context.setTarget(new MessageTestTarget(Collections.singletonList("by.pano.contracttests")));
	}

	@TestTemplate
	@ExtendWith(PactVerificationSpring6Provider.class)
	void pactVerificationTestTemplate(PactVerificationContext context) {
		if (context == null) {
			return;
		}
		context.verifyInteraction();
	}

	@State("config is updated")
	void preMessageReadByReceiverEventV2() {
	}

	@PactVerifyProvider("an id of updated config")
	MessageAndMetadata configIsUpdated() {
		producer.publish("2");

		AmqpMessageCaptor<ConfigUpdated> amqpMessageCaptor = new AmqpMessageCaptor<>(rabbitTemplateMock, ConfigUpdated.class);

		return new MessageAndMetadata(amqpMessageCaptor.getBody(), Map.of(
			"x-rabbit-exchange", amqpMessageCaptor.getExchange(),
			"x-rabbit-routingkey", amqpMessageCaptor.getRoutingKey()
		));
	}
}