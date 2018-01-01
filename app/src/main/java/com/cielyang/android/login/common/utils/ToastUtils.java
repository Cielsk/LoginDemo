package com.cielyang.android.login.common.utils;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import es.dmoral.toasty.Toasty;

/** */
public class ToastUtils {

    public static final int NORMAL = 0;
    public static final int INFO = 1;
    public static final int WARNING = 2;
    public static final int SUCCESS = 3;
    public static final int ERROR = 4;

    private ToastUtils() {
    }

    public static void msg(Context context, @StringRes int resId, @ToastLevel int level) {
        String msg = context.getString(resId);
        switch (level) {
            case NORMAL:
                normal(context, msg);
                break;
            case INFO:
                info(context, msg);
                break;
            case WARNING:
                warning(context, msg);
                break;
            case SUCCESS:
                success(context, msg);
                break;
            case ERROR:
                error(context, msg);
                break;
            default:
                break;
        }
    }

    public static void error(Context context, CharSequence msg) {
        Toasty.error(context, msg, Toast.LENGTH_SHORT, true).show();
    }

    public static void success(Context context, CharSequence msg) {
        Toasty.success(context, msg, Toast.LENGTH_SHORT, true).show();
    }

    public static void info(Context context, CharSequence msg) {
        Toasty.info(context, msg, Toast.LENGTH_SHORT, true).show();
    }

    public static void warning(Context context, CharSequence msg) {
        Toasty.warning(context, msg, Toast.LENGTH_SHORT, true).show();
    }

    public static void normal(Context context, CharSequence msg) {
        Toasty.normal(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {NORMAL, INFO, WARNING, SUCCESS, ERROR})
    public @interface ToastLevel {
    }
}
