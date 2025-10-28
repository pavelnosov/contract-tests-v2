package by.pano.contracttests.controller;

import by.pano.contracttests.command.SaveConfigCommand;
import by.pano.contracttests.model.Config;
import by.pano.contracttests.model.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;


@Controller
@RequestMapping("/config/save")
@RequiredArgsConstructor
class SaveConfigController {

	private final ObjectMapper objectMapper;
	private final SaveConfigCommand saveConfigCommand;
	private final ValidationErrorsViewMapper validationErrorsViewMapper;

	@PostMapping
	public MappingJackson2JsonView save(@RequestBody SaveConfigRequest configRequest, ModelMap model) {

		try {
			Config config = new Config(configRequest.id(), configRequest.name(), configRequest.value());
			saveConfigCommand.execute(config);
			model.addAttribute("success", true);
			model.addAttribute("redirectUrl", "/config");
		} catch (ValidationException e) {
			model.addAttribute("success", false);
			model.addAttribute("validatorError", validationErrorsViewMapper.mapConfigSaveErrors(e.getErrors()));
		}

		return new MappingJackson2JsonView(objectMapper);
	}


}
