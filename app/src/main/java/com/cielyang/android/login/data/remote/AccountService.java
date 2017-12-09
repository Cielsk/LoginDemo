package com.cielyang.android.login.data.remote;

import com.cielyang.android.login.configs.Api;
import com.cielyang.android.login.configs.leancloud.RequestHeader;
import com.cielyang.android.login.data.entities.Account;
import com.cielyang.android.login.data.entities.LoginInfo;
import com.cielyang.android.login.data.entities.RegisterInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/** Retrofit 专用的网络调用接口，用来定义可用的 api 接口内容. */
public interface AccountService {

    @POST(Api.REGISTER)
    Call<Account> register(@Body RegisterInfo registerInfo);

    @POST(Api.LOGIN)
    Call<Account> loginByEmail(@Body LoginInfo loginInfo);

    @GET(Api.LOGIN_BY_TOKEN)
    Call<Account> loginByToken(@Header(RequestHeader.SESSION_TOKEN_PARAMETER_NAME) String token);

    @GET(Api.QUERY_USER)
    Call<UserQueryResponse> findUsers(@Query(Api.QUERY_PARAM_NAME) String info);
}
