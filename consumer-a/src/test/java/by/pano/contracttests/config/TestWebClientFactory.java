package by.pano.contracttests.config;

import by.pano.contracttests.config.rabbit.ObjectMapperFactory;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestWebClientFactory {
	private TestWebClientFactory() {

	}

	public static WebClient createWebClient(int port) {
		ConfigServiceFactoryConfig config = createConfigServiceFactoryConfig(port);

		WebClientFactory webClientFactory = new WebClientFactory(config, ObjectMapperFactory.createObjectMapper());
		return webClientFactory.createWebClient();
	}

	private static ConfigServiceFactoryConfig createConfigServiceFactoryConfig(int port) {
		ConfigServiceFactoryConfig config = mock(ConfigServiceFactoryConfig.class);

		when(config.getConnectTimeoutMillis()).thenReturn(500);
		when(config.getReadTimeoutMillis()).thenReturn(500);
		when(config.getWriteTimeoutMillis()).thenReturn(500);
		when(config.getResponseBodyLimitBytes()).thenReturn(16777216);
		when(config.getConfigServiceUrl()).thenReturn("http://localhost:%d".formatted(port));

		return config;
	}
}