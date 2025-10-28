package by.pano.contracttests.client;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


@Slf4j
@Component
class ConfigRestClient implements ConfigClient {

	public static final String BASE_PATH = "/public/v1/configs";

	private final WebClient webClient;

	public ConfigRestClient(WebClient webClient) {
		this.webClient = webClient;
	}


	@Override
	public List<ConfigDTO> getConfigs() {
		return webClient.get()
						.uri(uriBuilder -> uriBuilder.path(BASE_PATH).build())
						.retrieve()
						.bodyToMono(new ParameterizedTypeReference<List<ConfigDTO>>() {

						})
						.onErrorResume(WebClientRequestException.class, ex -> Mono.empty())
						.onErrorResume(WebClientResponseException.class,
								ex -> ex.getStatusCode() == HttpStatus.NOT_FOUND ? Mono.just(List.of()) : Mono.error(ex))
						.block();
	}

}
