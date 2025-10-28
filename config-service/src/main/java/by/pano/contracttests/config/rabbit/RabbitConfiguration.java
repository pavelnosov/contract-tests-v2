package by.pano.contracttests.config.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@EnableRabbit
@Configuration
public class RabbitConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConfiguration.class);
	public static final String EXCHANGE_NAME = "configservice";

	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter(
			ObjectMapper objectMapper
	) {
		return new Jackson2JsonMessageConverter(objectMapper);
	}

	@Bean
	public DirectExchange configServiceExchange(AmqpAdmin amqpAdmin) {
		LOGGER.info("Declaring exchange '{}' if absent.", EXCHANGE_NAME);
		DirectExchange exchange = new DirectExchange(EXCHANGE_NAME);
		exchange.setShouldDeclare(true);
		exchange.setAdminsThatShouldDeclare(amqpAdmin);
		return exchange;
	}

}
