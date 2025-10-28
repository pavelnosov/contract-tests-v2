package by.pano.contracttests.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
class ConfigManagementClientConfiguration {

	@Bean
	public ConfigServiceFactoryConfig configServiceFactoryConfig() {
		return new ConfigServiceFactoryConfig();
	}

	@Bean
	public ObjectMapper configWebClientFactoryObjectMapper() {
		return ObjectMapperFactory.build();
	}

	@Bean
	public WebClientFactory configWebClientFactory(ConfigServiceFactoryConfig paceServiceFactoryConfig, ObjectMapper paceWebClientFactoryObjectMapper) {
		return new WebClientFactory(paceServiceFactoryConfig, paceWebClientFactoryObjectMapper);
	}

	@Bean
	public WebClient configWebClient(WebClientFactory paceWebClientFactory) {
		return paceWebClientFactory.createWebClient();
	}
}
