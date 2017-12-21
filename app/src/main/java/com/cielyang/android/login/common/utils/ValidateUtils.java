package com.cielyang.android.login.common.utils;

import java.util.regex.Pattern;

/** 本项目专用工具类，用以验证用户输入内容. */
public class ValidateUtils {

    public static final Pattern EMAIL_ADDRESS =
            Pattern.compile(
                    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
                            + "\\@"
                            + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"
                            + "("
                            + "\\."
                            + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"
                            + ")+");

    private ValidateUtils() {
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isShortPassword(CharSequence str) {
        return str.length() < 6;
    }

    public static boolean isValidPassword(CharSequence password) {
        // 不能包含空格
        for (int i = 0; i < password.length(); i++) {
            if (Character.isWhitespace(password.charAt(i))) {
                return false;
            }
        }
        // 不能只使用数字
        return !TextUtils.isDigitsOnly(password);
    }

    public static boolean isValidUsername(CharSequence username) {
        // 英文字母或数字
        for (int i = 0; i < username.length(); i++) {
            if (!Character.isLetterOrDigit(username.charAt(i))) {
                return false;
            }
        }

        return !TextUtils.isDigitsOnly(username);
    }
}
