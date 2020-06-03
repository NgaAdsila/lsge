package my.lsge.controller;

import lombok.extern.slf4j.Slf4j;
import my.lsge.util.ObjectUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@Slf4j
public abstract class BaseController {

    protected Long getUserId() {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new RuntimeException("Unauthorized!");
        }
        try {
            Map<String, Object> principalMap = ObjectUtils.getFieldNamesAndValues(auth.getPrincipal(), false);
            return Long.parseLong(principalMap.get("id").toString());
        } catch (Exception e) {
            throw new RuntimeException("Unauthorized!");
        }
    }
}
