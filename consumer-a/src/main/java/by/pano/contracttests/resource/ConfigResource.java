package by.pano.contracttests.resource;

import java.util.List;

import by.pano.contracttests.client.ConfigDTO;
import by.pano.contracttests.client.ConfigClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/configs")
@RequiredArgsConstructor
class ConfigResource {

	private final ConfigClient configClient;

	@GetMapping
	List<ConfigDTO> getConfigs() {
		return configClient.getConfigs();
	}

}
