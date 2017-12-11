package com.cielyang.android.login.main;

import com.cielyang.android.login.data.AccountManager;
import com.cielyang.android.login.data.entities.Account;
import com.cielyang.android.login.di.ActivityScoped;

import javax.inject.Inject;

/**
 *
 */
@ActivityScoped
public class MainPresenter implements MainContract.Presenter {

    private final AccountManager mAccountManager;

    private MainContract.View mView;

    @Inject
    public MainPresenter(AccountManager accountManager) {
        mAccountManager = accountManager;
    }

    @Override
    public void bindView(MainContract.View view) {
        mView = view;
        Account account = mAccountManager.getAccount();
        mView.showUserName(account.getUsername());
        mView.showEmail(account.getEmail());
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
