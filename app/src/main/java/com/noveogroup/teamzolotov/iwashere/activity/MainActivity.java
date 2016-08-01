package com.noveogroup.teamzolotov.iwashere.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import com.noveogroup.teamzolotov.iwashere.fragment.ColourMapFragment;
import com.noveogroup.teamzolotov.iwashere.fragment.RegionListFragment;
import com.noveogroup.teamzolotov.iwashere.util.FragmentUtils;
import com.noveogroup.teamzolotov.iwashere.util.ImageUtils;
import com.noveogroup.teamzolotov.iwashere.fragments.AccountFragment;
import com.noveogroup.teamzolotov.iwashere.fragments.LoginFragment;
import com.noveogroup.teamzolotov.iwashere.fragments.RegisterFragment;
import com.noveogroup.teamzolotov.iwashere.model.Profile;

import java.util.logging.Logger;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements Registrable{

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String MAP_FRAGMENT_TAG = "map";
    private static final String REGIONS_FRAGMENT_TAG = "regions";

    private final static int LOGIN_ID = 0;
    private final static int MAP_ID = 1;
    private final static int LIST_REGIONS_ID = 2;
    private final static int SETTINGS_ID = 3;
    private final static int HELP_ID = 4;

    private final static String IS_AUTH_KEY = "IS AUTH_KEY";
    private final static String EMAIL_KEY = "EMAIL_KEY";
    private final static String USERNAME_KEY = "USERNAME_KEY";
    private final static String PASSWORD_KEY = "PASSWORD_KEY";
    private final static String UID_KEY = "UID_KEY";

    private final static String CURRENT_ITEM_STATE_KEY = "CURRENT_ITEM_STATE_KEY";

    private final static Logger logger = Logger.getLogger(MainActivity.class.getName());

    private LoginState loginState = LoginState.LOGIN;

    private int currentItemState = MAP_ID;

    private AccountHeader accountHeader;
    private Profile profile;

    private SharedPreferences sharedPreferences;

    private Drawer drawer;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            FragmentUtils.addFragment(ColourMapFragment.newInstance(), R.id.layout_for_showing_fragment,
                    getSupportFragmentManager(), MAP_FRAGMENT_TAG);
        }
    }


    @Override
    protected void onPostCreate(@Nullable final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        sharedPreferences = getPreferences(MODE_PRIVATE);

        boolean isAuth = sharedPreferences.getBoolean(IS_AUTH_KEY, false);

        IProfile iProfile;

        if (isAuth) {
            String username = sharedPreferences.getString(USERNAME_KEY, getResources().getString(R.string.default_username));
            String email = sharedPreferences.getString(EMAIL_KEY, getResources().getString(R.string.default_email));
            String password = sharedPreferences.getString(PASSWORD_KEY, null);
            String uid = sharedPreferences.getString(UID_KEY, null);

            profile = new Profile(email, username, password, uid);

            iProfile = new ProfileDrawerItem()
                    .withEmail(email)
                    .withName(username);

            loginState = LoginState.SINGED_UP;
        } else {
            iProfile = new ProfileDrawerItem()
                    .withEmail(getString(R.string.default_email))
                    .withName(getString(R.string.default_username));;
        }

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.map_string);

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withTextColorRes(R.color.primary_text)
                .addProfiles(iProfile)
                .withSelectionListEnabled(false)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        onAccountHeaderClicked();
                        return false;
                    }
                })
                .build();
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
                                onAccountHeaderClicked();
                        }
                        return false;
                    }
                })
                .build();

        if (null != savedInstanceState) {
            currentItemState = savedInstanceState.getInt(CURRENT_ITEM_STATE_KEY, MAP_ID);
        } else {
            currentItemState = MAP_ID;
        }

        drawer.setSelection(currentItemState);

        switch(currentItemState) {
            case MAP_ID:
                onMapItemSelected();
                break;

            case LIST_REGIONS_ID:
                onRegionsItemSelected();
                break;

            case LOGIN_ID:
                onAccountHeaderClicked();
                break;

            case SETTINGS_ID:
                onSettingItemSelected();
                break;

            case HELP_ID:
                onHelpItemSelected();
                break;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_ITEM_STATE_KEY, currentItemState);
    }


    private void updateAuthState() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (null == profile) {
            editor.putBoolean(IS_AUTH_KEY, false);
        } else {
            editor.putBoolean(IS_AUTH_KEY, true);
            editor.putString(USERNAME_KEY, profile.getUsername());
            editor.putString(EMAIL_KEY, profile.getEmail());
            editor.putString(PASSWORD_KEY, profile.getPassword());
            editor.putString(UID_KEY, profile.getUid());
        }

        editor.apply();
    }

    @Override
    public void onLoginLinkClicked() {
        loginState = LoginState.LOGIN;
        onAccountHeaderClicked();
    }

    @Override
    public void onLoginSuccessfully(Profile profile) {
        this.profile = profile;

        updateAccountHeader(profile);

        loginState = LoginState.SINGED_UP;
        onAccountHeaderClicked();
        updateAuthState();
    }

    @Override
    public void onSignOutClicked() {
        loginState = LoginState.LOGIN;
        this.profile = null;
        updateAccountHeader(null);
        onAccountHeaderClicked();
        updateAuthState();
    }

    @Override
    public void onRegisterLinkClicked() {
        loginState = LoginState.REGISTER;
        onAccountHeaderClicked();
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
        ColourMapFragment mapFragment = (ColourMapFragment) getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
        if (mapFragment == null) {
            FragmentUtils.replaceFragment(ColourMapFragment.newInstance(), R.id.layout_for_showing_fragment,
                    getSupportFragmentManager(), MAP_FRAGMENT_TAG);
        }
        currentItemState = MAP_ID;
    }

    private void onRegionsItemSelected() {
        toolbar.setTitle(R.string.regions_string);
        RegionListFragment regionsFragment = (RegionListFragment) getSupportFragmentManager().findFragmentByTag(REGIONS_FRAGMENT_TAG);
        if (regionsFragment == null) {
            FragmentUtils.replaceFragment(RegionListFragment.newInstance(), R.id.layout_for_showing_fragment,
                    getSupportFragmentManager(), REGIONS_FRAGMENT_TAG);
        }

        currentItemState = LIST_REGIONS_ID;
    }

    private void onSettingItemSelected() {
        toolbar.setTitle(R.string.settings_string);
        currentItemState = SETTINGS_ID;
    }

    private void onHelpItemSelected() {
        toolbar.setTitle(R.string.help_string);
        currentItemState = HELP_ID;
    }

    private void updateAccountHeader(@Nullable Profile profile) {
        this.profile = profile;

        IProfile iProfile;

        if (null != profile) {
            iProfile = new ProfileDrawerItem()
                    .withEmail(profile.getEmail())
                    .withName(profile.getUsername());
        } else {
            iProfile = new ProfileDrawerItem()
                    .withEmail(getResources().getString(R.string.default_email))
                    .withName(getResources().getString(R.string.default_username));
        }

        accountHeader.removeProfile(0);
        accountHeader.addProfiles(iProfile);
    }

    private void onAccountHeaderClicked() {
        currentItemState = LOGIN_ID;

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

    enum LoginState {
        LOGIN, REGISTER, SINGED_UP
    }
}
