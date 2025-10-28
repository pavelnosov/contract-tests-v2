package by.pano.contracttests.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ConfigUpdatedConsumer extends EventConsumer<ConfigUpdated> {

	@Override
	public void handleMessage(ConfigUpdated event) {
		log.info("Received config updated event: {}", event);
	}
}
