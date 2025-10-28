package by.pano.contracttests.command;

import by.pano.contracttests.event.ConfigUpdatePublisher;
import by.pano.contracttests.model.Config;
import by.pano.contracttests.persistence.ConfigEntity;
import by.pano.contracttests.persistence.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CreateConfigCommand {

	private final ConfigRepository configRepository;
	private final ConfigUpdatePublisher configUpdatePublisher;

	public Config exacute(Config config) {
		configRepository.findById(config.id())
						.ifPresent(configEntity -> {
							throw new CommandException("Config with given key already exists");
						});

		ConfigEntity entity = ConfigEntity.builder()
										  .id(config.id())
										  .name(config.name())
										  .value(config.value())
										  .build();
		ConfigEntity saveEntity = configRepository.save(entity);
		configUpdatePublisher.publish(config.id());

		return Config.from(saveEntity);
	}

}
