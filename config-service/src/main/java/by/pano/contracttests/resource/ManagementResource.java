package by.pano.contracttests.resource;

import java.util.List;

import by.pano.contracttests.command.CreateConfigCommand;
import by.pano.contracttests.command.UpdateConfigCommand;
import by.pano.contracttests.model.Config;
import by.pano.contracttests.query.GetConfigQuery;
import by.pano.contracttests.query.GetConfigsQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/management/v1/configs")
@RequiredArgsConstructor
public class ManagementResource {

	private final GetConfigQuery getConfigQuery;
	private final GetConfigsQuery getConfigsQuery;
	private final CreateConfigCommand createConfigCommand;
	private final UpdateConfigCommand updateConfigCommand;

	@GetMapping("/{id}")
	public ResponseEntity<ConfigManagementDTO> getConfig(@PathVariable String id) {
		Config config = getConfigQuery.getConfig(id);
		if (config == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return new ResponseEntity<>(ConfigManagementDTO.from(config), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<ConfigManagementDTO>> getConfigs() {
		List<ConfigManagementDTO> configs = getConfigsQuery.getConfigs().stream()
														   .map(ConfigManagementDTO::from)
														   .toList();
		return new ResponseEntity<>(configs, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteConfig(@PathVariable String id) {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}


	@PostMapping
	public ResponseEntity<ConfigManagementDTO> createConfigs(@RequestBody ConfigManagementDTO configManagementDTO) {
		Config config = createConfigCommand.exacute(configManagementDTO.toModel());
		return new ResponseEntity<>(ConfigManagementDTO.from(config), HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<ConfigManagementDTO> updateConfigs(@RequestBody ConfigManagementDTO configManagementDTO) {
		Config config = updateConfigCommand.exacute(configManagementDTO.toModel());
		return new ResponseEntity<>(ConfigManagementDTO.from(config), HttpStatus.CREATED);
	}

}
