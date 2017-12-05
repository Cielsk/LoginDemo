package com.cielyang.android.login.common.utils;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/** */
public class ToastUtils {

    private ToastUtils() {
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
}
