package com.noveogroup.teamzolotov.iwashere.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPolygonStyle;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.util.ImageUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.logging.Logger;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String MAP_FRAGMENT_TAG = "map";

    private final static int MAP_ID = 1;
    private final static int LIST_REGIONS_ID = 2;
    private final static int SETTINGS_ID = 3;
    private final static int HELP_ID = 4;

    private final static Logger logger = Logger.getLogger(MainActivity.class.getName());

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            GoogleMapOptions options = new GoogleMapOptions();
            options.camera(new CameraPosition.Builder().target(new LatLng(55.0415, 82.9346)).build());
            SupportMapFragment mapFragment = SupportMapFragment.newInstance(options);
            mapFragment.setRetainInstance(true);
            mapFragment.getMapAsync(this);
            transaction.add(R.id.layout_for_showing_fragment, mapFragment, MAP_FRAGMENT_TAG);
            transaction.commit();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        try {
            GeoJsonLayer layer = new GeoJsonLayer(googleMap, R.raw.adm4_region, this);
            for (GeoJsonFeature feature : layer.getFeatures()) {
                GeoJsonPolygonStyle polygonStyle = feature.getPolygonStyle();
                polygonStyle.setFillColor(0xFFE5DAA5);
                polygonStyle.setStrokeWidth(1);
                feature.setPolygonStyle(polygonStyle);
            }
            layer.addLayerToMap();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onPostCreate(@Nullable final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.map_string);

        View headerView = getLayoutInflater().inflate(R.layout.header_view, null);
        ImageView headerImageView = (ImageView) headerView.findViewById(R.id.header_view_image);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) headerImageView.getLayoutParams();
        layoutParams.height = ImageUtil.getAppropriateHeight(this);

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
                .withHeader(headerView)
                .addDrawerItems(mapDrawerItem,
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
                        }
                        return false;
                    }
                })
                .build();

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    private void onMapItemSelected() {
        toolbar.setTitle(R.string.map_string);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
        if (mapFragment == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SupportMapFragment newSupportMapFragment = SupportMapFragment.newInstance();
            newSupportMapFragment.setRetainInstance(true);
            newSupportMapFragment.getMapAsync(this);
            transaction.replace(R.id.layout_for_showing_fragment, newSupportMapFragment, MAP_FRAGMENT_TAG);
            transaction.commit();
        }
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
}
