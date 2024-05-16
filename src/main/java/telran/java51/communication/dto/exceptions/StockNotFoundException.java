package telran.java51.communication.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StockNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5902615205414603803L;
	public StockNotFoundException(String message) {
        super(message);
    }
	
}
