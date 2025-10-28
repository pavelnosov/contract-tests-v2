package by.pano.contracttests.event;

import by.pano.contracttests.config.rabbit.CustomMessageConverterForReceiving;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;

public abstract class EventConsumer<T> implements MessageListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumer.class);

	private MessageConverter messageConverter;

	protected EventConsumer() {
		messageConverter = new CustomMessageConverterForReceiving();
	}

	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}

	@Override
	public void onMessage(Message message) {
		T event;
		try {
			event = (T)messageConverter.fromMessage(message);
		} catch (Exception ex) {
			LOGGER.error("Error converting message : {}", message, ex);
			return;
		}

		try {
			handleMessage(event);
		} catch (Exception ex) {
			LOGGER.error("Error on handleMessage for Event: {}", event, ex);
		}
	}

	public abstract void handleMessage(T event);
}
