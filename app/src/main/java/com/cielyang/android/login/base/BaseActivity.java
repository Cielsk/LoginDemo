package com.cielyang.android.login.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import dagger.android.support.DaggerAppCompatActivity;

/** */
@SuppressLint("Registered")
public class BaseActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.v("%s created.", getClass().getSimpleName());
    }
}
