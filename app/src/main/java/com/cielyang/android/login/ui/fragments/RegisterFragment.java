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
import android.widget.EditText;
import android.widget.TextView;

import com.cielyang.android.login.R;
import com.cielyang.android.login.base.BaseFragment;
import com.cielyang.android.login.common.utils.ToastUtils;
import com.cielyang.android.login.di.ActivityScoped;
import com.cielyang.android.login.ui.activities.MainActivity;
import com.cielyang.android.login.viewmodel.RegisterViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/** */
@ActivityScoped
public class RegisterFragment extends BaseFragment {

    @BindView(R.id.edit_text_username)
    TextInputEditText mEditTextUsername;

    @BindView(R.id.text_input_layout_username)
    TextInputLayout mTextInputLayoutUsername;

    @BindView(R.id.edit_text_email)
    TextInputEditText mEditTextEmail;

    @BindView(R.id.text_input_layout_email)
    TextInputLayout mTextInputLayoutEmail;

    @BindView(R.id.edit_text_password)
    TextInputEditText mEditTextPassword;

    @BindView(R.id.text_input_layout_password)
    TextInputLayout mTextInputLayoutPassword;

    @BindView(R.id.btn_register)
    AppCompatButton mBtnRegister;

    @BindView(R.id.link_login)
    TextView mLinkLogin;

    Unbinder unbinder;
    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private OnClickedListener mListener;
    private Context mActivity;
    private RegisterViewModel mRegisterViewModel;

    @Inject
    public RegisterFragment() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, view);
        initViewModel();

        return view;
    }

    private void initViewModel() {
        mRegisterViewModel =
                ViewModelProviders.of(this, mViewModelFactory).get(RegisterViewModel.class);
        mRegisterViewModel
                .getLaunchMainPageCommand()
                .observe(this, __ -> MainActivity.actionStart(mActivity));

        mRegisterViewModel
                .getTokenMessage()
                .observe(this, (resId, toastLevel) -> ToastUtils.msg(mActivity, resId, toastLevel));

        mRegisterViewModel
                .getRegisterState()
                .observe(
                        this,
                        isLogging -> {
                            if (isLogging == null) return;
                            mListener.showLoadingIndicator(isLogging);
                            boolean enabled = !isLogging;
                            mBtnRegister.setEnabled(enabled);
                        });

        mRegisterViewModel
                .getUsernameErrorResId()
                .observe(
                        this,
                        resId ->
                                mTextInputLayoutUsername.setError(
                                        resId == null ? null : getString(resId)));
        mRegisterViewModel
                .getEmailErrorResId()
                .observe(
                        this,
                        resId ->
                                mTextInputLayoutEmail.setError(
                                        resId == null ? null : getString(resId)));
        mRegisterViewModel
                .getPasswordErrorResId()
                .observe(
                        this,
                        resId ->
                                mTextInputLayoutPassword.setError(
                                        resId == null ? null : getString(resId)));
    }

    private void init() {
        mEditTextUsername.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence charSequence, int start, int count, int after) {
                        // do nothing
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence charSequence, int start, int before, int count) {
                        mTextInputLayoutUsername.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
        mEditTextUsername.setOnFocusChangeListener(
                (view, hasFocus) -> {
                    if (!hasFocus && view != null) {
                        CharSequence username = ((EditText) view).getText();
                        if (username.length() > 0) {
                            mRegisterViewModel.checkUsernameRegisteredOrNot(username);
                        }
                    }
                });

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
        mEditTextEmail.setOnFocusChangeListener(
                (view, hasFocus) -> {
                    if (!hasFocus && view != null) {
                        CharSequence email = ((EditText) view).getText();
                        if (email.length() > 0) {
                            mRegisterViewModel.checkEmailRegisteredOrNot(email);
                        }
                    }
                });

        mEditTextPassword.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence charSequence, int i, int i1, int i2) {
                        // do nothing
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        mTextInputLayoutPassword.setError(null);
                        mTextInputLayoutPassword.setPasswordVisibilityToggleEnabled(true);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() == 0) {
                            mTextInputLayoutPassword.setPasswordVisibilityToggleEnabled(false);
                        }
                    }
                });
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

    @OnClick(R.id.btn_register)
    public void onBtnRegisterClicked() {
        mRegisterViewModel.register(
                mEditTextUsername.getText(), mEditTextEmail.getText(), mEditTextPassword.getText());
    }

    @OnClick(R.id.link_login)
    public void onLinkLoginClicked() {
        mListener.onLoginLinkClicked();
    }

    public interface OnClickedListener {

        void onLoginLinkClicked();

        void showLoadingIndicator(boolean enabled);
    }
}
