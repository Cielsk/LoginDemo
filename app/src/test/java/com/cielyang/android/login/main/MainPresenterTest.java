package com.cielyang.android.login.main;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cielyang.android.login.data.AccountManager;
import com.cielyang.android.login.data.entities.Account;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** */
public class MainPresenterTest {

    @Mock
    private AccountManager mAccountManager;
    @Mock
    private MainContract.View mView;
    @Mock
    private Account mAccount;

    private MainPresenter mMainPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainPresenter = new MainPresenter(mAccountManager);
    }

    @Test
    public void bindView() throws Exception {
        when(mAccount.getUsername()).thenReturn("john");
        when(mAccount.getEmail()).thenReturn("john@test.com");
        when(mAccountManager.getAccount()).thenReturn(mAccount);

        mMainPresenter.bindView(mView);

        verify(mAccountManager).getAccount();
        verify(mView).showUserName(anyString());
        verify(mView).showEmail(anyString());
    }
}
