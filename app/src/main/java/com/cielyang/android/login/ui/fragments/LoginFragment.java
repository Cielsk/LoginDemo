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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link OnClickedListener} interface to handle interaction events. Use the {@link
 * LoginFragment#newInstance} factory method to create an instance of this fragment.
 */
public class LoginFragment extends BaseFragment implements AccountManager.LoginByEmailCallback {

    private static final String ARG_EMAIL = "param1";

    @Inject
    AccountManager mAccountManager;

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

    @BindString(R.string.error_invalid_email)
    String mErrorInvalidEmail;

    @BindString(R.string.error_incorrect_password)
    String mErrorIncorrectPwd;

    @BindString(R.string.error_field_required)
    String mErrorFiledRequired;

    @BindString(R.string.error_email_not_registered)
    String mErrorEmailNotRegistered;

    @BindString(R.string.error_login_failed_unknown_cause)
    String mErrorLoginFailedUnknownCause;

    @BindString(R.string.msg_success_login)
    String mMsgLoginSuccess;

    Unbinder unbinder;

    private CharSequence mEmail;
    private CharSequence mPwd;

    private OnClickedListener mListener;
    private Context mActivity;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     *
     * @param email email address.
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance(CharSequence email) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putCharSequence(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEmail = getArguments().getCharSequence(ARG_EMAIL);
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();
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
                        clearErrorEmail();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        clearErrorEmail();
                        if (TextUtils.isEmpty(editable)) {
                            errorEmptyEmail();
                        } else if (!ValidateUtils.isValidEmail(editable)) {
                            errorInvalidEmail();
                        }
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
                        clearErrorPwd();
                        mTextInputLayoutPwd.setPasswordVisibilityToggleEnabled(true);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        clearErrorPwd();
                        mTextInputLayoutPwd.setPasswordVisibilityToggleEnabled(true);
                        if (TextUtils.isEmpty(editable)) {
                            mTextInputLayoutPwd.setPasswordVisibilityToggleEnabled(false);
                            errorEmptyPassword();
                        }
                    }
                });
    }

    private void errorInvalidEmail() {
        mTextInputLayoutEmail.setError(mErrorInvalidEmail);
    }

    private void errorEmptyEmail() {
        mTextInputLayoutEmail.setError(mErrorFiledRequired);
    }

    private void errorEmptyPassword() {
        mTextInputLayoutPwd.setError(mErrorFiledRequired);
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
        if (isValidInput()) {
            mListener.showLoadingIndicator(true);
            mBtnLogin.setEnabled(false);
            mAccountManager.loginByEmail(mEmail, mPwd, this);
        }
    }

    private boolean isValidInput() {
        mEmail = mEditTextEmail.getText();
        mPwd = mEditTextPwd.getText();

        return ValidateUtils.isValidEmail(mEmail)
                && !ValidateUtils.isShortPassword(mPwd)
                && ValidateUtils.isValidPassword(mPwd);
    }

    public void errorEmailNotExisted() {
        clearErrorEmail();
        mTextInputLayoutEmail.setError(mErrorEmailNotRegistered);
    }

    @OnClick(R.id.link_register)
    public void onLinkRegisterClicked() {
        mListener.onRegisterLinkClicked();
    }

    private void launchMainPage() {
        MainActivity.actionStart(mActivity);
    }

    private void clearErrorEmail() {
        mTextInputLayoutEmail.setError(null);
    }

    private void clearErrorPwd() {
        mTextInputLayoutPwd.setError(null);
    }

    @Override
    public void onLoginSucceed() {
        mListener.showLoadingIndicator(false);
        mBtnLogin.setEnabled(true);
        ToastUtils.success(mActivity, mMsgLoginSuccess);
        launchMainPage();
    }

    @Override
    public void onEmailNotExisted() {
        mListener.showLoadingIndicator(false);
        mBtnLogin.setEnabled(true);
        errorEmailNotExisted();
    }

    @Override
    public void onPasswordIncorrect() {
        mListener.showLoadingIndicator(false);
        mBtnLogin.setEnabled(true);
        mTextInputLayoutPwd.setError(mErrorIncorrectPwd);
    }

    @Override
    public void onLoginFailed() {
        mListener.showLoadingIndicator(false);
        mBtnLogin.setEnabled(true);
        ToastUtils.error(mActivity, mErrorLoginFailedUnknownCause);
    }

    public interface OnClickedListener {

        void onRegisterLinkClicked();

        void showLoadingIndicator(boolean shown);
    }
}
