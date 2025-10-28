package by.pano.contracttests.config.rabbit;

import java.util.UUID;

import by.pano.contracttests.event.ConfigUpdatedConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@EnableRabbit
@Configuration
public class RabbitConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConfiguration.class);
	public static final String CONFIG_SERVICE_EXCHANGE_NAME = "configservice";
	public static final String ROUTING_KEY = "config_updated";
	public static final String EXCHANGE_NAME = "consumera";
	public static final String QUEUE = "consumera.config_updated";

	@Bean(name = "consumerTagStrategy")
	public ConsumerTagStrategy consumerTagStrategy(
			@Value("${info.app.version}") final String version
	){
		return queue -> "consumer-a-" + version + "-" + UUID.randomUUID();
	}

	@Bean
	public DirectExchange configServiceExchange() {
		LOGGER.info("Declaring exchange '{}' if absent.", CONFIG_SERVICE_EXCHANGE_NAME);
		DirectExchange exchange = new DirectExchange(CONFIG_SERVICE_EXCHANGE_NAME);
		exchange.setShouldDeclare(false);
		return exchange;
	}

	@Bean
	public DirectExchange consumerAExchange() {
		LOGGER.info("Declaring exchange '{}' if absent.", EXCHANGE_NAME);
		DirectExchange exchange = new DirectExchange(EXCHANGE_NAME);
		exchange.setShouldDeclare(true);
		return exchange;
	}

	@Bean
	public Queue consumerAConfigUpdatedQueue() {
		LOGGER.info("Declaring queue '{}' if absent.", QUEUE);
		Queue queue = QueueBuilder
				.durable(QUEUE)
				.build();
		queue.setShouldDeclare(true);
		return queue;
	}

	@Bean
	public Binding provideConsumerAExchangeToConfigServiceExchangeBinding(
			@Qualifier("configServiceExchange") DirectExchange configServiceExchange,
			@Qualifier("consumerAExchange") DirectExchange consumerAExchange) {
		return BindingBuilder.bind(consumerAExchange)
							 .to(configServiceExchange)
							 .with(ROUTING_KEY);
	}

	@Bean
	public Binding provideConsumerAExchangeToConfigUpdatedQueueBinding(
			@Qualifier("consumerAExchange") DirectExchange consumerAExchange,
			@Qualifier("consumerAConfigUpdatedQueue") Queue consumerAConfigUpdatedQueue) {
		return BindingBuilder.bind(consumerAConfigUpdatedQueue)
							 .to(consumerAExchange)
							 .with(ROUTING_KEY);
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setMessageConverter(new Jackson2JsonMessageConverter());
		return factory;
	}

	@Bean
	SimpleMessageListenerContainer container(
			ConnectionFactory connectionFactory,
			@Qualifier("consumerAConfigUpdatedQueue") Queue consumerAConfigUpdatedQueue,
			@Qualifier("consumerTagStrategy") ConsumerTagStrategy consumerTagStrategy,
			ConfigUpdatedConsumer receiver) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConsumerTagStrategy(consumerTagStrategy);
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(consumerAConfigUpdatedQueue.getName());
		container.setMessageListener(receiver);
		return container;
	}
}
