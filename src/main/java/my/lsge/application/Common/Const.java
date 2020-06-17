package my.lsge.application.Common;

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
}
