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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.activities.Loginable;
import com.noveogroup.teamzolotov.iwashere.activities.Registrable;
import com.noveogroup.teamzolotov.iwashere.util.EmailValidator;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterFragment extends BaseFragment {

    private static final int MIN_LENGTH_OF_PASSWORD = 6;
    private static final int MIN_LENGTH_OF_USERNAME = 2;
    private static final Logger logger = Logger.getLogger(RegisterFragment.class.getName());

    @BindView(R.id.input_name)
    protected EditText nameText;

    @BindView(R.id.input_email)
    protected EditText emailText;

    @BindView(R.id.input_password)
    protected EditText passwordText;

    @BindView(R.id.btn_signup)
    protected Button signupButton;

    @BindView(R.id.link_login)
    protected TextView loginLink;

    private FirebaseAuth mAuth;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    protected void onPostViewCrated(@Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.btn_signup)
    void onSignUpButton() {
        RegisterValidateResult registerValidateResult = validateLogin();

        switch (registerValidateResult) {
            case SHORT_PASSWORD:
                showMessage("Short password. It must has 6 characters at least");
                break;

            case SHORT_PASSWORD_INVALID_EMAIL:
                showMessage("Invalid email");
                break;

            case SHORT_PASSWORD_INVALID_EMAIL_INVALID_USERNAME:
                showMessage("Invalid email, too short username (it must has 2 characters at least" +
                        "too short password(min length is 6 characters");
                break;

            case INVALID_EMAIL:
                showMessage("Invalid email");
                break;

            case INVALID_EMAIL_INVALID_USERNAME:
                showMessage("Invalid email and too short username (min length is 2 characters");
                break;

            case INVALID_USERNAME:
                showMessage("Invalid username. Min length is 2 characters");
                break;

            case SHORT_PASSWORD_INVALID_USERNAME:
                showMessage("Too short password (min length is 6 characters) and too short username(min length is 2 characters)");
                break;

            case SUCCESS:

                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                signupButton.setEnabled(false);

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getResources().getString(R.string.sign_up_text));
                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();

                                if (!task.isSuccessful()) {
                                    onRegisterFailed();
                                } else {
                                    onRegisterSuccess();
                                }
                            }
                        });
                break;
        }
    }

    private void onRegisterSuccess() {

        final String name = nameText.getText().toString();
        final String email = emailText.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(email);
        myRef.setValue(name);

        Activity activity = getActivity();

        if (activity instanceof Registrable) {
            Registrable registrable = (Registrable) activity;
            registrable.register();
        } else {
            logger.info("Error! Activity must implement registrable interface");
        }
    }

    private void onRegisterFailed() {
        showMessage("The problem with registration! The account hasn't been created!");
    }

    @OnClick(R.id.link_login)
    protected void onLoginLinkClick() {
        Activity activity = getActivity();

        if (activity instanceof Loginable) {
            Loginable loginable = (Loginable) activity;
            loginable.login();
        } else {
            logger.info("Error! Activity must implement loginable interface");
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.register_layout;
    }

    private RegisterValidateResult validateLogin() {
        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (EmailValidator.validate(email)) {
            if (password.length() >= MIN_LENGTH_OF_PASSWORD) {
                if (name.length() >= MIN_LENGTH_OF_USERNAME) {
                    return RegisterValidateResult.SUCCESS;
                } else {
                    return RegisterValidateResult.INVALID_USERNAME;
                }
            } else {
                if (name.length() >= MIN_LENGTH_OF_USERNAME) {
                    return RegisterValidateResult.SHORT_PASSWORD;
                } else {
                    return RegisterValidateResult.SHORT_PASSWORD_INVALID_USERNAME;
                }
            }
        } else {
            if (password.length() >= MIN_LENGTH_OF_PASSWORD) {
                if (name.length() >= MIN_LENGTH_OF_USERNAME) {
                    return RegisterValidateResult.INVALID_EMAIL;
                } else {
                    return RegisterValidateResult.INVALID_EMAIL_INVALID_USERNAME;
                }
            } else {
                if (name.length() >= MIN_LENGTH_OF_USERNAME) {
                    return RegisterValidateResult.SHORT_PASSWORD_INVALID_EMAIL;
                } else {
                    return RegisterValidateResult.SHORT_PASSWORD_INVALID_EMAIL_INVALID_USERNAME;
                }
            }
        }
    }

    private enum RegisterValidateResult {
        SHORT_PASSWORD, INVALID_EMAIL, SUCCESS, SHORT_PASSWORD_INVALID_EMAIL, INVALID_USERNAME,
        SHORT_PASSWORD_INVALID_USERNAME, INVALID_EMAIL_INVALID_USERNAME, SHORT_PASSWORD_INVALID_EMAIL_INVALID_USERNAME
    }
}
