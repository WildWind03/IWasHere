package com.noveogroup.teamzolotov.iwashere.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.fragments.DoWithProfile;

public class LoginUtil {

    private LoginUtil() {

    }

    public static void login(final String email, final String password, final Activity activity, final boolean isProgressDialog,
                             final DoWithProfile doWithProfile) {

        final ProgressDialog progressDialog = new ProgressDialog(activity);

        if (isProgressDialog) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(activity.getResources().getString(R.string.auth_text));
            progressDialog.show();
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (isProgressDialog) {
                            progressDialog.dismiss();
                        }

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
