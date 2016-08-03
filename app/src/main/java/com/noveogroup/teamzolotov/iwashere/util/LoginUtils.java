package com.noveogroup.teamzolotov.iwashere.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.fragment.DoWithProfile;

public final class LoginUtils {

    private LoginUtils() {
        throw new UnsupportedOperationException("Trying to create instance of utility class");
    }

    public static void login(final String email, final String password, final Activity activity,
                             final DoWithProfile doWithProfile) {

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Exception exception = task.getException();
                            doWithProfile.onError(exception);
                        } else {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            doWithProfile.onSuccess(firebaseUser, password);
                        }
                    }
                });
    }
}
