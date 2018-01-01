package com.cielyang.android.login.register;

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

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/** */
@ActivityScoped
public class RegisterFragment extends BaseFragment implements RegisterContract.View {

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

    @BindString(R.string.error_invalid_username)
    String mErrorInvalidUsername;

    @BindString(R.string.error_invalid_email)
    String mErrorInvalidEmail;

    @BindString(R.string.error_short_password)
    String mErrorShortPwd;

    @BindString(R.string.error_invalid_password)
    String mErrorInvalidPwd;

    @BindString(R.string.error_username_existed)
    String mErrorUsernameExisted;

    @BindString(R.string.error_email_existed)
    String mErrorEmailExisted;

    @BindString(R.string.error_register_failed_unknown_cause)
    String mErrorRegisterFailedUnknownCause;

    @BindString(R.string.error_field_required)
    String mErrorFieldRequired;

    @BindString(R.string.msg_success_register)
    String mMsgRegisterSuccess;

    Unbinder unbinder;

    @Inject
    RegisterContract.Presenter mPresenter;

    private OnClickedListener mListener;
    private Context mActivity;

    @Inject
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.bindView(this);
        init();
    }

    @Override
    public void onStop() {
        mPresenter.unbindView();
        super.onStop();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, view);

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
                        mPresenter.checkUsername(editable);
                    }
                });
        mEditTextUsername.setOnFocusChangeListener(
                (view, hasFocus) -> {
                    if (!hasFocus && view != null) {
                        CharSequence username = ((EditText) view).getText();
                        if (username.length() > 0) {
                            mPresenter.checkUsernameRegisteredOrNot(username);
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
                        mPresenter.checkEmail(editable);
                    }
                });
        mEditTextEmail.setOnFocusChangeListener(
                (view, hasFocus) -> {
                    if (!hasFocus && view != null) {
                        CharSequence email = ((EditText) view).getText();
                        if (email.length() > 0) {
                            mPresenter.checkEmailRegisteredOrNot(email);
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
                        mPresenter.checkPassword(editable);
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
        mPresenter.register(
                mEditTextUsername.getText(), mEditTextEmail.getText(), mEditTextPassword.getText());
    }

    @OnClick(R.id.link_login)
    public void onLinkLoginClicked() {
        mListener.onLoginLinkClicked();
    }

    @Override
    public void clearUsernameError() {
        mTextInputLayoutUsername.setError(null);
    }

    @Override
    public void clearPasswordError() {
        mTextInputLayoutPassword.setError(null);
    }

    @Override
    public void clearEmailError() {
        mTextInputLayoutEmail.setError(null);
    }

    @Override
    public void errorEmptyUsername() {
        mTextInputLayoutUsername.setError(mErrorFieldRequired);
    }

    @Override
    public void errorUsernameRegistered() {
        mTextInputLayoutUsername.setError(mErrorUsernameExisted);
    }

    @Override
    public void errorInvalidUsername() {
        mTextInputLayoutUsername.setError(mErrorInvalidUsername);
    }

    @Override
    public void errorEmptyEmail() {
        mTextInputLayoutEmail.setError(mErrorFieldRequired);
    }

    @Override
    public void errorEmailRegistered() {
        mTextInputLayoutEmail.setError(mErrorEmailExisted);
    }

    @Override
    public void errorInvalidEmail() {
        mTextInputLayoutEmail.setError(mErrorInvalidEmail);
    }

    @Override
    public void errorEmptyPassword() {
        mTextInputLayoutPassword.setError(mErrorFieldRequired);
    }

    @Override
    public void errorShortPassword() {
        mTextInputLayoutPassword.setError(mErrorShortPwd);
    }

    @Override
    public void errorRegisterFailed() {
        ToastUtils.error(mActivity, mErrorRegisterFailedUnknownCause);
    }

    @Override
    public void errorInvalidPassword() {
        mTextInputLayoutPassword.setError(mErrorInvalidPwd);
    }

    @Override
    public void launchMainPage() {
        MainActivity.actionStart(mActivity);
    }

    @Override
    public void setRegisterBtnEnabled(boolean enabled) {
        mBtnRegister.setEnabled(enabled);
    }

    @Override
    public void setPasswordToggleEnabled(boolean enabled) {
        mTextInputLayoutPassword.setPasswordVisibilityToggleEnabled(enabled);
    }

    @Override
    public void showLoadingIndicator(boolean enabled) {
        mListener.showLoadingIndicator(enabled);
    }

    @Override
    public void showMsgRegisterSucceed() {
        ToastUtils.success(mActivity, mMsgRegisterSuccess);
    }

    public interface OnClickedListener {

        void onLoginLinkClicked();

        void showLoadingIndicator(boolean enabled);
    }
}
