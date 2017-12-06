package com.cielyang.android.login.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import dagger.android.support.DaggerFragment;

/** */
public class BaseFragment extends DaggerFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.v("%s created.", getClass().getSimpleName());
    }
}
