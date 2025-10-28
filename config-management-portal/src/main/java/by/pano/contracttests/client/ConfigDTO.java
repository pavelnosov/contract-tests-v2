package by.pano.contracttests.client;

import java.time.Instant;


public record ConfigDTO(String id, String name, String value, Instant version) {

}
