package com.cielyang.android.login.register;

import static org.mockito.Mockito.verify;

import com.cielyang.android.login.data.AccountManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** */
public class RegisterPresenterTest {

    private static final String USERNAME = "john";
    private static final String EMAIL = "john@test.com";
    private static final String PASSWORD = "qwerty";
    @Mock
    private RegisterContract.View mView;
    @Mock
    private AccountManager mAccountManager;
    private RegisterPresenter mRegisterPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mRegisterPresenter = new RegisterPresenter(mAccountManager);
        mRegisterPresenter.bindView(mView);
    }

    @After
    public void tearDown() throws Exception {
        mRegisterPresenter.unbindView();
    }

    @Test
    public void checkUsernameRegisteredOrNot() throws Exception {
        mRegisterPresenter.checkUsername(USERNAME);
        mRegisterPresenter.checkUsernameRegisteredOrNot(USERNAME);

        verify(mAccountManager).queryUserByName(USERNAME, mRegisterPresenter);
    }

    @Test
    public void checkEmailRegisteredOrNot() throws Exception {
        mRegisterPresenter.checkEmail(EMAIL);
        mRegisterPresenter.checkEmailRegisteredOrNot(EMAIL);

        verify(mAccountManager).queryUserByEmail(EMAIL, mRegisterPresenter);
    }

    @Test
    public void checkUsername_valid() throws Exception {
        mRegisterPresenter.checkUsername(USERNAME);

        verify(mView).clearUsernameError();
    }

    @Test
    public void checkUsername_empty() throws Exception {
        mRegisterPresenter.checkUsername(null);

        verify(mView).clearUsernameError();
        verify(mView).errorEmptyUsername();
    }

    @Test
    public void checkUsername_invalid() throws Exception {
        mRegisterPresenter.checkUsername("!@#$%");

        verify(mView).clearUsernameError();
        verify(mView).errorInvalidUsername();
    }

    @Test
    public void checkEmail_valid() throws Exception {
        mRegisterPresenter.checkEmail(EMAIL);

        verify(mView).clearEmailError();
    }

    @Test
    public void checkEmail_empty() throws Exception {
        mRegisterPresenter.checkEmail(null);

        verify(mView).clearEmailError();
        verify(mView).errorEmptyEmail();
    }

    @Test
    public void checkEmail_invalid() throws Exception {
        mRegisterPresenter.checkEmail("test");

        verify(mView).clearEmailError();
        verify(mView).errorInvalidEmail();
    }

    @Test
    public void checkPassword_valid() throws Exception {
        mRegisterPresenter.checkPassword(PASSWORD);

        verify(mView).clearPasswordError();
    }

    @Test
    public void checkPassword_empty() throws Exception {
        mRegisterPresenter.checkPassword(null);

        verify(mView).clearPasswordError();
        verify(mView).errorEmptyPassword();
    }

    @Test
    public void checkPassword_short() throws Exception {
        mRegisterPresenter.checkPassword("abc");

        verify(mView).clearPasswordError();
        verify(mView).errorShortPassword();
    }

    @Test
    public void checkPassword_invalid() throws Exception {
        mRegisterPresenter.checkPassword("1234 5678");

        verify(mView).clearPasswordError();
        verify(mView).errorInvalidPassword();
    }

    @Test
    public void register() throws Exception {
        mRegisterPresenter.register(USERNAME, EMAIL, PASSWORD);

        verify(mView).showLoadingIndicator(true);
        verify(mView).setRegisterBtnEnabled(false);
        verify(mAccountManager).register(USERNAME, EMAIL, PASSWORD, mRegisterPresenter);
    }

    @Test
    public void onRegisterSucceed() throws Exception {
        mRegisterPresenter.onRegisterSucceed();

        verify(mView).showLoadingIndicator(false);
        verify(mView).setRegisterBtnEnabled(true);
        verify(mView).showMsgRegisterSucceed();
        verify(mView).launchMainPage();
    }

    @Test
    public void onUsernameRegistered() throws Exception {
        mRegisterPresenter.onUsernameRegistered();

        verify(mView).showLoadingIndicator(false);
        verify(mView).setRegisterBtnEnabled(true);
        verify(mView).errorUsernameRegistered();
    }

    @Test
    public void onEmailRegistered() throws Exception {
        mRegisterPresenter.onEmailRegistered();

        verify(mView).showLoadingIndicator(false);
        verify(mView).setRegisterBtnEnabled(true);
        verify(mView).errorEmailRegistered();
    }

    @Test
    public void onRegisterFailed() throws Exception {
        mRegisterPresenter.onRegisterFailed();

        verify(mView).showLoadingIndicator(false);
        verify(mView).setRegisterBtnEnabled(true);
        verify(mView).errorRegisterFailed();
    }

    @Test
    public void onUsernameNotRegistered() throws Exception {
        mRegisterPresenter.onUsernameNotRegistered();

        verify(mView).clearUsernameError();
    }

    @Test
    public void onEmailNotRegistered() throws Exception {
        mRegisterPresenter.onEmailNotRegistered();

        verify(mView).clearEmailError();
    }
}
