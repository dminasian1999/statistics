package telran.java51.communication.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OutOfBoundaryException extends RuntimeException {

	private static final long serialVersionUID = 2088447411787497990L;

	public OutOfBoundaryException(String message) {
        super(message);
    }
}
