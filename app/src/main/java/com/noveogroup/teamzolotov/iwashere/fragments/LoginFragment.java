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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.activities.Registrable;
import com.noveogroup.teamzolotov.iwashere.model.Profile;
import com.noveogroup.teamzolotov.iwashere.util.EmailValidator;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginFragment extends BaseFragment {

    private static final int MIN_LENGTH_OF_PASSWORD = 6;
    private final static Logger logger = Logger.getLogger(LoginFragment.class.getName());
    public static final String USERNAME_DATABASE_KEY = "username";
    public static final String USERS_DATABASE_TAG = "users";

    @BindView(R.id.input_email)
    protected EditText emailText;

    @BindView(R.id.input_password)
    protected EditText passwordText;

    @BindView(R.id.btn_login)
    protected Button loginButton;

    @BindView(R.id.link_signup)
    protected TextView registerLink;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

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
                showMessage(R.string.invalid_data, R.string.invalid_email_message);
                return;

            case SHORT_PASSWORD:
                showMessage(R.string.invalid_data, R.string.short_password_message);
                return;

            case SHORT_PASSWORD_INVALID_EMAIL:
                showMessage(R.string.invalid_data, R.string.short_password_invalid_email);
                return;

            case SUCCESS:
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getResources().getString(R.string.auth_text));
                progressDialog.show();

                final String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();

                                if (!task.isSuccessful()) {
                                    onLoginFailed();
                                } else {
                                    firebaseUser = task.getResult().getUser();
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
            registrable.onRegisterLinkClicked();
        } else {
            logger.info("Error! Activity does not implement onRegisterLinkClicked interface");
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
        showMessage(R.string.auth_troubles_title, R.string.auth_failed_message);
    }

    private void onLoginSuccess() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.getting_data_from_server_text));

        progressDialog.show();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(USERS_DATABASE_TAG).child(firebaseUser.getUid()).child(USERNAME_DATABASE_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = (String) dataSnapshot.getValue();

                progressDialog.dismiss();

                Profile profile = new Profile(firebaseUser.getEmail(), username);

                Activity activity = getActivity();
                if (activity instanceof Registrable) {
                    Registrable onLoginSuccessfully = (Registrable) activity;
                    onLoginSuccessfully.onLoginSuccessfully(profile);
                } else {
                    logger.info("Error! Activity does not implement onLoginSuccessfully interface");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private enum LoginValidateResult {
        SHORT_PASSWORD, INVALID_EMAIL, SUCCESS, SHORT_PASSWORD_INVALID_EMAIL
    }

}
