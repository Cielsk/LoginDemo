package com.cielyang.android.login.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

/** */
public class SharedPreferencesUtils {

    private SharedPreferences mPreferences;

    public SharedPreferencesUtils(Context context, String fileName) {
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

    public void save(String key, Object o) {
        String value = new Gson().toJson(o);
        save(key, value);
    }

    public String get(String key) {
        return mPreferences.getString(key, null);
    }

    /**
     * 根据 key 获取对应的对象，对象由 Gson 解析而来.
     *
     * @param key the key
     * @param cls 对象类型信息
     * @return the object
     */
    public Object get(String key, Class<Object> cls) {
        String value = get(key);
        try {
            if (value != null) {
                return new Gson().fromJson(value, cls);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return null;
    }
}
