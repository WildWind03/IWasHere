package com.noveogroup.teamzolotov.iwashere.fragments;

import com.google.firebase.auth.FirebaseUser;


public interface DoWithProfile {
    void onSuccess(FirebaseUser firebaseUser, String password);

    void onError();
}
