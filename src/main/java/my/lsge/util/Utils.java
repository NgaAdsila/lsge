package my.lsge.util;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import my.lsge.application.Common.Const;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Slf4j
public final class Utils {
    private static final List<? extends DateTimeFormatter> DATE_FORMATS = //
            Arrays.asList(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"), //
                    DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"), DateTimeFormat.forPattern("yyyy-MM-dd"));

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

    public static String getUTCDateTimeString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(date);
    }

    public static Date addMonths(Date source, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(source);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    public static Date addDate(Date source, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(source);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public static Date getAfterMinutes(Date source, int minutes) {
        Calendar c = Calendar.getInstance();
        c.setTime(source);
        c.add(Calendar.MINUTE, minutes);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static String formatDate(Date date, int timezoneOffset, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        date = new Date(date.getTime() + (date.getTimezoneOffset() - timezoneOffset) * 60 * 1000);
        return sdf.format(date);
    }

    public static Date convertToDate(String source) {
        if (!StringUtils.isBlank(source)) {
            for (val fmt : DATE_FORMATS) {
                try {
                    val d = fmt.parseLocalDateTime(source);
                    return d.toDate();
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

    public static String randomCode(int length) {
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (alphaNumericString.length() * Math.random());
            sb.append(alphaNumericString.charAt(index));
        }
        return sb.toString();
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
}
