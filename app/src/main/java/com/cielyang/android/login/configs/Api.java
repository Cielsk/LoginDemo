package com.cielyang.android.login.configs;

import com.cielyang.android.login.BuildConfig;

/** */
public class Api {

    public static final String QUERY_PARAM_NAME = "condition";
    // 注册
    private static final String REGISTER = "/users";
    // 登录
    private static final String LOGIN = "/login";
    // token 登录
    private static final String LOGIN_BY_TOKEN = "/users/me";
    // 查询户是否存在
    // 传入的条件是 JSON 字符串
    private static final String QUERY_USER = "/users?where={condition}";
    // base sDomain
    private static final String DEBUG_DOMAIN = "https://ikg5f8q7.api.lncld.net/1.1";
    private static final String RElEASE_DOMAIN = "https://ikg5f8q7.api.lncld.net/1.1";
    // app id
    private static final String DEBUG_APP_ID = "IKG5f8Q72UVbht3p31i4GOH9-gzGzoHsz";
    private static final String RELEASE_APP_ID = "IKG5f8Q72UVbht3p31i4GOH9-gzGzoHsz";
    // app key
    private static final String DEBUG_APP_KEY = "ff5d9daWihvXEIYapNE4u7xJ";
    private static final String RELEASE_APP_KEY = "ff5d9daWihvXEIYapNE4u7xJ";
    private static String sAppId = DEBUG_APP_ID;
    private static String sAppKey = DEBUG_APP_KEY;
    private static String sDomain = DEBUG_DOMAIN;

    static {
        setDebug(BuildConfig.DEBUG);
    }

    /**
     * 根据应用是否处于 debug 环境而调整网络设置.
     *
     * @param isDebug 是否处于 debug 环境
     */
    public static void setDebug(boolean isDebug) {
        sDomain = isDebug ? DEBUG_DOMAIN : RElEASE_DOMAIN;
        sAppId = isDebug ? DEBUG_APP_ID : RELEASE_APP_ID;
        sAppKey = isDebug ? DEBUG_APP_KEY : RELEASE_APP_KEY;
    }

    public static String getDomain() {
        return sDomain;
    }

    public static String getAppId() {
        return sAppId;
    }

    public static String getAppKey() {
        return sAppKey;
    }

    public static String getRegisterUrl() {
        return sDomain + REGISTER;
    }

    public static String getLoginUrl() {
        return sDomain + LOGIN;
    }

    public static String getLoginByTokenUrl() {
        return sDomain + LOGIN_BY_TOKEN;
    }

    /**
     * 使用 LeanCloud restful api 条件查询用户是否存在.
     *
     * @return the query user url
     * @see <a href="https://leancloud.cn/docs/rest_api.html#hash827796182">LeanCloud 文档地址</a>
     */
    public static String getQueryUserUrl() {
        return sDomain + QUERY_USER;
    }
}
