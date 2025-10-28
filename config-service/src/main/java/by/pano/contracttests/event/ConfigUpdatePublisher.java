package by.pano.contracttests.event;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class ConfigUpdatePublisher{

	private static final String ROUTING_KEY = "config_updated";
	private final RabbitTemplate rabbitTemplate;
	private final Exchange exchange;

	public ConfigUpdatePublisher(RabbitTemplate rabbitTemplate, @Qualifier("configServiceExchange") Exchange exchange) {
		this.rabbitTemplate = rabbitTemplate;
		this.exchange = exchange;
	}

	public void publish(String configId) {
		rabbitTemplate.convertAndSend(exchange.getName(), ROUTING_KEY, new ConfigUpdated(configId));
	}
}
