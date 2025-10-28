package by.pano.contracttests.resource;

import java.time.Instant;

import by.pano.contracttests.model.Config;


public record ConfigManagementDTO(String id, String name, String value, Instant version) {
	public Config toModel() {
		return new Config(id, name, value, version);
	}

	public static ConfigManagementDTO from(Config config) {
		return new ConfigManagementDTO(config.id(), config.name(), config.value(), config.version());
	}
}
