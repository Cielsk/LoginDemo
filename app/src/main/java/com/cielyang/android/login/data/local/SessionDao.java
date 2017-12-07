package com.cielyang.android.login.data.local;

import static com.cielyang.android.login.configs.SharedPreferencesConfig.TOKEN_KEY;

import com.cielyang.android.login.common.utils.SharedPrefsUtil;
import com.cielyang.android.login.data.entities.Account;

import javax.inject.Inject;
import javax.inject.Singleton;

/** */
@Singleton
public class SessionDao {

    private SharedPrefsUtil mSharedPrefsUtil;

    @Inject
    public SessionDao(SharedPrefsUtil sharedPrefsUtil) {
        mSharedPrefsUtil = sharedPrefsUtil;
    }

    public void saveSession(Account account) {
        mSharedPrefsUtil.save(TOKEN_KEY, account.getSessionToken());
    }

    public String getSessionToken() {
        return mSharedPrefsUtil.get(TOKEN_KEY);
    }
}
