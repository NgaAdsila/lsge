package my.lsge.util;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import my.lsge.application.common.Const;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public final class Utils {
    private static final List<? extends DateTimeFormatter> DATE_FORMATS = //
            Arrays.asList(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"), //
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    public static <T> boolean isNullOrEmpty(List<T> t) {
        return t == null || t.isEmpty();
    }

    public static List<String> toListLowerCase(List<String> strs) {
        if (isNullOrEmpty(strs)) {
            return strs;
        }
        return strs.stream() //
                .filter(StringUtils::isNotEmpty) //
                .map(t -> t.toLowerCase().trim()) //
                .collect(Collectors.toList());
    }

    public static List<String> toListUpperCase(List<String> strs) {
        if (isNullOrEmpty(strs)) {
            return strs;
        }
        return strs.stream()
                .filter(StringUtils::isNotEmpty)
                .map(t -> t.toUpperCase().trim())
                .distinct()
                .collect(Collectors.toList());
    }

    public static Date getNow() {
        return Date.from(ZonedDateTime.now().toInstant());
    }

    public static Date getToday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getNow());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String formatDate(LocalDate date, String format) {
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern(format));
    }

    public static String formatDateTime(LocalDateTime source, String format) {
        if (source == null) {
            return "";
        }
        return source.format(DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime convertToDate(String source) {
        if (!StringUtils.isBlank(source)) {
            for (val fmt : DATE_FORMATS) {
                try {
                    return LocalDateTime.parse(source, fmt);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        return null;
    }

    public static <T> String joinList(List<T> list, String delimiter) {
        if (isNullOrEmpty(list))
            return "";
        if (delimiter == null) {
            delimiter = ",";
        }
        return list.stream().filter(Objects::nonNull).map(T::toString).collect(Collectors.joining(delimiter));
    }

    public static <T> List<T> removeNull(List<T> list) {
        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static String randomString(int length, String characters) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (characters.length() * Math.random());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    public static String randomPassword() {
        int avg = Math.floorDiv(Const.PASSWORD_LENGTH_DEFAULT, 4);
        return randomString(avg, Const.UPPERCASE_CHARATER)
                + randomString(avg, Const.NUMBERIC_CHARATER)
                + randomString(avg, Const.LOWERCASE_CHARATER)
                + randomString(avg, Const.SYMBOL_CHARATER);
    }

    public static boolean startWith(String str, String tag) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.toLowerCase().startsWith(tag.toLowerCase());
    }

    public static List<String> convertStringToList(String str, String delimiter) {
        if (StringUtils.isEmpty(str)) {
            return new ArrayList<>();
        }
        if (StringUtils.isEmpty(delimiter)) {
            delimiter = ",";
        }
        str = str.trim();
        if (!StringUtils.equals(str, StringUtils.EMPTY)) {
            return Arrays.stream(str.split(delimiter)) //
                    .filter(StringUtils::isNotEmpty) //
                    .map(String::trim) //
                    .distinct() //
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static boolean isNullOrEmptyObject(Object value) {
        return value == null || value == "";
    }

    public static boolean isValidEmail(String email) {
        return StringUtils.isNotEmpty(email) && //
                Pattern.compile(Const.EMAIL_VALID_REGEX).matcher(email).matches();
    }

    public static Long parseDateTimeToMilliSecond(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String randomDarkColor() {
        Color color = Color.getHSBColor(
                new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()).darker();
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()).toUpperCase();
    }

    public static String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }
}
