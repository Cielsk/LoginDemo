package com.cielyang.android.login.common.utils;

/**
 *
 */
public class TextUtils {

    private TextUtils() {
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isDigitsOnly(CharSequence str) {
        int i;
        for (i = 0; i < str.length() && Character.isDigit(str.charAt(i)); i++) ;
        return i == str.length();
    }
}
