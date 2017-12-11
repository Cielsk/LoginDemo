package com.cielyang.android.login.login;

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
import com.cielyang.android.login.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/** */
@ActivityScoped
public class LoginFragment extends BaseFragment implements LoginContract.View {

    @Inject
    LoginContract.Presenter mPresenter;

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

    private OnClickedListener mListener;
    private Context mActivity;

    @Inject
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        mPresenter.unbindView();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.bindView(this);
        init();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);

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
                        clearEmailError();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        mPresenter.checkEmail(editable);
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
                        clearPasswordError();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        clearPasswordError();
                        mPresenter.checkPassword(editable);
                    }
                });
    }

    @Override
    public void errorInvalidEmail() {
        mTextInputLayoutEmail.setError(mErrorInvalidEmail);
    }

    @Override
    public void errorEmptyEmail() {
        mTextInputLayoutEmail.setError(mErrorFiledRequired);
    }

    @Override
    public void errorEmptyPassword() {
        mTextInputLayoutPwd.setError(mErrorFiledRequired);
    }

    @Override
    public void errorIncorrectPassword() {
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
        mPresenter.loginByEmail(mEditTextEmail.getText(), mEditTextPwd.getText());
    }

    public void errorEmailNotExisted() {
        clearEmailError();
        mTextInputLayoutEmail.setError(mErrorEmailNotRegistered);
    }

    @Override
    public void errorLoginFailed() {
        ToastUtils.error(mActivity, mErrorLoginFailedUnknownCause);
    }

    @OnClick(R.id.link_register)
    public void onRegisterLinkClicked() {
        mListener.onRegisterLinkClicked();
    }

    @Override
    public void launchMainPage() {
        MainActivity.actionStart(mActivity);
    }

    @Override
    public void setLoginBtnEnabled(boolean enabled) {
        mBtnLogin.setEnabled(enabled);
    }

    @Override
    public void setPasswordToggleEnabled(boolean enabled) {
        mTextInputLayoutPwd.setPasswordVisibilityToggleEnabled(enabled);
    }

    @Override
    public void showLoadingIndicator(boolean enabled) {
        mListener.showLoadingIndicator(enabled);
    }

    @Override
    public void showMsgLoginSucceed() {
        ToastUtils.success(mActivity, mMsgLoginSuccess);
    }

    @Override
    public void clearEmailError() {
        mTextInputLayoutEmail.setError(null);
    }

    @Override
    public void clearPasswordError() {
        mTextInputLayoutPwd.setError(null);
    }

    public interface OnClickedListener {

        void onRegisterLinkClicked();

        void showLoadingIndicator(boolean shown);
    }
}
