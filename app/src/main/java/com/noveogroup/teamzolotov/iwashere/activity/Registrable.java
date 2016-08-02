package com.noveogroup.teamzolotov.iwashere.activity;

import com.google.firebase.auth.FirebaseUser;
import com.noveogroup.teamzolotov.iwashere.model.Profile;

public interface Registrable {
    void onRegisterLinkClicked();

    void onRegisteredSuccessfully(Profile profile, FirebaseUser firebaseUser);

    void onLoginLinkClicked();

    void onLoginSuccessfully(Profile profile, FirebaseUser firebaseUser);

    void onSignOutClicked();
}
