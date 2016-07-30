package com.noveogroup.teamzolotov.iwashere.activities;

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
import com.noveogroup.teamzolotov.iwashere.fragments.LoginFragment;
import com.noveogroup.teamzolotov.iwashere.fragments.RegisterFragment;
import com.noveogroup.teamzolotov.iwashere.model.Profile;

import java.util.logging.Logger;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements Registrable, Loginable {
    private final static int LOGIN_ID = 0;
    private final static int MAP_ID = 1;
    private final static int LIST_REGIONS_ID = 2;
    private final static int SETTINGS_ID = 3;
    private final static int HELP_ID = 4;

    private final static Logger logger = Logger.getLogger(MainActivity.class.getName());

    private AccountHeader accountHeader;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    protected void onPostCreate(@Nullable final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.map_string);

        Profile profile = new Profile("No e-mail address", "Anonymous");

        IProfile iProfile = new ProfileDrawerItem()
                .withEmail(profile.getEmail())
                .withName(profile.getUsername());

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withTextColorRes(R.color.primary_text)
                .addProfiles(iProfile)
                .withSelectionListEnabled(false)
                .build();

        PrimaryDrawerItem loginDrawerItem = new PrimaryDrawerItem();
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

        new DrawerBuilder()
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
    public void onLoginLinkClicked() {
        onLoginItemSelected();
    }

    @Override
    public void onLoginSuccessfully(Profile profile) {
        IProfile iProfile = new ProfileDrawerItem()
                .withEmail(profile.getEmail())
                .withName(profile.getUsername());

        accountHeader.removeProfile(0);
        accountHeader.addProfiles(iProfile);

        onMapItemSelected();
    }

    @Override
    public void onRegisterLinkClicked() {
        toolbar.setTitle(getString(R.string.register_string));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_for_showing_fragment, RegisterFragment.newInstance(), RegisterFragment.class.getName())
                .commit();
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
        toolbar.setTitle(R.string.login_string);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_for_showing_fragment, LoginFragment.newInstance(), LoginFragment.class.getName())
                .commit();

    }
}
