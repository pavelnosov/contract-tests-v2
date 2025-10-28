package by.pano.contracttests.query;

import by.pano.contracttests.client.ConfigDTO;
import by.pano.contracttests.client.ConfigManagementClient;
import by.pano.contracttests.model.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GetConfigQuery {

	public final ConfigManagementClient configManagementClient;

	public Config execute(String id) {
		ConfigDTO configDto = configManagementClient.getConfigById(id);
		return toConfig(configDto);
	}

	private static Config toConfig(ConfigDTO configDto) {
		return new Config(configDto.id(), configDto.name(), configDto.value());
	}
}
