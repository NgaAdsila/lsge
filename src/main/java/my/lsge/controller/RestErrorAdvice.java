package my.lsge.controller;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.exception.ForbiddenException;
import my.lsge.application.exception.FormValidationException;
import my.lsge.application.exception.NotAcceptableException;
import my.lsge.application.exception.NotFoundException;
import my.lsge.application.security.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class RestErrorAdvice {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmer);
        Object obj = binder.getTarget();
        if (obj == null) {
            return;
        }
        List<Field> fields = new ArrayList<>(Arrays.asList(obj.getClass().getDeclaredFields()));
        if (obj.getClass().getSuperclass() != null) {
            fields.addAll(Arrays.asList(obj.getClass().getSuperclass().getDeclaredFields()));
        }
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (value instanceof String) {
                    field.set(obj, ((String) value).replaceAll("(^\\p{Z}+|\\p{Z}+$)", ""));
                } else if (value instanceof Collection<?>) {
                    ArrayList list = (ArrayList) value;
                    for (Object el : list) {
                        List<Field> fieldChildren = new ArrayList<>(Arrays.asList(el.getClass().getDeclaredFields()));
                        if (obj.getClass().getSuperclass() != null) {
                            fieldChildren.addAll(Arrays.asList(el.getClass().getSuperclass().getDeclaredFields()));
                        }
                        loop3 : for (Field fieldChild : fieldChildren) {
                            fieldChild.setAccessible(true);
                            Object valueChild = fieldChild.get(el);
                            if (valueChild instanceof String) {
                                fieldChild.set(el, ((String) valueChild).replaceAll("(^\\p{Z}+|\\p{Z}+$)", ""));
                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ResponseBody> handleServletRequestBindingException(HttpServletRequest req, ServletRequestBindingException e) {
        log.warn(e.getMessage(), e);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseBody body = ResponseBody.of(req, e, status);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseBody> handleAccessDeniedException(HttpServletRequest req, AccessDeniedException e) {
        log.warn(e.getMessage());
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ResponseBody body = ResponseBody.of(req, "Unauthorized: Access denied", status);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseBody> handleBadCredentialsException(HttpServletRequest req, BadCredentialsException e) {
        log.warn(e.getMessage());
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ResponseBody body = ResponseBody.of(req, "Unauthorized: Bad credential", status);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseBody> handleConstraintViolationException(HttpServletRequest req, ConstraintViolationException e) {
        log.warn(e.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseBody body = ResponseBody.of(req, e, status);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResponseBody> handleForbiddenException(HttpServletRequest req, ForbiddenException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ResponseBody body = ResponseBody.of(req, "Forbidden Exception", status);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(NotAcceptableException.class)
    public ResponseEntity<ResponseBody> handleNotAcceptableException(HttpServletRequest req, NotAcceptableException e) {
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        ResponseBody body = ResponseBody.of(req, e, status);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseBody> handleNotFoundException(HttpServletRequest req, NotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message;
        if (StringUtils.isEmpty(e.getMessage())) {
            message = "Not found";
        } else {
            message = e.getMessage();
        }
        ResponseBody body = ResponseBody.of(req, message, status);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(FormValidationException.class)
    public ResponseEntity<ResponseBody> handleValidationException(HttpServletRequest req, FormValidationException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseBody body = ResponseBody.of(req, e, status);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseBody> handleBindException(HttpServletRequest req, BindException e) {
        log.warn(e.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseBody body = ResponseBody.of(req, e, status);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ResponseBody> handleIOException(HttpServletRequest req, IOException e) {
        if (e.getMessage().contains("Broken pipe")) {
            log.info("client connection was unexpected closing. (broken pipe)");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return handleException(req, e);
        }
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ResponseBody> handleResourceAccessException(HttpServletRequest req, ResourceAccessException e) {
        log.warn(e.getMessage());
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseBody body = ResponseBody.of(req, "Exception", status);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBody> handleException(HttpServletRequest req, Exception e) {
        log.error(e.getMessage(), e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseBody body = ResponseBody.of(req, "Exception", status);
        return new ResponseEntity<>(body, status);
    }
}
