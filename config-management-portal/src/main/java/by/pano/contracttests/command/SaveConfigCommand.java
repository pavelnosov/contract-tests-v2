package by.pano.contracttests.command;

import by.pano.contracttests.client.ConfigDTO;
import by.pano.contracttests.client.ConfigManagementClient;
import by.pano.contracttests.model.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SaveConfigCommand {

	private final ConfigManagementClient configManagementClient;

	public void execute(Config config) {
		ConfigDTO configDto = configManagementClient.getConfigById(config.id());

		if (configDto == null) {
			configManagementClient.create(toConfigDTO(config));
		} else {
			configManagementClient.update(extendConfigDTO(configDto, config));
		}
	}

	private ConfigDTO extendConfigDTO(ConfigDTO configDto, Config config) {
		return new ConfigDTO(config.id(), config.name(), config.value(), configDto.version());
	}

	private ConfigDTO toConfigDTO(Config config) {
		return new ConfigDTO(config.id(), config.name(), config.value(), null);
	}

}
