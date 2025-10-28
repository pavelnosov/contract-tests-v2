package by.pano.contracttests.model;

import java.time.Instant;

import by.pano.contracttests.persistence.ConfigEntity;


public record Config(String id, String name, String value, Instant version) {

	public static Config from(ConfigEntity entity) {
		return new Config(entity.getId(), entity.getName(), entity.getValue(), entity.getUpdatedAt());
	}
}
