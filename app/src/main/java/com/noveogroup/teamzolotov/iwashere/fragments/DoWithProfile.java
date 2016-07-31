package com.noveogroup.teamzolotov.iwashere.fragments;

import com.google.firebase.auth.FirebaseUser;
import com.noveogroup.teamzolotov.iwashere.model.Profile;


public interface DoWithProfile {
    void onSuccess(FirebaseUser firebaseUser, String password);
    void onError();
}
