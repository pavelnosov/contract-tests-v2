package by.pano.contracttests.resource;

import java.util.List;

import by.pano.contracttests.query.GetConfigsQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/public/v1/configs")
@RequiredArgsConstructor
public class PublicResource {

	private final GetConfigsQuery getConfigsQuery;

	@GetMapping
	public ResponseEntity<List<ConfigPublicDTO>> getConfigs() {
		List<ConfigPublicDTO> configs = getConfigsQuery.getConfigs().stream()
														   .map(ConfigPublicDTO::from)
														   .toList();
		return new ResponseEntity<>(configs, HttpStatus.OK);
	}

}
