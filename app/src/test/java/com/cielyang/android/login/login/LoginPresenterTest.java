package com.cielyang.android.login.login;

import static org.mockito.Mockito.verify;

import com.cielyang.android.login.data.AccountManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** */
public class LoginPresenterTest {

    private static final String EMAil = "john@test.com";
    private static final String PASSWORD = "qwerty";
    @Mock
    private AccountManager mAccountManager;
    @Mock
    private LoginContract.View mView;
    private LoginPresenter mLoginPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mLoginPresenter = new LoginPresenter(mAccountManager);
        mLoginPresenter.bindView(mView);
    }

    @After
    public void tearDown() throws Exception {
        mLoginPresenter.unbindView();
    }

    @Test
    public void checkEmail_valid() throws Exception {
        mLoginPresenter.checkEmail(EMAil);

        verify(mView).clearEmailError();
    }

    @Test
    public void checkEmail_empty() throws Exception {
        mLoginPresenter.checkEmail(null);

        verify(mView).clearEmailError();
        verify(mView).errorEmptyEmail();
    }

    @Test
    public void checkEmail_invalid() throws Exception {
        mLoginPresenter.checkEmail("test");

        verify(mView).clearEmailError();
        verify(mView).errorInvalidEmail();
    }

    @Test
    public void checkPassword_valid() throws Exception {
        mLoginPresenter.checkPassword(PASSWORD);

        verify(mView).clearPasswordError();
        verify(mView).setPasswordToggleEnabled(true);
    }

    @Test
    public void checkPassword_empty() throws Exception {
        mLoginPresenter.checkPassword(null);

        verify(mView).clearPasswordError();
        verify(mView).errorEmptyPassword();
        verify(mView).setPasswordToggleEnabled(false);
    }

    @Test
    public void loginByEmail() throws Exception {
        mLoginPresenter.loginByEmail(EMAil, PASSWORD);

        verify(mView).showLoadingIndicator(true);
        verify(mView).setLoginBtnEnabled(false);
        verify(mAccountManager).loginByEmail(EMAil, PASSWORD, mLoginPresenter);
    }

    @Test
    public void onLoginSucceed() throws Exception {
        mLoginPresenter.onLoginSucceed();

        verify(mView).showLoadingIndicator(false);
        verify(mView).setLoginBtnEnabled(true);
        verify(mView).showMsgLoginSucceed();
        verify(mView).launchMainPage();
    }

    @Test
    public void onEmailNotExisted() throws Exception {
        mLoginPresenter.onEmailNotExisted();

        verify(mView).showLoadingIndicator(false);
        verify(mView).setLoginBtnEnabled(true);
        verify(mView).errorEmailNotExisted();
    }

    @Test
    public void onPasswordIncorrect() throws Exception {
        mLoginPresenter.onPasswordIncorrect();

        verify(mView).showLoadingIndicator(false);
        verify(mView).setLoginBtnEnabled(true);
        verify(mView).errorIncorrectPassword();
    }

    @Test
    public void onLoginFailed() throws Exception {
        mLoginPresenter.onLoginFailed();

        verify(mView).showLoadingIndicator(false);
        verify(mView).setLoginBtnEnabled(true);
        verify(mView).errorLoginFailed();
    }
}
