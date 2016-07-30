package com.noveogroup.teamzolotov.iwashere.activities;

import com.noveogroup.teamzolotov.iwashere.model.Profile;

public interface Loginable {
    void onLoginLinkClicked();

    void onLoginSuccessfully(Profile profile);
}

