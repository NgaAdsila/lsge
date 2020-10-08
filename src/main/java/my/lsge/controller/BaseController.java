package my.lsge.controller;

import lombok.extern.slf4j.Slf4j;
import my.lsge.util.ObjectUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@Slf4j
public abstract class BaseController {

    protected Long getUserId() {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new BadCredentialsException("Unauthorized!");
        }
        try {
            Map<String, Object> principalMap = ObjectUtils.getFieldNamesAndValues(auth.getPrincipal(), false);
            if (principalMap.get("id") == null) {
                throw new BadCredentialsException("Unauthorized!");
            }
            return Long.parseLong(principalMap.get("id").toString());
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
