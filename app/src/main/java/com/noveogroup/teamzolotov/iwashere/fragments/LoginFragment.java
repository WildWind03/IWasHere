package com.noveogroup.teamzolotov.iwashere.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.activities.OnLoginSuccessfully;
import com.noveogroup.teamzolotov.iwashere.activities.Registrable;
import com.noveogroup.teamzolotov.iwashere.util.EmailValidator;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginFragment extends BaseFragment {

    private static final int MIN_LENGTH_OF_PASSWORD = 6;
    private final static Logger logger = Logger.getLogger(LoginFragment.class.getName());

    @BindView(R.id.input_email)
    protected EditText emailText;

    @BindView(R.id.input_password)
    protected EditText passwordText;

    @BindView(R.id.btn_login)
    protected Button loginButton;

    @BindView(R.id.link_signup)
    protected TextView registerLink;

    private FirebaseAuth mAuth;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    protected void onPostViewCrated(@Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.btn_login)
    protected void onButtonLoginClick() {

        LoginValidateResult loginValidateResult = validateLogin();

        switch (loginValidateResult) {
            case INVALID_EMAIL:
                showMessage("Invalid email");
                return;

            case SHORT_PASSWORD:
                showMessage("Short password. It must has 6 characters at least!");
                return;

            case SHORT_PASSWORD_INVALID_EMAIL:
                showMessage("Invalid email and short password. A password must has 6 characters at least!");
                return;

            case SUCCESS:
                loginButton.setEnabled(false);

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getResources().getString(R.string.auth_text));
                progressDialog.show();

                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                loginButton.setEnabled(true);

                                if (!task.isSuccessful()) {
                                    onLoginFailed();
                                } else {
                                    onLoginSuccess();
                                }
                            }
                        });
                break;
        }
    }

    @OnClick(R.id.link_signup)
    protected void onRegisterLinkClick() {
        Activity activity = getActivity();
        if (activity instanceof Registrable) {
            Registrable registrable = (Registrable) activity;
            registrable.register();
        } else {
            logger.info ("Error! Activity does not implement register interface");
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.login_view;
    }

    private LoginValidateResult validateLogin() {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (EmailValidator.validate(email)) {
            if (password.length() >= MIN_LENGTH_OF_PASSWORD) {
                return LoginValidateResult.SUCCESS;
            } else {
                return LoginValidateResult.SHORT_PASSWORD;
            }
        } else {
            if (password.length() >= MIN_LENGTH_OF_PASSWORD) {
                return LoginValidateResult.INVALID_EMAIL;
            } else {
                return LoginValidateResult.SHORT_PASSWORD_INVALID_EMAIL;
            }
        }
    }

    private void onLoginFailed() {
        showMessage("The problem with authentication");
    }

    private void onLoginSuccess() {
        showMessage("The authentication was successful!");

        Activity activity = getActivity();
        if (activity instanceof OnLoginSuccessfully) {
            OnLoginSuccessfully onLoginSuccessfully = (OnLoginSuccessfully) activity;
            onLoginSuccessfully.onLoginSuccessfully();
        } else {
            logger.info ("Error! Activity does not implement onLoginSuccessfully interface");
        }
    }

    private enum LoginValidateResult {
        SHORT_PASSWORD, INVALID_EMAIL, SUCCESS, SHORT_PASSWORD_INVALID_EMAIL
    }

}
