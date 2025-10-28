package by.pano.contracttests.command;

import by.pano.contracttests.event.ConfigUpdatePublisher;
import by.pano.contracttests.model.Config;
import by.pano.contracttests.persistence.ConfigEntity;
import by.pano.contracttests.persistence.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UpdateConfigCommand {

	private final ConfigRepository configRepository;
	private final ConfigUpdatePublisher configUpdatePublisher;

	public Config exacute(Config config) {
		return configRepository.findById(config.id())
							   .map(entity -> {
								   entity.setId(config.id());
								   entity.setName(config.name());
								   entity.setValue(config.value());
								   entity.setUpdatedAt(config.version());
								   ConfigEntity savedEntity = configRepository.save(entity);
								   configUpdatePublisher.publish(config.id());
								   return savedEntity;

							   })
							   .map(Config::from)
							   .orElseThrow(() -> new CommandException("Config with given key doesn't exist"));
	}

}
