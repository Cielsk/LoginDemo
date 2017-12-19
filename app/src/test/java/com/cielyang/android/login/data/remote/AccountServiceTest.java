package com.cielyang.android.login.data.remote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.cielyang.android.login.configs.Api;
import com.cielyang.android.login.configs.leancloud.ErrorCode;
import com.cielyang.android.login.configs.leancloud.ErrorResponse;
import com.cielyang.android.login.data.entities.Account;
import com.cielyang.android.login.data.entities.LoginInfo;
import com.cielyang.android.login.data.entities.RegisterInfo;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** */
public class AccountServiceTest {

    private final static String USERNAME = "johnsmith";
    private final static String EMAIL = "johnsmith@test.com";
    private final static String PASSWORD = "qwerty";
    private final static String TOKEN = "g3klhu5kvi5fvltr3onlo4ewr";
    private AccountService mService;

    @Before
    public void setUp() throws Exception {
        Gson gson = new Gson();
        Interceptor interceptor = new CommonInterceptor();
        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(interceptor).build();
        Retrofit retrofit =
                new Retrofit.Builder()
                        .client(client)
                        .baseUrl(Api.getDomain())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
        mService = retrofit.create(AccountService.class);
    }

    @After
    public void tearDown() throws Exception {
        mService = null;
    }

    private String randomString(int len) {
        return new Random()
                .ints(48, 122)
                .mapToObj(i -> (char) i)
                .filter(Character::isAlphabetic)
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    @Test
    public void register_withRandomUsername_success() throws IOException {
        String username = randomString(5);
        String email = username + "@test.com";
        String password = "qwerty";
        RegisterInfo registerInfo = new RegisterInfo(username, email, password);
        Call<Account> call = mService.register(registerInfo);
        Response<Account> response = call.execute();
        Account account = response.body();

        assertTrue(response.isSuccessful());
        assertEquals(201, response.code());
        assertNotNull(account);
    }

    @Test
    public void register_withExistedUsername() throws IOException {
        RegisterInfo registerInfo = new RegisterInfo(USERNAME, EMAIL + ".cn", PASSWORD);
        Call<Account> call = mService.register(registerInfo);
        Response<Account> response = call.execute();
        Account account = response.body();

        assertFalse(response.isSuccessful());
        assertNull(account);

        ResponseBody responseBody = response.errorBody();
        ErrorResponse errorResponse =
                new Gson().fromJson(responseBody.string(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals(ErrorCode.USERNAME_EXISTED, errorResponse.getCode());
    }

    @Test
    public void register_withExistedEmail() throws IOException {
        RegisterInfo registerInfo = new RegisterInfo(USERNAME + "test", EMAIL, PASSWORD);
        Call<Account> call = mService.register(registerInfo);
        Response<Account> response = call.execute();
        Account account = response.body();

        assertFalse(response.isSuccessful());
        assertNull(account);

        ResponseBody responseBody = response.errorBody();
        ErrorResponse errorResponse =
                new Gson().fromJson(responseBody.string(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals(ErrorCode.EMAIL_EXISTED, errorResponse.getCode());
    }

    @Test
    public void query_withExistedUsername() throws IOException {
        String queryString = String.format("{\"username\":\"%s\"}", USERNAME);
        Call<UserQueryResponse> call = mService.findUsers(queryString);
        Response<UserQueryResponse> response = call.execute();
        UserQueryResponse queryResponse = response.body();

        assertTrue(response.isSuccessful());
        assertNotNull(queryResponse);
        assertNotNull(queryResponse.getResults());
        assertEquals(1, queryResponse.getResults().size());
    }

    @Test
    public void query_withExistedEmail() throws IOException {
        String queryString = String.format("{\"email\":\"%s\"}", EMAIL);
        Call<UserQueryResponse> call = mService.findUsers(queryString);
        Response<UserQueryResponse> response = call.execute();
        UserQueryResponse queryResponse = response.body();

        assertTrue(response.isSuccessful());
        assertNotNull(queryResponse);
        assertNotNull(queryResponse.getResults());
        assertEquals(1, queryResponse.getResults().size());
    }

    @Test
    public void loginByEmail_success() throws IOException {
        LoginInfo loginInfo = new LoginInfo(EMAIL, PASSWORD);
        Call<Account> call = mService.loginByEmail(loginInfo);
        Response<Account> response = call.execute();
        Account account = response.body();

        assertTrue(response.isSuccessful());
        assertEquals(200, response.code());
        assertNotNull(account);
        assertEquals(USERNAME, account.getUsername());
        assertEquals(TOKEN, account.getSessionToken());
    }

    @Test
    public void loginByEmail_withIncorrectEmail() throws IOException {
        LoginInfo loginInfo = new LoginInfo(EMAIL + ".cn", PASSWORD);
        Call<Account> call = mService.loginByEmail(loginInfo);
        Response<Account> response = call.execute();
        Account account = response.body();

        assertFalse(response.isSuccessful());
        assertNull(account);

        ResponseBody responseBody = response.errorBody();
        ErrorResponse errorResponse =
                new Gson().fromJson(responseBody.string(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals(ErrorCode.USER_NOT_REGISTERED, errorResponse.getCode());
    }

    @Test
    public void loginByEmail_withIncorrectPassword() throws IOException {
        LoginInfo loginInfo = new LoginInfo(EMAIL, PASSWORD + "test");
        Call<Account> call = mService.loginByEmail(loginInfo);
        Response<Account> response = call.execute();
        Account account = response.body();

        assertFalse(response.isSuccessful());
        assertNull(account);

        ResponseBody responseBody = response.errorBody();
        ErrorResponse errorResponse =
                new Gson().fromJson(responseBody.string(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals(ErrorCode.INCORRECT_PASSWORD, errorResponse.getCode());
    }

    @Test
    public void loginByToken_success() throws IOException {
        Call<Account> call = mService.loginByToken(TOKEN);
        Response<Account> response = call.execute();
        Account account = response.body();

        assertTrue(response.isSuccessful());
        assertEquals(200, response.code());
        assertNotNull(account);
        assertEquals(USERNAME, account.getUsername());
        assertEquals(EMAIL, account.getEmail());
    }
}
