package by.pano.contracttests.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestWebClientSupport {
	private TestWebClientSupport() {

	}

	public static WebClient createWebClient(int port) {
		ConfigServiceFactoryConfig config = createConfigServiceFactoryConfig(port);

		WebClientFactory webClientFactory = new WebClientFactory(config, ObjectMapperFactory.build());
		return webClientFactory.createWebClient();
	}

	public static ObjectMapper getObjectMapper() {
		return ObjectMapperFactory.build();
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