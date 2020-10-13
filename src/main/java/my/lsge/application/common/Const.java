package my.lsge.application.common;

import java.util.Arrays;
import java.util.List;

public class Const {

    public static final int PAGE_DEFAULT = 1;

    public static final int LIMIT_DEFAULT = 50;

    public static final String EMAIL_VALID_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    public static final String DB_DATE_FORMAT = "yyyy-MM-dd";

    public static final String DISPLAY_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static final int PASSWORD_LENGTH_DEFAULT = 8;

    public static final String LOWERCASE_CHARATER = "abcdefghijklmnopqrstuvxyz";

    public static final String UPPERCASE_CHARATER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String NUMBERIC_CHARATER = "0123456789";

    public static final String SYMBOL_CHARATER = "!\"#$%&'()*+,-./:;<=>?@[]^_`{|}~";

    public static final String ALPHA_NUMBERIC_CHARATER = UPPERCASE_CHARATER + NUMBERIC_CHARATER + LOWERCASE_CHARATER;

    public static final List<String> TEXT_FILE_EXTENSIONS = Arrays.asList(
            "txt", "md",
            "csv", "ssh", "pem",
            "yml", "yaml", "properties",
            "asp", "aspx", "cer", "cfm", "cgi", "pl", "css", "scss", "sass", "htm", "html", "phtml",
            "js", "jsp", "part", "py", "rss", "xhtml", "json", "ts",
            "php", "vue", "java", "c", "class", "cpp", "h", "sh", "swift", "vb",
            "sql", "dat", "db", "dbf", "mdb", "log", "sav", "xml"
    );

    public static final long ONE_DAY_TIME_IN_MS = 86400000;
}
