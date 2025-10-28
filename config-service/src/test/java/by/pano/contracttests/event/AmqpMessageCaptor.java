package by.pano.contracttests.event;


import by.pano.contracttests.config.rabbit.Jsr310AwareJsonMessageConverter;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import static org.mockito.BDDMockito.then;


public class AmqpMessageCaptor<T> {

	private final ArgumentCaptor<T> messageCaptor;
	private final ArgumentCaptor<String> exchangeCaptor;
	private final ArgumentCaptor<String> routingKeyCaptor;

	public AmqpMessageCaptor(AmqpTemplate amqpTemplateMock, Class<T> clazz) {
		messageCaptor = ArgumentCaptor.forClass(clazz);
		exchangeCaptor = ArgumentCaptor.forClass(String.class);
		routingKeyCaptor = ArgumentCaptor.forClass(String.class);
		then(amqpTemplateMock).should().convertAndSend(exchangeCaptor.capture(), routingKeyCaptor.capture(), messageCaptor.capture());
	}

	public byte[] getBody() {
		Jsr310AwareJsonMessageConverter jsr310AwareJsonMessageConverter = new Jsr310AwareJsonMessageConverter();
		Message message = jsr310AwareJsonMessageConverter.toMessage(messageCaptor.getValue(), new MessageProperties());
		return message.getBody();
	}

	public String getExchange() {
		return exchangeCaptor.getValue();
	}

	public String getRoutingKey() {
		return routingKeyCaptor.getValue();
	}
}
