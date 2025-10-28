package by.pano.contracttests.controller;

import java.util.List;
import java.util.Map;

import by.pano.contracttests.model.ValidationError;
import org.springframework.stereotype.Component;


@Component
class ValidationErrorsViewMapper {

	private static final Map<String, String> CONFIG_SAVE_FIELDS = Map.of(
			"id", "configId",
			"name", "configName",
			"value", "configValue"
	);
	public ValidationErrorsView mapConfigSaveErrors(List<ValidationError> errors) {
		List<ValidationErrorView> errorViews = errors.stream()
													 .map(error -> new ValidationErrorView(CONFIG_SAVE_FIELDS.getOrDefault(error.field(), error.field()), error.message())).toList();
		return new ValidationErrorsView(errorViews);
	}

}
