package my.lsge.application.exception;

import lombok.Getter;
import lombok.Setter;

import javax.validation.ValidationException;

@Getter
@Setter
public class FormValidationException extends ValidationException {
    public FormValidationException() {
        super();
    }

    public FormValidationException(String message) {
        super(message);
    }

    public FormValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormValidationException(Throwable cause) {
        super(cause);
    }
}
