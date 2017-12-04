package com.cielyang.android.login.data.local;

import static com.cielyang.android.login.configs.SharedPreferencesConfig.TOKEN_KEY;

import com.cielyang.android.login.common.utils.SharedPreferencesUtils;
import com.cielyang.android.login.data.entities.Account;

/** */
public class SessionDao {

    private SharedPreferencesUtils mSharedPreferencesUtils;

    public SessionDao(SharedPreferencesUtils sharedPreferencesUtils) {
        mSharedPreferencesUtils = sharedPreferencesUtils;
    }

    public void saveSession(Account account) {
        mSharedPreferencesUtils.save(TOKEN_KEY, account.getSessionToken());
    }

    public String getSessionToken() {
        return mSharedPreferencesUtils.get(TOKEN_KEY);
    }
}
