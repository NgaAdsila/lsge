package my.lsge.application.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class ResponseBody {
    private String message;

    private String statusCode;

    private int statusCodeValue;

    private String internal;

    /**
     * @param req
     *            HttpServletRequest
     * @param e
     *            Exception
     * @param status
     *            HttpStatus
     * @return ResponseBody
     */
    public static ResponseBody of(HttpServletRequest req, Exception e, HttpStatus status) {
        ResponseBody body = new ResponseBody();
        body.setMessage(e.getMessage());
        body.setStatusCode(status.name());
        body.setStatusCodeValue(status.value());
        body.setInternal(req.getRequestURI());

        return body;
    }

    /**
     * @param req
     *            HttpServletRequest
     * @param message
     *            String
     * @param status
     *            HttpStatus
     * @return ResponseBody
     */
    public static ResponseBody of(HttpServletRequest req, String message, HttpStatus status) {
        ResponseBody body = new ResponseBody();
        body.setMessage(message);
        body.setStatusCode(status.name());
        body.setStatusCodeValue(status.value());
        body.setInternal(req.getRequestURI());

        return body;
    }
}
