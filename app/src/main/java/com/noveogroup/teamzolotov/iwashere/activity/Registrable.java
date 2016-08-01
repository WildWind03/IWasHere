package com.noveogroup.teamzolotov.iwashere.activity;

import com.noveogroup.teamzolotov.iwashere.model.Profile;

public interface Registrable {
    void onRegisterLinkClicked();

    void onRegisteredSuccessfully(Profile profile);

    void onLoginLinkClicked();

    void onLoginSuccessfully(Profile profile);

    void onSignOutClicked();
}
