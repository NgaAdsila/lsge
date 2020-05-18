package my.lsge.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
public class NotFoundException extends BaseException {

    public NotFoundException() {

    }

    public NotFoundException(String message) {
        super(message);
    }

    private static final long serialVersionUID = 1L;
}
