package com.noveogroup.teamzolotov.iwashere.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.noveogroup.teamzolotov.iwashere.util.LoginUtils;
import com.noveogroup.teamzolotov.iwashere.util.ValidatorUtils;
import com.noveogroup.teamzolotov.iwashere.validation.ValidationResult;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginFragment extends BaseFragment {
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
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof Registrable)) {
            logger.info("Error! Activity does not implement onRegisterLinkClicked interface");
            throw new ClassCastException("Error! Activity does not implement onRegisterLinkClicked interface");
        }
    }

    @Override
    protected void onPostViewCrated(@Nullable Bundle savedInstanceState) {

    }

    @OnClick(R.id.btn_login)
    protected void onButtonLoginClick() {
        ValidationResult validationResult = new ValidationResult();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        ValidatorUtils.validateEmail(email, validationResult);
        ValidatorUtils.validatePassword(password, validationResult);

        if (validationResult.isNoProblems()) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());

            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getResources().getString(R.string.auth_text));
            progressDialog.setCancelable(false);
            progressDialog.show();

            LoginUtils.login(email, password, getActivity(), new DoWithProfile() {
                @Override
                public void onSuccess(FirebaseUser firebaseUser, String password) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    onLoginSuccess(firebaseUser, password);
                }

                @Override
                public void onError(Exception e) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    onLoginFailed(e);
                }
            });
        } else {
            showErrorMessage(validationResult);
        }
    }

    @OnClick(R.id.link_signup)
    protected void onRegisterLinkClick() {
        Activity activity = getActivity();
        Registrable registrable = (Registrable) activity;
        registrable.onRegisterLinkClicked();
    }


    @Override
    protected int getLayout() {
        return R.layout.login_view;
    }

    private void onLoginFailed(Exception e) {
        showMessage(R.string.auth_troubles_title, e.getMessage());
    }

    private void onLoginSuccess(final FirebaseUser firebaseUser, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.getting_data_from_server_text));
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(USERS_DATABASE_TAG)
                .child(firebaseUser.getUid())
                .child(USERNAME_DATABASE_KEY)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String username = (String) dataSnapshot.getValue();

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        Profile profile = new Profile(firebaseUser.getEmail(), username, password, firebaseUser.getUid());

                        Activity activity = getActivity();
                        Registrable onLoginSuccessfully = (Registrable) activity;
                        onLoginSuccessfully.onLoginSuccessfully(profile, firebaseUser);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        onLoginFailed(databaseError.toException());
                    }
                });
    }
}
