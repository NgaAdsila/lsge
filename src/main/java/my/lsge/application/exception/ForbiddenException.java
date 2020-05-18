package my.lsge.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Forbidden")
public class ForbiddenException extends BaseException {

    public ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
