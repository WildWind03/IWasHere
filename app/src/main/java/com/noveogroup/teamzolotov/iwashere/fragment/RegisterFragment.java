package com.noveogroup.teamzolotov.iwashere.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.noveogroup.teamzolotov.iwashere.activity.Registrable;
import com.noveogroup.teamzolotov.iwashere.model.Profile;
import com.noveogroup.teamzolotov.iwashere.util.ValidatorUtils;
import com.noveogroup.teamzolotov.iwashere.validation.ValidationResult;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterFragment extends BaseFragment {

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

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof Registrable)) {
            logger.info("Error! Activity must implement registrable interface");
            throw new ClassCastException("Error! Activity must implement registrable interface");
        }
    }

    @Override
    protected void onPostViewCrated(@Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.btn_signup)
    void onSignUpButton() {
        ValidationResult validationResult = new ValidationResult();

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        ValidatorUtils.validateEmail(email, validationResult);
        ValidatorUtils.validateUsername(name, validationResult);
        ValidatorUtils.validatePassword(password, validationResult);

        if (!validationResult.isNoProblems()) {
            showErrorMessage(validationResult);
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getResources().getString(R.string.sign_up_text));
            progressDialog.setCancelable(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (!task.isSuccessful()) {
                                onRegisterFailed();
                            } else {
                                onRegisterSuccess(task.getResult().getUser());
                            }
                        }
                    });
        }
    }

    private void onRegisterSuccess(FirebaseUser firebaseUser) {

        String name = nameText.getText().toString();
        String uid = firebaseUser.getUid();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        DatabaseReference databaseReference = myRef.child(USERS_DATABASE_TAG).child(uid);
        databaseReference.child(USERNAME_DATABASE_TAG).setValue(name);

        Profile profile = new Profile(email, name, password, firebaseUser.getUid());

        Activity activity = getActivity();

        Registrable registrable = (Registrable) activity;
        registrable.onRegisteredSuccessfully(profile, firebaseUser);
    }

    private void onRegisterFailed() {
        showMessage(R.string.registration_problems_title, R.string.registration_failed_message);
    }

    @OnClick(R.id.link_login)
    protected void onLoginLinkClick() {
        Activity activity = getActivity();

        Registrable registrable = (Registrable) activity;
        registrable.onLoginLinkClicked();
    }

    @Override
    protected int getLayout() {
        return R.layout.register_layout;
    }
}
