package by.pano.contracttests.query;

import java.util.List;

import by.pano.contracttests.client.ConfigDTO;
import by.pano.contracttests.client.ConfigManagementClient;
import by.pano.contracttests.model.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GetConfigsQuery {

	private final ConfigManagementClient configManagementClient;

	public List<Config> execute() {
		List<ConfigDTO> configs = configManagementClient.getConfigs();
		return toConfigs(configs);
	}

	private static List<Config> toConfigs(List<ConfigDTO> configs) {
		return configs.stream()
					  .map(configDTO -> new Config(configDTO.id(), configDTO.name(), configDTO.value()))
					  .toList();
	}
}
