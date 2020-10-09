package my.lsge.domain.enums;

import java.util.Arrays;
import java.util.List;

public enum UserRoleEnum {
    ROLE_USER,
    ROLE_ADMIN;

    public static List<String> toList() {
        return Arrays.asList(ROLE_ADMIN.name(), ROLE_USER.name());
    }
}