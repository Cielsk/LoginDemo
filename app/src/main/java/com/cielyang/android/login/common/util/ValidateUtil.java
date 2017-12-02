package com.cielyang.android.login.common.util;

import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;

/** 本项目专用工具类，用以验证用户输入内容. */
public class ValidateUtil {

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isShortPassword(CharSequence str) {
        return str.length() < 6;
    }

    public static boolean isValidPassword(CharSequence password) {
        // 不能包含空格
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Java 8 的 stream API，更高效
            return password.codePoints().noneMatch(Character::isWhitespace);
        } else {
            for (int i = 0; i < password.length(); i++) {
                if (Character.isWhitespace(password.charAt(i))) {
                    return false;
                }
            }
        }
        // 不能只使用数字
        return !TextUtils.isDigitsOnly(password);
    }

    public static boolean isValidUsername(Editable username) {
        // 英文字母或数字
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return username.codePoints().allMatch(Character::isLetterOrDigit);
        } else {
            for (int i = 0; i < username.length(); i++) {
                if (!Character.isLetterOrDigit(username.charAt(i))) {
                    return false;
                }
            }
        }

        return !TextUtils.isDigitsOnly(username);
    }
}
