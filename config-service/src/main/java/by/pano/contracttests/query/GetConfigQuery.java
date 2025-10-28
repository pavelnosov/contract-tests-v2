package by.pano.contracttests.query;

import by.pano.contracttests.model.Config;
import by.pano.contracttests.persistence.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GetConfigQuery {

	private final ConfigRepository configRepository;

	public Config getConfig(String configId) {
		return configRepository.findById(configId)
							   .map(Config::from)
							   .orElse(null);
	}



}
