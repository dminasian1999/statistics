package telran.java51.communication.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)

public class TypeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -4031282640467816263L;

	public TypeNotFoundException(String message) {
        super(message);
    }
}
