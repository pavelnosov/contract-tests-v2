package by.pano.contracttests.controller;

import java.util.List;

import by.pano.contracttests.model.Config;
import by.pano.contracttests.query.GetConfigQuery;
import by.pano.contracttests.query.GetConfigsQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/config")
@RequiredArgsConstructor
class ConfigController {

	private static final String LIST_VIEW_NAME = "config/list.html";
	private static final String EDIT_VIEW_NAME = "config/edit.html";

	private final GetConfigQuery getConfigQuery;
	private final GetConfigsQuery getConfigsQuery;

	@GetMapping
	public ModelAndView listConfigs() {
		ModelAndView modelAndView = new ModelAndView(LIST_VIEW_NAME);
		List<Config> configs = getConfigsQuery.execute();
		modelAndView.addObject("configs", configs);
		return modelAndView;
	}
	@GetMapping("/edit/{configId}")
	public ModelAndView editConfig(@PathVariable String configId) {
		ModelAndView modelAndView = new ModelAndView(EDIT_VIEW_NAME);
		Config config = getConfigQuery.execute(configId);
		modelAndView.addObject("config", config);
		return modelAndView;
	}

	@GetMapping("/add")
	public ModelAndView editConfig() {
		ModelAndView modelAndView = new ModelAndView(EDIT_VIEW_NAME);
		return modelAndView;
	}

}
