package com.cielyang.android.login.viewmodel;

import static com.cielyang.android.login.common.utils.PreconditionUtils.checkNotNull;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.cielyang.android.login.data.AccountManager;
import com.cielyang.android.login.data.entities.Account;

import javax.inject.Inject;

/** */
public class MainViewModel extends ViewModel {

    public final ObservableField<String> username = new ObservableField<>();

    public final ObservableField<String> email = new ObservableField<>();

    private final LiveData<Account> mAccountLiveData;

    @Inject
    public MainViewModel(AccountManager accountManager) {
        mAccountLiveData = accountManager.getAccount();
    }

    public void start() {
        Account account = mAccountLiveData.getValue();
        checkNotNull(account);
        username.set(account.getUsername());
        email.set(account.getEmail());
    }
}
