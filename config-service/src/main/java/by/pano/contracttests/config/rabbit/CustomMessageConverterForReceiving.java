package by.pano.contracttests.config.rabbit;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;


public class CustomMessageConverterForReceiving extends ContentTypeDelegatingMessageConverter {

	public CustomMessageConverterForReceiving() {
		super(new ContentTypeDelegatingMessageConverter(new SimpleMessageConverter()));
		addDelegate(MessageProperties.CONTENT_TYPE_JSON, new Jsr310AwareJsonMessageConverter());
		addDelegate(MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT, new SerializerMessageConverter());
	}

}
