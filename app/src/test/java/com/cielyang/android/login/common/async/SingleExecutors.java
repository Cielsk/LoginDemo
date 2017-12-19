package com.cielyang.android.login.common.async;

import java.util.concurrent.Executor;

/**
 *
 */
public class SingleExecutors extends AppExecutors {

    private static Executor instant = Runnable::run;

    public SingleExecutors() {
        super(instant, instant, instant);
    }
}