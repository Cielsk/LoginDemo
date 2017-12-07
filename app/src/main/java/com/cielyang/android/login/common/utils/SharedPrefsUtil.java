package com.cielyang.android.login.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/** */
public class SharedPrefsUtil {

    private SharedPreferences mPreferences;

    public SharedPrefsUtil(Context context, String fileName) {
        mPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * 保存字符串键值对.
     *
     * @param key   the key
     * @param value the value
     */
    public void save(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    public String get(String key) {
        return mPreferences.getString(key, null);
    }
}
