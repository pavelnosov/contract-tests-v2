package by.pano.contracttests.model;

import java.io.Serial;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class ValidationException extends RuntimeException{

	@Serial
	private static final long serialVersionUID = -6L;

	private final List<ValidationError> errors;
}
