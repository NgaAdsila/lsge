package my.lsge.domain.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum UserRoleEnum {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private String title;

    UserRoleEnum(String title) {
        this.title = title;
    }

    public static List<String> toList() {
        return Arrays.asList(ROLE_ADMIN.name(), ROLE_USER.name());
    }

    public String getTitle() {
        return this.title;
    }
}