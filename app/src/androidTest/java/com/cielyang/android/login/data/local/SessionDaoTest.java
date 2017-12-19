package com.cielyang.android.login.data.local;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.cielyang.android.login.common.utils.SharedPrefsUtil;
import com.cielyang.android.login.data.entities.Account;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class SessionDaoTest {

    private Account mAccount;
    private SessionDao mSessionDao;

    @Before
    public void setUp() throws Exception {
        mAccount = new Account();
        mAccount.setSessionToken("testSessionTokenInfo");
        Context context = InstrumentationRegistry.getTargetContext();
        SharedPrefsUtil sharedPrefsUtil = new SharedPrefsUtil(context, "test");
        mSessionDao = new SessionDao(sharedPrefsUtil);
    }

    @After
    public void tearDown() throws Exception {
        mSessionDao = null;
    }

    @Test
    public void getSessionToken_afterSaving() {
        mSessionDao.saveSession(mAccount);

        String token = mSessionDao.getSessionToken();
        assertEquals(token, mAccount.getSessionToken());
    }
}
