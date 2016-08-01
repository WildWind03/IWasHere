package com.noveogroup.teamzolotov.iwashere.fragment;

import com.google.firebase.auth.FirebaseUser;


public interface DoWithProfile {
    void onSuccess(FirebaseUser firebaseUser, String password);

    void onError(Exception e);
}
