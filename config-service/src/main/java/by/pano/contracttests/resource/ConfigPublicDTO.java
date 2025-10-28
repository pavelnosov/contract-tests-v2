package by.pano.contracttests.resource;

import by.pano.contracttests.model.Config;


public record ConfigPublicDTO(String id, String name, String value) {
	public static ConfigPublicDTO from(Config config) {
		return new ConfigPublicDTO(config.id(), config.name(), config.value());
	}
}
