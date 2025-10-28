package by.pano.contracttests.config;

import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@RequiredArgsConstructor
class WebClientFactory {

	private final ConfigServiceFactoryConfig configServiceFactoryConfig;
	private final ObjectMapper objectMapper;

	public WebClient createWebClient() {
		ExchangeStrategies strategies = ExchangeStrategies.builder().codecs(configurer -> {
			configurer.defaultCodecs().maxInMemorySize(configServiceFactoryConfig.getResponseBodyLimitBytes());
			configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, APPLICATION_JSON));
			configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, APPLICATION_JSON));
		}).build();

		return WebClient.builder()
						.exchangeStrategies(strategies)
						.clientConnector(new ReactorClientHttpConnector(getClient()))
						.baseUrl(configServiceFactoryConfig.getConfigServiceUrl())
						.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.build();
	}

	private HttpClient getClient() {
		return HttpClient.create()
						 .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, configServiceFactoryConfig.getConnectTimeoutMillis())
						 .doOnConnected(connection -> {
							 connection.addHandlerLast(
								 new ReadTimeoutHandler(configServiceFactoryConfig.getReadTimeoutMillis(), TimeUnit.MILLISECONDS));
							 connection.addHandlerLast(
								 new WriteTimeoutHandler(configServiceFactoryConfig.getWriteTimeoutMillis(), TimeUnit.MILLISECONDS));
						 });
	}
}
