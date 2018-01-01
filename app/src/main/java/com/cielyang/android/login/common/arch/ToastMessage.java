package com.cielyang.android.login.common.arch;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.StringRes;

import com.cielyang.android.login.common.utils.ToastUtils;

/** */
public class ToastMessage extends SingleLiveEvent<ToastMessage.ToastInfo> {

    public void observe(LifecycleOwner owner, final ToastMessage.ToastObserver observer) {
        super.observe(
                owner,
                info -> {
                    if (info == null) {
                        return;
                    }
                    observer.onNewMessage(info.resId, info.level);
                });
    }

    public interface ToastObserver {
        /**
         * Called when there is a new message to be shown.
         *
         * @param resId The new message, non-null.
         */
        void onNewMessage(
                @StringRes int resId, @ToastUtils.ToastLevel int toastLevel);
    }

    public static class ToastInfo {
        public @StringRes
        int resId;
        public @ToastUtils.ToastLevel
        int level;
    }
}
