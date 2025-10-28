package by.pano.contracttests.query;

import java.util.List;

import by.pano.contracttests.model.Config;
import by.pano.contracttests.persistence.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GetConfigsQuery {

	private final ConfigRepository configRepository;

	public List<Config> getConfigs() {
		return configRepository.findAll().stream()
							   .map(Config::from)
							   .toList();
	}



}
