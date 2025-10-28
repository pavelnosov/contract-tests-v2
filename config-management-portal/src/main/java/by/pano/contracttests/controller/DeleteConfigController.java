package by.pano.contracttests.controller;

import by.pano.contracttests.command.DeleteConfigCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;


@Controller
@RequestMapping("/config/delete")
@RequiredArgsConstructor
class DeleteConfigController {

	private final ObjectMapper objectMapper;
	private final DeleteConfigCommand deleteConfigCommand;

	@DeleteMapping("/{configId}")
	public MappingJackson2JsonView save(@PathVariable String configId, ModelMap model) {
		try {
			deleteConfigCommand.execute(configId);
			model.addAttribute("success", true);
		} catch (Exception e) {
			model.addAttribute("success", false);
		}

		return new MappingJackson2JsonView(objectMapper);
	}


}
