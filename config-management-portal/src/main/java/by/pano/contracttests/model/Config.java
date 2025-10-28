package by.pano.contracttests.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.thymeleaf.util.StringUtils;

public record Config(String id, String name, String value) {

	public Config(String id, String name, String value) {
		List<ValidationError> errors = new ArrayList<>();

		this.id = validateId(id, errors::add);
		this.name = validateName(name, errors::add);
		this.value = validateValue(value, errors::add);

		if (!errors.isEmpty()) {
			throw new ValidationException(errors);
		}
	}

	private static String validateId(String id, Consumer<ValidationError> errorConsumer) {
		if (StringUtils.isEmpty(id)) {
			errorConsumer.accept(new ValidationError("id", "Id should not be empty"));
		}
		return id;
	}

	private static String validateName(String name, Consumer<ValidationError> errorConsumer) {
		if (StringUtils.isEmpty(name)) {
			errorConsumer.accept(new ValidationError("name", "Name should not be empty"));
		}
		return name;
	}

	private static String validateValue(String value, Consumer<ValidationError> errorConsumer) {
		if (StringUtils.isEmpty(value)) {
			errorConsumer.accept(new ValidationError("value", "Value should not be empty"));
		}
		return value;
	}
}
