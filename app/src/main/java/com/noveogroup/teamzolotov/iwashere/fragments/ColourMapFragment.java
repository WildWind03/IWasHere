package com.noveogroup.teamzolotov.iwashere.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPolygonStyle;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.database.ContentDescriptor;
import com.noveogroup.teamzolotov.iwashere.database.RegionOrmLiteOpenHelper;
import com.noveogroup.teamzolotov.iwashere.model.Region;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ColourMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static final String TAG = ColourMapFragment.class.getSimpleName();

    private RegionOrmLiteOpenHelper openHelper;

    public static ColourMapFragment newInstance() {
        return new ColourMapFragment();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (openHelper == null) {
            openHelper = OpenHelperManager.getHelper(getContext(), RegionOrmLiteOpenHelper.class);
        }

        setRetainInstance(true);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        googleMap.setIndoorEnabled(false);
        googleMap.setBuildingsEnabled(false);
        LatLngBounds russia = new LatLngBounds(new LatLng(41, 19), new LatLng(82, 169));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(russia.getCenter(), 0));
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (cameraPosition.zoom > 5) {
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(5));
                }
            }
        });
        UiSettings settings = googleMap.getUiSettings();
        settings.setCompassEnabled(false);
        settings.setRotateGesturesEnabled(false);
        settings.setTiltGesturesEnabled(false);

        Observable.create(new Observable.OnSubscribe<GeoJsonLayer>() {
            @Override
            public void call(Subscriber<? super GeoJsonLayer> subscriber) {
                try {
                    GeoJsonLayer layer = new GeoJsonLayer(googleMap, R.raw.adm4_region, getContext());
                    GeoJsonPolygonStyle defaultStyle = layer.getDefaultPolygonStyle();
                    defaultStyle.setFillColor(0);
                    List<Region> regions = openHelper.getDao().queryForAll();
                    for (GeoJsonFeature feature : layer.getFeatures()) {
                        GeoJsonPolygonStyle polygonStyle = new GeoJsonPolygonStyle();
                        polygonStyle.setStrokeWidth(1);
                        polygonStyle.setFillColor(0);
                        for (Region r : regions) {
                            if (r.getOsmId() == Integer.parseInt(feature.getProperty("OSM_ID")) &&
                                    r.isVisited()) {
                                polygonStyle.setFillColor(0xFF64DD17);
                                break;
                            }
                        }
                        feature.setPolygonStyle(polygonStyle);
                    }
                    subscriber.onNext(layer);
                } catch (JSONException | IOException | SQLException e) {
                    subscriber.onError(e);
                }
            }
        })
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<GeoJsonLayer>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "Error while parsing geojson/regions db", e);
                }

                @Override
                public void onNext(GeoJsonLayer geoJsonLayer) {
                    geoJsonLayer.addLayerToMap();
                }
            });

    }

    @Override
    public void onDestroy() {
        if (openHelper != null) {
            OpenHelperManager.releaseHelper();
            openHelper = null;
        }
        super.onDestroy();
    }
}
