package com.cielyang.android.login.common.util;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/** */
public class ToastUtil {

    public void error(Context context, CharSequence msg) {
        Toasty.error(context, msg, Toast.LENGTH_SHORT, true).show();
    }

    public void success(Context context, CharSequence msg) {
        Toasty.success(context, msg, Toast.LENGTH_SHORT, true).show();
    }

    public void info(Context context, CharSequence msg) {
        Toasty.info(context, msg, Toast.LENGTH_SHORT, true).show();
    }

    public void warning(Context context, CharSequence msg) {
        Toasty.warning(context, msg, Toast.LENGTH_SHORT, true).show();
    }

    public void normal(Context context, CharSequence msg) {
        Toasty.normal(context, msg, Toast.LENGTH_SHORT).show();
    }
}
