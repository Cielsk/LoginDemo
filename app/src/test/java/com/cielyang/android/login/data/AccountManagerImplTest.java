package com.cielyang.android.login.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.text.TextUtils;

import com.cielyang.android.login.common.async.AppExecutors;
import com.cielyang.android.login.common.async.SingleExecutors;
import com.cielyang.android.login.configs.leancloud.ErrorCode;
import com.cielyang.android.login.configs.leancloud.ErrorResponse;
import com.cielyang.android.login.data.entities.Account;
import com.cielyang.android.login.data.entities.LoginInfo;
import com.cielyang.android.login.data.entities.RegisterInfo;
import com.cielyang.android.login.data.local.SessionDao;
import com.cielyang.android.login.data.remote.AccountService;
import com.cielyang.android.login.data.remote.UserQueryResponse;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.Lazy;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/** */
@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
public class AccountManagerImplTest {

    private static final String USERNAME = "JohnDoe";
    private static final String EMAIL = "john@test.com";
    private static final String PASSWORD = "qwerty";
    private static final String TOKEN = "testToken";
    private Account mAccount;

    @Mock
    private SessionDao mSessionDao;

    @Mock
    private AccountService mAccountService;

    @Mock
    private AccountManager.CheckSessionTokenCallback mCheckSessionTokenCallback;
    @Mock
    private AccountManager.LoginByEmailCallback mLoginByEmailCallback;
    @Mock
    private AccountManager.LoginByTokenCallback mLoginByTokenCallback;
    @Mock
    private AccountManager.RegisterCallback mRegisterCallback;
    @Mock
    private AccountManager.QueryUsernameCallback mQueryUsernameCallback;
    @Mock
    private AccountManager.QueryEmailCallback mQueryEmailCallback;

    private AccountManagerImpl mAccountManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Retrofit retrofit = mock(Retrofit.class);
        when(retrofit.create(AccountService.class)).thenReturn(mAccountService);

        AppExecutors appExecutors = new SingleExecutors();
        mAccountManager = new AccountManagerImpl(appExecutors, mSessionDao, retrofit);

        // 模拟 Gson 对象的延迟依赖注入
        Lazy<Gson> gsonLazy = (Lazy<Gson>) mock(Lazy.class);
        when(gsonLazy.get()).thenReturn(new Gson());
        mAccountManager.mGsonInjector = gsonLazy;

        mAccount = new Account();
        mAccount.setUsername(USERNAME);
        mAccount.setEmail(EMAIL);
        mAccount.setSessionToken(TOKEN);
    }

    @Test
    public void checkSessionToken_tokenSaved() throws Exception {
        PowerMockito.mockStatic(TextUtils.class);
        when(TextUtils.isEmpty(any())).thenReturn(false);
        mAccountManager.checkSessionToken(mCheckSessionTokenCallback);

        verify(mSessionDao).getSessionToken();

        verify(mCheckSessionTokenCallback).onTokenSaved();
    }

    @Test
    public void checkSessionToken_tokenNotSaved() throws Exception {
        PowerMockito.mockStatic(TextUtils.class);
        when(TextUtils.isEmpty(any())).thenReturn(true);
        mAccountManager.checkSessionToken(mCheckSessionTokenCallback);

        verify(mSessionDao).getSessionToken();

        verify(mCheckSessionTokenCallback).onTokenNotSaved();
    }

    @Test
    public void loginByEmail_success() throws Exception {
        Response<Account> response = Response.success(mAccount);
        Call<Account> call = (Call<Account>) mock(Call.class);
        when(mAccountService.loginByEmail(any(LoginInfo.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);

        mAccountManager.loginByEmail(EMAIL, PASSWORD, mLoginByEmailCallback);

        assertTrue(response.isSuccessful());
        verify(mSessionDao).saveSession(any(Account.class));
        verify(mLoginByEmailCallback).onLoginSucceed();
    }

    @Test
    public void loginByEmail_fail() throws Exception {
        Call<Account> call = (Call<Account>) mock(Call.class);
        when(mAccountService.loginByEmail(any(LoginInfo.class))).thenReturn(call);
        when(call.execute()).thenThrow(new IOException());

        mAccountManager.loginByEmail(EMAIL, PASSWORD, mLoginByEmailCallback);

        verify(mLoginByEmailCallback).onLoginFailed();
    }

    @Test
    public void loginByEmail_errorNotRegistered() throws Exception {
        // 设置返回的错误信息
        ResponseBody responseBody = mock(ResponseBody.class);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ErrorCode.USER_NOT_REGISTERED);
        errorResponse.setError("User not registered.");
        when(responseBody.string()).thenReturn(new Gson().toJson(errorResponse));
        Response<Account> response = Response.error(400, responseBody);

        // 模拟 http 请求过程
        Call<Account> call = (Call<Account>) mock(Call.class);
        when(mAccountService.loginByEmail(any(LoginInfo.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);

        mAccountManager.loginByEmail(EMAIL, PASSWORD, mLoginByEmailCallback);

        assertFalse(response.isSuccessful());
        verify(mLoginByEmailCallback).onEmailNotExisted();
    }

    @Test
    public void loginByEmail_errorIncorrectPassword() throws Exception {
        // 设置返回的错误信息
        ResponseBody responseBody = mock(ResponseBody.class);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ErrorCode.INCORRECT_PASSWORD);
        errorResponse.setError("User not registered.");
        when(responseBody.string()).thenReturn(new Gson().toJson(errorResponse));
        Response<Account> response = Response.error(400, responseBody);

        // 模拟 http 请求过程
        Call<Account> call = (Call<Account>) mock(Call.class);
        when(mAccountService.loginByEmail(any(LoginInfo.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);

        mAccountManager.loginByEmail(EMAIL, PASSWORD, mLoginByEmailCallback);

        assertFalse(response.isSuccessful());
        verify(mLoginByEmailCallback).onPasswordIncorrect();
    }

    @Test
    public void loginByEmail_unknownError() throws Exception {
        // 设置返回的错误信息
        ResponseBody responseBody = mock(ResponseBody.class);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(10000);
        errorResponse.setError("Unknown Error.");
        when(responseBody.string()).thenReturn(new Gson().toJson(errorResponse));
        Response<Account> response = Response.error(400, responseBody);

        // 模拟 http 请求过程
        Call<Account> call = (Call<Account>) mock(Call.class);
        when(mAccountService.loginByEmail(any(LoginInfo.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);

        mAccountManager.loginByEmail(EMAIL, PASSWORD, mLoginByEmailCallback);

        assertFalse(response.isSuccessful());
        verify(mLoginByEmailCallback).onLoginFailed();
    }

    @Test
    public void loginByToken_success() throws Exception {
        Response<Account> response = Response.success(mAccount);
        Call<Account> call = (Call<Account>) mock(Call.class);
        when(mAccountService.loginByToken(anyString())).thenReturn(call);
        when(call.execute()).thenReturn(response);
        when(mSessionDao.getSessionToken()).thenReturn(TOKEN);

        mAccountManager.loginByToken(mLoginByTokenCallback);

        assertTrue(response.isSuccessful());
        verify(mSessionDao).saveSession(any(Account.class));
        verify(mLoginByTokenCallback).onLoginSucceed();
    }

    @Test
    public void loginByToken_fail() throws Exception {
        Call<Account> call = (Call<Account>) mock(Call.class);
        when(mAccountService.loginByToken(anyString())).thenReturn(call);
        when(call.execute()).thenThrow(new IOException());
        when(mSessionDao.getSessionToken()).thenReturn(TOKEN);

        mAccountManager.loginByToken(mLoginByTokenCallback);

        verify(mLoginByTokenCallback).onLoginFailed();
    }

    @Test
    public void loginByToken_errorNotRegistered() throws Exception {
        // 设置返回的错误信息
        ResponseBody responseBody = mock(ResponseBody.class);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ErrorCode.USER_NOT_REGISTERED);
        errorResponse.setError("User not registered.");
        when(responseBody.string()).thenReturn(new Gson().toJson(errorResponse));
        Response<Account> response = Response.error(400, responseBody);

        // 模拟 http 请求过程
        Call<Account> call = (Call<Account>) mock(Call.class);
        when(mAccountService.loginByToken(anyString())).thenReturn(call);
        when(call.execute()).thenReturn(response);
        when(mSessionDao.getSessionToken()).thenReturn(TOKEN);

        mAccountManager.loginByToken(mLoginByTokenCallback);

        assertFalse(response.isSuccessful());
        verify(mLoginByTokenCallback).onTokenExpired();
    }

    @Test
    public void loginByToken_unknownError() throws Exception {
        // 设置返回的错误信息
        ResponseBody responseBody = mock(ResponseBody.class);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(10000);
        errorResponse.setError("Unknown Error.");
        when(responseBody.string()).thenReturn(new Gson().toJson(errorResponse));
        Response<Account> response = Response.error(400, responseBody);

        // 模拟 http 请求过程
        Call<Account> call = (Call<Account>) mock(Call.class);
        when(mAccountService.loginByToken(anyString())).thenReturn(call);
        when(call.execute()).thenReturn(response);
        when(mSessionDao.getSessionToken()).thenReturn(TOKEN);

        mAccountManager.loginByToken(mLoginByTokenCallback);

        assertFalse(response.isSuccessful());
        verify(mLoginByTokenCallback).onLoginFailed();
    }

    @Test
    public void register_success() throws Exception {
        Response<Account> response = Response.success(mAccount);
        Call<Account> call = (Call<Account>) mock(Call.class);
        when(mAccountService.register(any(RegisterInfo.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);

        mAccountManager.register(USERNAME, EMAIL, PASSWORD, mRegisterCallback);

        assertTrue(response.isSuccessful());
        verify(mSessionDao).saveSession(any(Account.class));
        verify(mRegisterCallback).onRegisterSucceed();
    }

    @Test
    public void register_fail() throws Exception {
        Call<Account> call = (Call<Account>) mock(Call.class);
        when(mAccountService.register(any(RegisterInfo.class))).thenReturn(call);
        when(call.execute()).thenThrow(new IOException());

        mAccountManager.register(USERNAME, EMAIL, PASSWORD, mRegisterCallback);

        verify(mRegisterCallback).onRegisterFailed();
    }

    @Test
    public void register_errorUsernameRegistered() throws Exception {
        // 设置返回的错误信息
        ResponseBody responseBody = mock(ResponseBody.class);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ErrorCode.USERNAME_EXISTED);
        errorResponse.setError("Username registered.");
        when(responseBody.string()).thenReturn(new Gson().toJson(errorResponse));
        Response<Account> response = Response.error(400, responseBody);

        // 模拟 http 请求过程
        Call<Account> call = (Call<Account>) mock(Call.class);
        when(mAccountService.register(any(RegisterInfo.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);

        mAccountManager.register(USERNAME, EMAIL, PASSWORD, mRegisterCallback);

        assertFalse(response.isSuccessful());
        verify(mRegisterCallback).onUsernameRegistered();
    }

    @Test
    public void register_errorEmailRegistered() throws Exception {
        // 设置返回的错误信息
        ResponseBody responseBody = mock(ResponseBody.class);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ErrorCode.EMAIL_EXISTED);
        errorResponse.setError("Email registered.");
        when(responseBody.string()).thenReturn(new Gson().toJson(errorResponse));
        Response<Account> response = Response.error(400, responseBody);

        // 模拟 http 请求过程
        Call<Account> call = (Call<Account>) mock(Call.class);
        when(mAccountService.register(any(RegisterInfo.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);

        mAccountManager.register(USERNAME, EMAIL, PASSWORD, mRegisterCallback);

        assertFalse(response.isSuccessful());
        verify(mRegisterCallback).onEmailRegistered();
    }

    @Test
    public void register_unknownError() throws Exception {
        // 设置返回的错误信息
        ResponseBody responseBody = mock(ResponseBody.class);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(10000);
        errorResponse.setError("Unknown Error.");
        when(responseBody.string()).thenReturn(new Gson().toJson(errorResponse));
        Response<Account> response = Response.error(400, responseBody);

        // 模拟 http 请求过程
        Call<Account> call = (Call<Account>) mock(Call.class);
        when(mAccountService.register(any(RegisterInfo.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);

        mAccountManager.register(USERNAME, EMAIL, PASSWORD, mRegisterCallback);

        assertFalse(response.isSuccessful());
        verify(mRegisterCallback).onRegisterFailed();
    }

    @Test
    public void queryUserByName_registered() throws Exception {
        UserQueryResponse queryResponse = new UserQueryResponse();
        List<Account> list = new ArrayList<>(1);
        list.add(mAccount);
        queryResponse.setResults(list);
        Response<UserQueryResponse> response = Response.success(queryResponse);
        Call<UserQueryResponse> call = (Call<UserQueryResponse>) mock(Call.class);
        when(mAccountService.findUsers(anyString())).thenReturn(call);
        when(call.execute()).thenReturn(response);

        mAccountManager.queryUserByName(USERNAME, mQueryUsernameCallback);

        assertTrue(response.isSuccessful());

        verify(mQueryUsernameCallback).onUsernameRegistered();
    }

    @Test
    public void queryUserByName_notRegistered() throws Exception {
        UserQueryResponse queryResponse = new UserQueryResponse();
        queryResponse.setResults(Collections.emptyList());
        Response<UserQueryResponse> response = Response.success(queryResponse);
        Call<UserQueryResponse> call = (Call<UserQueryResponse>) mock(Call.class);
        when(mAccountService.findUsers(anyString())).thenReturn(call);
        when(call.execute()).thenReturn(response);

        mAccountManager.queryUserByName(USERNAME, mQueryUsernameCallback);

        assertTrue(response.isSuccessful());

        verify(mQueryUsernameCallback).onUsernameNotRegistered();
    }

    @Test
    public void queryUserByEmail_registered() throws Exception {
        UserQueryResponse queryResponse = new UserQueryResponse();
        List<Account> list = new ArrayList<>(1);
        list.add(mAccount);
        queryResponse.setResults(list);
        Response<UserQueryResponse> response = Response.success(queryResponse);
        Call<UserQueryResponse> call = (Call<UserQueryResponse>) mock(Call.class);
        when(mAccountService.findUsers(anyString())).thenReturn(call);
        when(call.execute()).thenReturn(response);

        mAccountManager.queryUserByEmail(USERNAME, mQueryEmailCallback);

        assertTrue(response.isSuccessful());

        verify(mQueryEmailCallback).onEmailRegistered();
    }


    @Test
    public void queryUserByEmail_notRegistered() throws Exception {
        UserQueryResponse queryResponse = new UserQueryResponse();
        queryResponse.setResults(Collections.emptyList());
        Response<UserQueryResponse> response = Response.success(queryResponse);
        Call<UserQueryResponse> call = (Call<UserQueryResponse>) mock(Call.class);
        when(mAccountService.findUsers(anyString())).thenReturn(call);
        when(call.execute()).thenReturn(response);

        mAccountManager.queryUserByEmail(USERNAME, mQueryEmailCallback);

        assertTrue(response.isSuccessful());

        verify(mQueryEmailCallback).onEmailNotRegistered();
    }
}
