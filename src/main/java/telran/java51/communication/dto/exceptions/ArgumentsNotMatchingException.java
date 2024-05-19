package telran.java51.communication.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)

public class ArgumentsNotMatchingException extends RuntimeException {

	private static final long serialVersionUID = -3831370191954506908L;

	public ArgumentsNotMatchingException(String message) {
        super(message);
    }
}
