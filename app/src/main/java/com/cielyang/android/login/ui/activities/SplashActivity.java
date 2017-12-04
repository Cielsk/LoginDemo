package com.cielyang.android.login.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.cielyang.android.login.MainActivity;
import com.cielyang.android.login.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, MainActivity.class));
    }
}
