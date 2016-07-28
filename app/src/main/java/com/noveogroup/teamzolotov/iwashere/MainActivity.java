package com.noveogroup.teamzolotov.iwashere;

import android.app.FragmentTransaction;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPolygonStyle;
import com.google.maps.android.kml.KmlLayer;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String MAP_FRAGMENT_TAG = "map";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            GoogleMapOptions options = new GoogleMapOptions();
            options.camera(new CameraPosition.Builder().target(new LatLng(55.0415, 82.9346)).build());
            MapFragment mapFragment = MapFragment.newInstance(options);
            mapFragment.getMapAsync(this);
            transaction.add(R.id.rel_layout, mapFragment, MAP_FRAGMENT_TAG);
            transaction.commit();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
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
}
