package by.pano.contracttests.client;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


@Component
class ConfigManagementRestClient implements ConfigManagementClient {

	public static final String BASE_PATH = "/management/v1/configs";
	private static final Logger log = LoggerFactory.getLogger(ConfigManagementRestClient.class);

	private final WebClient webClient;
	private final ObjectMapper objectMapper;
	private final ValidationErrorMapper validationErrorMapper;

	public ConfigManagementRestClient(WebClient webClient, @Qualifier("configWebClientFactoryObjectMapper") ObjectMapper objectMapper, ValidationErrorMapper validationErrorMapper) {
		this.webClient = webClient;
		this.objectMapper = objectMapper;
		this.validationErrorMapper = validationErrorMapper;
	}

	@Override
	public ConfigDTO getConfigById(String id) {
		return webClient.get()
						.uri(uriBuilder -> uriBuilder.path(BASE_PATH + "/{id}")
													 .build(id))
						.retrieve()
						.bodyToMono(ConfigDTO.class)
						.onErrorResume(WebClientRequestException.class, ex -> Mono.empty())
						.onErrorResume(WebClientResponseException.class, ex -> ex.getStatusCode() == HttpStatus.NOT_FOUND ? Mono.empty() : Mono.error(ex))
						.block();
	}

	@Override
	public List<ConfigDTO> getConfigs() {
		return webClient.get()
						.uri(uriBuilder -> uriBuilder.path(BASE_PATH ).build())
						.retrieve()
						.bodyToMono(new ParameterizedTypeReference<List<ConfigDTO>>() {

						})
						.onErrorResume(WebClientRequestException.class, ex -> Mono.empty())
						.onErrorResume(WebClientResponseException.class,
								ex -> ex.getStatusCode() == HttpStatus.NOT_FOUND ? Mono.just(List.of()) : Mono.error(ex))
						.block();
	}

	@Override
	public void delete(String id) {
		webClient.delete()
				 .uri(uriBuilder -> uriBuilder.path(BASE_PATH + "/{id}")
											  .build(id))
				 .retrieve()
				 .toBodilessEntity()
				 .onErrorResume(WebClientResponseException.class, ex -> ex.getStatusCode() == HttpStatus.NOT_FOUND ? Mono.empty() : Mono.error(ex))
				 .block();
	}

	@Override
	public ConfigDTO create(ConfigDTO config) {
		return webClient.post()
						.uri(uriBuilder -> uriBuilder.path(BASE_PATH).build())
						.body(Mono.just(config), ConfigDTO.class)
						.retrieve()
						.bodyToMono(ConfigDTO.class)
						.onErrorResume(WebClientResponseException.class, ex -> ex.getStatusCode() == HttpStatus.NOT_FOUND
										   ? Mono.empty()
										   : tryToPassThroughValidationResults(ex))
						.block();

	}

	@Override
	public ConfigDTO update(ConfigDTO config) {
		return webClient.put()
						.uri(uriBuilder -> uriBuilder.path(BASE_PATH).build())
						.body(Mono.just(config), ConfigDTO.class)
						.retrieve()
						.bodyToMono(ConfigDTO.class)
						.onErrorResume(WebClientResponseException.class, ex -> ex.getStatusCode() == HttpStatus.NOT_FOUND
										? Mono.empty()
										: tryToPassThroughValidationResults(ex))
						.block();

	}

	private Mono<ConfigDTO> tryToPassThroughValidationResults(WebClientResponseException e) {
		if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
			try {
				Errors errors = objectMapper.readValue(e.getResponseBodyAsString(), Errors.class);
				return Mono.error(new by.pano.contracttests.model.ValidationException(validationErrorMapper.map(errors.errors())));
			} catch (IOException ex) {
				log.error("Cannot parse error response", ex);
			}
		}

		return Mono.error(e);
	}
}
