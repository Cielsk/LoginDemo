package com.cielyang.android.login.common.arch;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.StringRes;

/** */
public class SnackbarMessage extends SingleLiveEvent<Integer> {

    public void observe(LifecycleOwner owner, final SnackbarObserver observer) {
        super.observe(
                owner,
                t -> {
                    if (t == null) {
                        return;
                    }
                    observer.onNewMessage(t);
                });
    }

    public interface SnackbarObserver {
        /**
         * Called when there is a new message to be shown.
         *
         * @param snackbarMessageResourceId The new message, non-null.
         */
        void onNewMessage(@StringRes int snackbarMessageResourceId);
    }
}
