package my.lsge.domain.enums;

import java.util.Arrays;
import java.util.List;

public enum SortTypeEnum {
    DESC,
    ASC;

    public static List<String> toList() {
        return Arrays.asList(SortTypeEnum.ASC.name(), SortTypeEnum.DESC.name());
    }
}
