package com.noveogroup.teamzolotov.iwashere.activities;

import com.noveogroup.teamzolotov.iwashere.model.Profile;

public interface Registrable {
    void onRegisterLinkClicked();

    void onRegisteredSuccessfully(Profile profile);
}
