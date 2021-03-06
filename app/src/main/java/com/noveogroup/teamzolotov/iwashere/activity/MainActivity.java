package com.noveogroup.teamzolotov.iwashere.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseUser;
import com.j256.ormlite.dao.Dao;
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
import com.noveogroup.teamzolotov.iwashere.api.NominatimApi;
import com.noveogroup.teamzolotov.iwashere.api.NominatimService;
import com.noveogroup.teamzolotov.iwashere.database.RegionOrmLiteOpenHelper;
import com.noveogroup.teamzolotov.iwashere.fragment.AccountFragment;
import com.noveogroup.teamzolotov.iwashere.fragment.ColourMapFragment;
import com.noveogroup.teamzolotov.iwashere.fragment.DoWithProfile;
import com.noveogroup.teamzolotov.iwashere.fragment.HelpFragment;
import com.noveogroup.teamzolotov.iwashere.fragment.LoginFragment;
import com.noveogroup.teamzolotov.iwashere.fragment.RegionListFragment;
import com.noveogroup.teamzolotov.iwashere.fragment.RegisterFragment;
import com.noveogroup.teamzolotov.iwashere.model.Place;
import com.noveogroup.teamzolotov.iwashere.model.Profile;
import com.noveogroup.teamzolotov.iwashere.model.Region;
import com.noveogroup.teamzolotov.iwashere.util.BackupRestoreUtils;
import com.noveogroup.teamzolotov.iwashere.util.FragmentUtils;
import com.noveogroup.teamzolotov.iwashere.util.InternetUtils;
import com.noveogroup.teamzolotov.iwashere.util.LoginUtils;
import com.noveogroup.teamzolotov.iwashere.util.RegionUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseDatabaseActivity implements Registrable, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ColourMapFragment.MapRecreateListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String MAP_FRAGMENT_TAG = "map";
    private static final String REGIONS_FRAGMENT_TAG = "regions";
    private final static String HELP_FRAGMENT_TAG = "help";

    private final static int LOGIN_ID = 0;
    private final static int MAP_ID = 1;
    private final static int LIST_REGIONS_ID = 2;
    private final static int SETTINGS_ID = 3;
    private final static int HELP_ID = 4;
    private final static int RESTORE_ID = 5;
    private final static int BACKUP_ID = 6;

    private final static String IS_AUTH_KEY = "IS AUTH_KEY";
    private final static String EMAIL_KEY = "EMAIL_KEY";
    private final static String USERNAME_KEY = "USERNAME_KEY";
    private final static String PASSWORD_KEY = "PASSWORD_KEY";
    private final static String UID_KEY = "UID_KEY";

    private final static String CURRENT_ITEM_STATE_KEY = "CURRENT_ITEM_STATE_KEY";


    private LoginState loginState = LoginState.LOGIN;

    private int currentItemState = MAP_ID;

    private AccountHeader accountHeader;
    private Profile profile;

    private SharedPreferences sharedPreferences;

    private Drawer drawer;

    private PrimaryDrawerItem backupDrawerItem;
    private PrimaryDrawerItem restoreDrawerItem;

    private GoogleApiClient googleApiClient;
    private Subscription reverseGeoSubscription;

    private FirebaseUser firebaseUser;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            FragmentUtils.addFragment(ColourMapFragment.newInstance(), R.id.layout_for_showing_fragment,
                    getSupportFragmentManager(), MAP_FRAGMENT_TAG);
        }

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    @Override
    protected void onPostCreate(@Nullable final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        boolean isAuth = sharedPreferences.getBoolean(IS_AUTH_KEY, false);

        IProfile iProfile;

        restoreDrawerItem = new PrimaryDrawerItem();
        restoreDrawerItem
                .withIdentifier(RESTORE_ID)
                .withName(R.string.restore_text)
                .withIcon(R.drawable.ic_restore_black_24dp);

        backupDrawerItem = new PrimaryDrawerItem();
        backupDrawerItem
                .withIdentifier(BACKUP_ID)
                .withName(R.string.backup_text)
                .withIcon(R.drawable.ic_backup_black_24dp);

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

            backupDrawerItem.withEnabled(true);
            restoreDrawerItem.withEnabled(true);
        } else {
            iProfile = new ProfileDrawerItem()
                    .withEmail(getString(R.string.default_email))
                    .withName(getString(R.string.default_username));

            backupDrawerItem.withEnabled(false);
            restoreDrawerItem.withEnabled(false);

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
                .withName(R.string.info_string)
                .withIcon(R.drawable.ic_info_outline_black_24dp);

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        mapDrawerItem,
                        listRegionsDrawerItem,
                        backupDrawerItem,
                        restoreDrawerItem,
                        new DividerDrawerItem(),
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
                                break;
                            case RESTORE_ID:
                                onRestoreItemSelected();
                                break;
                            case BACKUP_ID:
                                onBackupItemSelected();
                                break;
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

        if (currentItemState == LOGIN_ID) {
            drawer.deselect();
        } else {
            drawer.setSelection(currentItemState);
        }

        switch (currentItemState) {
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
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onPause() {
        if (reverseGeoSubscription != null) {
            reverseGeoSubscription.unsubscribe();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_ITEM_STATE_KEY, currentItemState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit_current_region:
                submitCurrentRegion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        showSnackBar(R.string.google_connection_failed);
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Snackbar.make(findViewById(R.id.layout_for_showing_fragment),
                connectionResult.getErrorMessage() != null ? connectionResult.getErrorMessage() : getString(R.string.google_connection_failed),
                Snackbar.LENGTH_LONG).show();
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
    public void onLoginSuccessfully(Profile profile, FirebaseUser firebaseUser) {
        this.profile = profile;
        this.firebaseUser = firebaseUser;

        updateAccountHeader(profile);

        loginState = LoginState.SINGED_UP;
        onAccountHeaderClicked();
        updateAuthState();

        backupDrawerItem.withEnabled(true);
        restoreDrawerItem.withEnabled(true);

        drawer.updateItem(backupDrawerItem);
        drawer.updateItem(restoreDrawerItem);
    }

    @Override
    public void onSignOutClicked() {
        loginState = LoginState.LOGIN;

        this.profile = null;
        this.firebaseUser = null;

        updateAccountHeader(null);
        onAccountHeaderClicked();
        updateAuthState();

        backupDrawerItem.withEnabled(false);
        restoreDrawerItem.withEnabled(false);

        drawer.updateItem(backupDrawerItem);
        drawer.updateItem(restoreDrawerItem);
    }

    @Override
    public void onRegisterLinkClicked() {
        loginState = LoginState.REGISTER;
        onAccountHeaderClicked();
    }

    @Override
    public void onRegisteredSuccessfully(Profile profile, FirebaseUser firebaseUser) {
        onLoginSuccessfully(profile, firebaseUser);
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

    private void forceMapItemSelected() {
        toolbar.setTitle(R.string.map_string);
        FragmentUtils.replaceFragment(ColourMapFragment.newInstance(), R.id.layout_for_showing_fragment,
                getSupportFragmentManager(), MAP_FRAGMENT_TAG);
        currentItemState = MAP_ID;
    }

    @Override
    public void onRecreate() {
        forceMapItemSelected();
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

    private void forceRegionsItemSelected() {
        toolbar.setTitle(R.string.regions_string);
        FragmentUtils.replaceFragment(RegionListFragment.newInstance(), R.id.layout_for_showing_fragment,
                getSupportFragmentManager(), REGIONS_FRAGMENT_TAG);

        currentItemState = LIST_REGIONS_ID;
    }

    private void onSettingItemSelected() {
        toolbar.setTitle(R.string.settings_string);
        currentItemState = SETTINGS_ID;
    }

    private void onHelpItemSelected() {
        toolbar.setTitle(R.string.info_string);
        currentItemState = HELP_ID;

        HelpFragment helpFragment = (HelpFragment) getSupportFragmentManager().findFragmentByTag(HELP_FRAGMENT_TAG);
        if (helpFragment == null) {
            FragmentUtils.replaceFragment(HelpFragment.newInstance(getString(R.string.default_license_title),
                    GoogleApiAvailability.getInstance().getOpenSourceSoftwareLicenseInfo(this)),
                    R.id.layout_for_showing_fragment,
                    getSupportFragmentManager(), HELP_FRAGMENT_TAG);
        }
    }

    private void onRestoreItemSelected() {
        if (null == firebaseUser) {
            login(new AbleToDoSomething() {
                @Override
                public void doSomething() {
                    restore(firebaseUser);
                }
            });
        } else {
            restore(firebaseUser);
        }
    }

    private void onBackupItemSelected() {
        if (null == firebaseUser) {
            login(new AbleToDoSomething() {
                @Override
                public void doSomething() {
                    backup(firebaseUser);
                }
            });
        } else {
            backup(firebaseUser);
        }
    }

    private void backup(FirebaseUser firebaseUser) {
        if (!InternetUtils.isOnline(this)) {
            showSnackBar(R.string.no_internet_message);
            return;
        }

        final Snackbar snackbar = Snackbar.make(findViewById(R.id.layout_for_showing_fragment), getText(R.string.backup_in_progress_message), Snackbar.LENGTH_INDEFINITE);
        snackbar.show();


        try {
            String currentDBPath = getDatabasePath(RegionOrmLiteOpenHelper.DATABASE_NAME).getPath();
            File backupDB = new File(currentDBPath);

            if (backupDB.canRead()) {
                BackupRestoreUtils.backup(backupDB, firebaseUser, new BackupRestoreUtils.OnBackupFailed() {
                    @Override
                    public void handle(Exception e) {
                        snackbar.dismiss();
                        showSnackBar(R.string.backup_troubles_title);
                    }
                }, new BackupRestoreUtils.OnBackupSuccess() {
                    @Override
                    public void handle() {
                        snackbar.dismiss();
                        showSnackBar(R.string.backup_successfully);
                    }
                });
            }
        } catch (Exception e) {
            Log.d(TAG, "Unable to backup");
            snackbar.dismiss();
            showSnackBar(R.string.backup_troubles_title);
        }
    }

    private void restore(FirebaseUser firebaseUser) {
        if (!InternetUtils.isOnline(this)) {
            showSnackBar(R.string.no_internet_message);
            return;
        }

        final Snackbar snackbar = Snackbar.make(findViewById(R.id.layout_for_showing_fragment), getText(R.string.restore_in_progress_message), Snackbar.LENGTH_INDEFINITE);
        snackbar.show();

        try {
            String currentDBPath = getDatabasePath(RegionOrmLiteOpenHelper.DATABASE_NAME).getPath();
            File restoreDB = new File(currentDBPath);

            if (restoreDB.canWrite()) {
                BackupRestoreUtils.restore(firebaseUser, restoreDB, new BackupRestoreUtils.OnRestoreSuccessfully() {
                    @Override
                    public void handle() {
                        snackbar.dismiss();
                        showSnackBar(R.string.successfully_restored);
                        if (currentItemState == MAP_ID) {
                            forceMapItemSelected();
                        }

                        if (currentItemState == LIST_REGIONS_ID) {
                            forceRegionsItemSelected();
                        }
                    }
                }, new BackupRestoreUtils.OnRestoreFailed() {
                    @Override
                    public void handle(Exception e) {
                        snackbar.dismiss();
                        showSnackBar(R.string.restore_troubles_title);
                    }
                });

            }
        } catch (Exception e) {
            Log.d(TAG, "Unable to restore");
            showSnackBar(R.string.restore_troubles_title);
        }
    }

    private void login(final AbleToDoSomething onLoginSuccess) {
        if (!InternetUtils.isOnline(this)) {
            showSnackBar(R.string.no_internet_message);
            return;
        }

        final Snackbar snackbar = Snackbar.make(findViewById(R.id.layout_for_showing_fragment), getText(R.string.connect_with_server), Snackbar.LENGTH_INDEFINITE);
        snackbar.show();


        LoginUtils.login(profile.getEmail(), profile.getPassword(), this, new DoWithProfile() {
            @Override
            public void onSuccess(FirebaseUser firebaseUser, String password) {
                MainActivity.this.firebaseUser = firebaseUser;
                snackbar.dismiss();
                onLoginSuccess.doSomething();
            }

            @Override
            public void onError(Exception e) {
                snackbar.dismiss();
                showMessage(R.string.auth_troubles_title, e.getMessage());
                MainActivity.this.firebaseUser = null;
            }
        });
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
        drawer.deselect();

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

    private void showSnackBar(int resId) {
        final Snackbar snackbar = Snackbar.make(findViewById(R.id.layout_for_showing_fragment), getText(resId), Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void showSnackBar(String msg) {
        final Snackbar snackbar = Snackbar.make(findViewById(R.id.layout_for_showing_fragment), msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void submitCurrentRegion() {
        if (googleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                NominatimService service = NominatimApi.getInstance();
                if (location != null) {
                    reverseGeoSubscription = service.getPlace(location.getLatitude(), location.getLongitude())
                            .subscribeOn(Schedulers.computation())
                            .observeOn(Schedulers.newThread())
                            .flatMap(new Func1<Place, Observable<Place>>() {
                                @Override
                                public Observable<Place> call(Place place) {
                                    String state = place.getState();
                                    try {
                                        Dao<Region, Integer> dao = openHelper.getDao();
                                        List<Region> result = dao.queryForEq("osm_id", RegionUtils.getRegionOsm(state));
                                        if (!result.isEmpty()) {
                                            Region region = result.get(0);
                                            if (!region.isVisited()) {
                                                region.setVisited(true);
                                                dao.update(region);
                                            }
                                        }
                                    } catch (SQLException e) {
                                        Log.d(TAG, "Failed accessing db after rev geocoding");
                                        e.printStackTrace();
                                    }
                                    return Observable.just(place);
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Place>() {
                                @Override
                                public void onCompleted() {
                                    Log.d(TAG, "Updated db based on geolocation");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    showSnackBar(R.string.location_error);
                                    Log.d(TAG, "Failed to update db based on geolocation", e);
                                }

                                @Override
                                public void onNext(Place place) {
                                    showSnackBar(getString(R.string.location_success) + " " + place.getState());
                                    forceMapItemSelected();
                                }
                            });
                } else {
                    showSnackBar(R.string.location_error);
                }
            } else {
                showSnackBar(R.string.missing_permission);
            }
        } else {
            showSnackBar(R.string.google_connection_failed);
        }
    }

    enum LoginState {
        LOGIN, REGISTER, SINGED_UP
    }

    private interface AbleToDoSomething {
        void doSomething();
    }
}
