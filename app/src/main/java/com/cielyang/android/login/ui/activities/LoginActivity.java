package com.cielyang.android.login.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.cielyang.android.login.R;
import com.cielyang.android.login.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    static void actionStart(@NonNull Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
