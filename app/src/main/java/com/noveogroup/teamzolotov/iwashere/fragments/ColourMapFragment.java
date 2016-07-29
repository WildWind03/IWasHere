package com.noveogroup.teamzolotov.iwashere.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPolygonStyle;
import com.j256.ormlite.dao.Dao;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.database.ContentDescriptor;
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

    private Dao<Region, Integer> dao;

    public static ColourMapFragment newInstance(Dao<Region, Integer> dao) {
        ColourMapFragment fragment = new ColourMapFragment();
        fragment.dao = dao;
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        Observable.create(new Observable.OnSubscribe<GeoJsonLayer>() {
            @Override
            public void call(Subscriber<? super GeoJsonLayer> subscriber) {
                try {
                    GeoJsonLayer layer = new GeoJsonLayer(googleMap, R.raw.adm4_region, getContext());
                    GeoJsonPolygonStyle defaultStyle = layer.getDefaultPolygonStyle();
                    defaultStyle.setFillColor(0);
                    List<Region> regions = dao.queryForAll();
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
}
