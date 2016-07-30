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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.activities.Loginable;
import com.noveogroup.teamzolotov.iwashere.activities.Registrable;
import com.noveogroup.teamzolotov.iwashere.model.Profile;
import com.noveogroup.teamzolotov.iwashere.util.EmailValidator;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterFragment extends BaseFragment {

    private static final int MIN_LENGTH_OF_PASSWORD = 6;
    private static final int MIN_LENGTH_OF_USERNAME = 2;
    private static final Logger logger = Logger.getLogger(RegisterFragment.class.getName());
    public static final String USERS_DATABASE_TAG = "users";
    public static final String USERNAME_DATABASE_TAG = "username";

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
    private FirebaseUser firebaseUser;

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
                showMessage(getContext().getString(R.string.short_password_message));
                break;

            case SHORT_PASSWORD_INVALID_EMAIL:
                showMessage(getContext().getString(R.string.short_password_invalid_email));
                break;

            case SHORT_PASSWORD_INVALID_EMAIL_INVALID_USERNAME:
                showMessage(getContext().getString(R.string.invalid_username_email_short_password));
                break;

            case INVALID_EMAIL:
                showMessage(getContext().getString(R.string.invalid_email_message));
                break;

            case INVALID_EMAIL_INVALID_USERNAME:
                showMessage(getContext().getString(R.string.invalid_email_invalid_username));
                break;

            case INVALID_USERNAME:
                showMessage(getContext().getString(R.string.invalid_username));
                break;

            case SHORT_PASSWORD_INVALID_USERNAME:
                showMessage(getContext().getString(R.string.short_password_invalid_username));
                break;

            case SUCCESS:

                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

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
                                    firebaseUser = task.getResult().getUser();
                                    onRegisterSuccess();
                                }
                            }
                        });
                break;
        }
    }

    private void onRegisterSuccess() {

        String name = nameText.getText().toString();
        String uid = firebaseUser.getUid();
        String email = emailText.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        DatabaseReference databaseReference = myRef.child(USERS_DATABASE_TAG).child(uid);
        databaseReference.child(USERNAME_DATABASE_TAG).setValue(name);

        Profile profile = new Profile(email, name);

        Activity activity = getActivity();

        if (activity instanceof Registrable) {
            Registrable registrable = (Registrable) activity;
            registrable.onRegisteredSuccessfully(profile);
        } else {
            logger.info("Error! Activity must implement registrable interface");
        }
    }

    private void onRegisterFailed() {
        showMessage(getContext().getString(R.string.REGISTARTION_FAILED_MESSAGE));
    }

    @OnClick(R.id.link_login)
    protected void onLoginLinkClick() {
        Activity activity = getActivity();

        if (activity instanceof Loginable) {
            Loginable loginable = (Loginable) activity;
            loginable.onLoginLinkClicked();
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
