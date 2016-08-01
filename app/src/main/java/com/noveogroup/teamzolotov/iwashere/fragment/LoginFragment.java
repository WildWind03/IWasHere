package com.noveogroup.teamzolotov.iwashere.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.activity.Registrable;
import com.noveogroup.teamzolotov.iwashere.model.Profile;
import com.noveogroup.teamzolotov.iwashere.util.EmailValidator;
import com.noveogroup.teamzolotov.iwashere.util.LoginUtil;

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


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    protected void onPostViewCrated(@Nullable Bundle savedInstanceState) {

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
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                LoginUtil.login(email, password, getActivity(), true, new DoWithProfile() {
                    @Override
                    public void onSuccess(FirebaseUser firebaseUser, String password) {
                        onLoginSuccess(firebaseUser, password);
                    }

                    @Override
                    public void onError(Exception e) {
                        onLoginFailed(e);
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

    private void onLoginFailed(Exception e) {
        showMessage(R.string.auth_troubles_title, e.getMessage());
    }

    private void onLoginSuccess(final FirebaseUser firebaseUser, final String password) {
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

                Profile profile = new Profile(firebaseUser.getEmail(), username, password, firebaseUser.getUid());

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
