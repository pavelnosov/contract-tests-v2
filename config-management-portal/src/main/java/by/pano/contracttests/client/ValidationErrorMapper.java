package by.pano.contracttests.client;

import java.util.List;
import java.util.Map;

import by.pano.contracttests.model.ValidationError;
import org.springframework.stereotype.Component;


@Component
public class ValidationErrorMapper {

	private static final Map<String, String> MAPPING = Map.of(
		"id", "id",
		"name", "name",
		"value", "value"
	);
	public List<ValidationError> map(List<Error> errors) {
		return errors.stream()
					 .map(error -> new ValidationError(MAPPING.getOrDefault(error.field(), error.field()), error.message()))
					 .toList();
	}

}
