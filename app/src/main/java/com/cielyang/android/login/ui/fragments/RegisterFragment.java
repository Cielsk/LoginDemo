package com.cielyang.android.login.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cielyang.android.login.R;
import com.cielyang.android.login.base.BaseFragment;
import com.cielyang.android.login.common.utils.ToastUtils;
import com.cielyang.android.login.common.utils.ValidateUtils;
import com.cielyang.android.login.data.AccountManager;
import com.cielyang.android.login.ui.activities.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link OnClickedListener} interface to handle interaction events. Use the {@link
 * RegisterFragment#newInstance} factory method to create an instance of this fragment.
 */
public class RegisterFragment extends BaseFragment implements AccountManager.RegisterCallback {

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
    AccountManager mAccountManager;
    private OnClickedListener mListener;
    private Context mActivity;

    private CharSequence mUsername;
    private CharSequence mEmail;
    private CharSequence mPwd;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();
        return view;
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
                        clearUsernameError();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        clearUsernameError();
                        if (TextUtils.isEmpty(editable)) {
                            errorEmptyUsername();
                        } else if (!ValidateUtils.isValidUsername(editable)) {
                            errorInvalidUsername();
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
                        clearEmailError();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        clearEmailError();
                        if (TextUtils.isEmpty(editable)) {
                            errorEmptyEmail();
                        } else if (!ValidateUtils.isValidEmail(editable)) {
                            errorInvalidEmail();
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
                        clearPasswordError();
                        mTextInputLayoutPassword.setPasswordVisibilityToggleEnabled(true);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        clearPasswordError();
                        mTextInputLayoutPassword.setPasswordVisibilityToggleEnabled(true);
                        if (TextUtils.isEmpty(editable)) {
                            mTextInputLayoutPassword.setPasswordVisibilityToggleEnabled(false);
                            errorEmptyPassword();
                        } else if (ValidateUtils.isShortPassword(editable)) {
                            errorShortPassword();
                        } else if (!ValidateUtils.isValidPassword(editable)) {
                            errorInvalidPassword();
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
        if (isValidInput()) mAccountManager.register(mUsername, mEmail, mPwd, this);
    }

    private boolean isValidInput() {
        mUsername = mEditTextUsername.getText();
        mEmail = mEditTextEmail.getText();
        mPwd = mEditTextPassword.getText();

        return ValidateUtils.isValidUsername(mUsername)
                && ValidateUtils.isValidEmail(mEmail)
                && !ValidateUtils.isShortPassword(mPwd)
                && ValidateUtils.isValidPassword(mPwd);
    }

    @OnClick(R.id.link_login)
    public void onLinkLoginClicked() {
        mListener.onLoginLinkClicked();
    }

    private void clearUsernameError() {
        mTextInputLayoutUsername.setError(null);
    }

    private void clearPasswordError() {
        mTextInputLayoutPassword.setError(null);
    }

    private void clearEmailError() {
        mTextInputLayoutEmail.setError(null);
    }

    private void errorEmptyUsername() {
        mTextInputLayoutUsername.setError(getString(R.string.error_field_required));
    }

    private void errorInvalidUsername() {
        mTextInputLayoutUsername.setError(getString(R.string.error_invalid_username));
    }

    private void errorEmptyEmail() {
        mTextInputLayoutEmail.setError(getString(R.string.error_field_required));
    }

    private void errorInvalidEmail() {
        mTextInputLayoutEmail.setError(getString(R.string.error_invalid_email));
    }

    private void errorEmptyPassword() {
        mTextInputLayoutPassword.setError(getString(R.string.error_field_required));
    }

    private void errorShortPassword() {
        mTextInputLayoutPassword.setError(getString(R.string.error_short_password));
    }

    private void errorInvalidPassword() {
        mTextInputLayoutPassword.setError(getString(R.string.error_invalid_password));
    }

    @Override
    public void onRegisterSucceed() {
        ToastUtils.success(mActivity, getString(R.string.msg_success_register));
        launchMainPage();
    }

    private void launchMainPage() {
        MainActivity.actionStart(mActivity);
    }

    @Override
    public void onUsernameExisted() {
        mTextInputLayoutUsername.setError(getString(R.string.error_username_existed));
    }

    @Override
    public void onEmailExisted() {
        mTextInputLayoutEmail.setError(getString(R.string.error_email_existed));
    }

    @Override
    public void onRegisterFailed() {
        ToastUtils.error(mActivity, getString(R.string.error_register_failed_unknown_cause));
    }

    public interface OnClickedListener {

        void onLoginLinkClicked();
    }
}
