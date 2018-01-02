package com.cielyang.android.login.ui.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cielyang.android.login.R;
import com.cielyang.android.login.base.BaseFragment;
import com.cielyang.android.login.common.utils.ToastUtils;
import com.cielyang.android.login.di.ActivityScoped;
import com.cielyang.android.login.ui.activities.MainActivity;
import com.cielyang.android.login.viewmodel.LoginViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/** */
@ActivityScoped
public class LoginFragment extends BaseFragment {

    @BindView(R.id.edit_text_email)
    TextInputEditText mEditTextEmail;

    @BindView(R.id.text_input_layout_email)
    TextInputLayout mTextInputLayoutEmail;

    @BindView(R.id.edit_text_pwd)
    TextInputEditText mEditTextPwd;

    @BindView(R.id.text_input_layout_pwd)
    TextInputLayout mTextInputLayoutPwd;

    @BindView(R.id.btn_login)
    AppCompatButton mBtnLogin;

    @BindView(R.id.link_register)
    TextView mLinkRegister;

    Unbinder unbinder;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private LoginViewModel mLoginViewModel;

    private OnClickedListener mListener;
    private Context mActivity;

    @Inject
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        initViewModel();

        return view;
    }

    private void init() {
        mEditTextEmail.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence charSequence, int start, int count, int after) {
                        // do nothing
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence charSequence, int start, int before, int count) {
                        mTextInputLayoutEmail.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });

        mEditTextPwd.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence charSequence, int i, int i1, int i2) {
                        // do nothing
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        mTextInputLayoutPwd.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
    }

    private void initViewModel() {
        mLoginViewModel = ViewModelProviders.of(this, mViewModelFactory).get(LoginViewModel.class);

        mLoginViewModel
                .getLaunchMainPageCommand()
                .observe(this, __ -> MainActivity.actionStart(mActivity));

        mLoginViewModel
                .getTokenMessage()
                .observe(this, (resId, toastLevel) -> ToastUtils.msg(mActivity, resId, toastLevel));
        mLoginViewModel
                .getLoginState()
                .observe(
                        this,
                        isLogging -> {
                            if (isLogging == null) return;
                            mListener.showLoadingIndicator(isLogging);
                            boolean enabled = !isLogging;
                            mBtnLogin.setEnabled(enabled);
                        });
        mLoginViewModel
                .getEmailErrorResId()
                .observe(
                        this,
                        resId ->
                                mTextInputLayoutEmail.setError(
                                        resId == null ? null : getString(resId)));
        mLoginViewModel
                .getPasswordErrorResId()
                .observe(
                        this,
                        resId ->
                                mTextInputLayoutPwd.setError(
                                        resId == null ? null : getString(resId)));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClickedListener) {
            mActivity = context;
            mListener = (OnClickedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_login)
    public void onBtnLoginClicked() {
        mLoginViewModel.loginByEmail(mEditTextEmail.getText(), mEditTextPwd.getText());
    }

    @OnClick(R.id.link_register)
    public void onRegisterLinkClicked() {
        mListener.onRegisterLinkClicked();
    }

    public interface OnClickedListener {

        void onRegisterLinkClicked();

        void showLoadingIndicator(boolean shown);
    }
}
