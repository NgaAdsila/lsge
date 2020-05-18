package my.lsge.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Not Acceptable")
public class NotAcceptableException extends BaseException {

    public NotAcceptableException() {
    }

    public NotAcceptableException(String message) {
        super(message);
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
