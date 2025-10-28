package by.pano.contracttests.config.rabbit;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;


public class Jsr310AwareJsonMessageConverter extends Jackson2JsonMessageConverter {

	public Jsr310AwareJsonMessageConverter() {
		super(ObjectMapperFactory.createObjectMapper(), "*");
	}

}
