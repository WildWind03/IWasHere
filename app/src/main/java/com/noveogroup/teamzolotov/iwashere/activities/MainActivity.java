package com.noveogroup.teamzolotov.iwashere.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.fragments.AccountFragment;
import com.noveogroup.teamzolotov.iwashere.fragments.LoginFragment;
import com.noveogroup.teamzolotov.iwashere.fragments.RegisterFragment;
import com.noveogroup.teamzolotov.iwashere.model.Profile;

import java.util.logging.Logger;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements Registrable {
    private final static int LOGIN_ID = 0;
    private final static int MAP_ID = 1;
    private final static int LIST_REGIONS_ID = 2;
    private final static int SETTINGS_ID = 3;
    private final static int HELP_ID = 4;

    private final static String IS_AUTH_KEY = "IS AUTH_KEY";
    private final static String EMAIL_KEY = "EMAIL_KEY";
    private final static String USERNAME_KEY = "USERNAME_KEY";
    private final static String PASSWORD_KEY = "PASSWORD_KEY";

    private final static Logger logger = Logger.getLogger(MainActivity.class.getName());

    private LoginState loginState = LoginState.LOGIN;

    private AccountHeader accountHeader;
    private Profile profile;
    private Drawer drawer;

    private PrimaryDrawerItem loginDrawerItem;

    private SharedPreferences sharedPreferences;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    protected void onPostCreate(@Nullable final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        sharedPreferences = getPreferences(MODE_PRIVATE);

        boolean isAuth = sharedPreferences.getBoolean(IS_AUTH_KEY, false);

        if (isAuth) {

        }

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.map_string);

        IProfile iProfile = new ProfileDrawerItem()
                .withEmail(getString(R.string.default_email))
                .withName(getString(R.string.default_username));

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withTextColorRes(R.color.primary_text)
                .addProfiles(iProfile)
                .withSelectionListEnabled(false)
                .build();

        loginDrawerItem = new PrimaryDrawerItem();

        loginDrawerItem
                .withIdentifier(LOGIN_ID)
                .withName(R.string.login_string)
                .withIcon(R.drawable.ic_person_black_24dp);

        PrimaryDrawerItem mapDrawerItem = new PrimaryDrawerItem();
        mapDrawerItem
                .withIdentifier(MAP_ID)
                .withName(R.string.map_string)
                .withIcon(R.drawable.ic_map_black_24dp);

        PrimaryDrawerItem listRegionsDrawerItem = new PrimaryDrawerItem();
        listRegionsDrawerItem
                .withIdentifier(LIST_REGIONS_ID)
                .withName(R.string.regions_string)
                .withIcon(R.drawable.ic_list_black_24dp);

        SecondaryDrawerItem settingDrawerItem = new SecondaryDrawerItem();
        settingDrawerItem
                .withIdentifier(SETTINGS_ID)
                .withName(R.string.settings_string)
                .withIcon(R.drawable.ic_settings_black_24dp);

        SecondaryDrawerItem helpDrawerItem = new SecondaryDrawerItem();
        helpDrawerItem
                .withIdentifier(HELP_ID)
                .withName(R.string.help_string)
                .withIcon(R.drawable.ic_help_black_24dp);

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        loginDrawerItem,
                        mapDrawerItem,
                        listRegionsDrawerItem,
                        new DividerDrawerItem(),
                        settingDrawerItem,
                        helpDrawerItem)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        int itemId = (int) drawerItem.getIdentifier();

                        switch (itemId) {
                            case MAP_ID:
                                onMapItemSelected();
                                break;
                            case LIST_REGIONS_ID:
                                onRegionsItemSelected();
                                break;
                            case SETTINGS_ID:
                                onSettingItemSelected();
                                break;
                            case HELP_ID:
                                onHelpItemSelected();
                                break;
                            case LOGIN_ID:
                                onLoginItemSelected();
                        }
                        return false;
                    }
                })
                .build();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (null == profile) {
            editor.putBoolean(IS_AUTH_KEY, false);
        } else {
            editor.putBoolean(IS_AUTH_KEY, true);
            editor.putString(USERNAME_KEY, profile.getUsername());
            editor.putString(EMAIL_KEY, profile.getEmail());
            editor.putString(PASSWORD_KEY, profile.getPassword());
        }

        editor.apply();

    }

    @Override
    public void onLoginLinkClicked() {
        loginState = LoginState.LOGIN;
        updateLoginDrawerItem();
        onLoginItemSelected();
    }

    @Override
    public void onLoginSuccessfully(Profile profile) {
        this.profile = profile;

        IProfile iProfile = new ProfileDrawerItem()
                .withEmail(profile.getEmail())
                .withName(profile.getUsername());

        accountHeader.removeProfile(0);
        accountHeader.addProfiles(iProfile);

        loginState = LoginState.SINGED_UP;
        updateLoginDrawerItem();
        onLoginItemSelected();
    }

    @Override
    public void onSignOutClicked() {
        loginState = LoginState.LOGIN;
        this.profile = null;
        updateLoginDrawerItem();
        onLoginItemSelected();
    }

    @Override
    public void onRegisterLinkClicked() {
        loginState = LoginState.REGISTER;
        updateLoginDrawerItem();
        onLoginItemSelected();
    }

    @Override
    public void onRegisteredSuccessfully(Profile profile) {
        onLoginSuccessfully(profile);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    private void onMapItemSelected() {
        toolbar.setTitle(R.string.map_string);
    }

    private void onRegionsItemSelected() {
        toolbar.setTitle(R.string.regions_string);
    }

    private void onSettingItemSelected() {
        toolbar.setTitle(R.string.settings_string);
    }

    private void onHelpItemSelected() {
        toolbar.setTitle(R.string.help_string);
    }

    private void onLoginItemSelected() {

        switch (loginState) {
            case LOGIN:
                toolbar.setTitle(R.string.login_string);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.layout_for_showing_fragment, LoginFragment.newInstance(), LoginFragment.class.getName())
                        .commit();
                break;
            case REGISTER:
                toolbar.setTitle(getString(R.string.register_string));

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.layout_for_showing_fragment, RegisterFragment.newInstance(), RegisterFragment.class.getName())
                        .commit();
                break;
            case SINGED_UP:
                toolbar.setTitle(getString(R.string.account_text));

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.layout_for_showing_fragment, AccountFragment.newInstance(profile), AccountFragment.class.getName())
                        .commit();
                break;
        }

    }

    private void updateLoginDrawerItem() {
        switch (loginState) {
            case LOGIN:
                loginDrawerItem
                        .withIdentifier(LOGIN_ID)
                        .withName(R.string.login_text)
                        .withIcon(R.drawable.ic_person_black_24dp);
                break;

            case SINGED_UP:
                loginDrawerItem
                        .withIdentifier(LOGIN_ID)
                        .withName(R.string.account_text)
                        .withIcon(R.drawable.ic_person_black_24dp);
                break;

            case REGISTER:
                loginDrawerItem
                        .withIdentifier(LOGIN_ID)
                        .withName(R.string.create_account_text)
                        .withIcon(R.drawable.ic_person_black_24dp);
                break;
        }

        drawer.updateItem(loginDrawerItem);
    }

    enum LoginState {
        LOGIN, REGISTER, SINGED_UP
    }
}
