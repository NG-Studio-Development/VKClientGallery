package ru.chulakov.akimovtest2.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.chulakov.akimovtest2.R;
import ru.chulakov.akimovtest2.ui.activities.MapActivity;

public class MapFragment extends BaseFragment <MapActivity> {

    private static String ARG_TITLE = "arg_title";

    private static String ARG_TEXT = "arg_text";

    private static String ARG_LATITUDE = "arg_latitude";

    private static String ARG_LONGITUDE = "arg_longitude";

    private static float ZOOM = 40f;

    private MapView mapView;
    private GoogleMap map;

    private double minLat = 0;
    private double maxLat = 0;
    private double minLong = 0;
    private double maxLong = 0;
    private int countMarker = 0;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_map;
    }

    public static MapFragment createMapFragment(String title, String text, double latitude, double longitude) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_TEXT, text);
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        MapsInitializer.initialize(getHostActivity());

        map = mapView.getMap();
        if (map == null)
            throw new Error("Map is null");
        map.getUiSettings().setZoomControlsEnabled(false);

        if(getArguments() != null) {
            String title = getArguments().getString(ARG_TITLE);
            String text = getArguments().getString(ARG_TEXT);
            double latitude = getArguments().getDouble(ARG_LATITUDE);
            double longitude = getArguments().getDouble(ARG_LONGITUDE);
            LatLng latLng = new LatLng(latitude, longitude);
            updateLocations(latLng, title, text);
            changingCameraPosition(latLng);
            zoomIn(latLng, ZOOM);
        }

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    protected void updateLocations(LatLng latLng, String title, String text) {
        minLat = countMarker>0?Math.min(latLng.latitude,minLat) : latLng.latitude;
        minLong = countMarker>0?Math.min(latLng.longitude,minLong) : latLng.longitude;
        maxLat = countMarker>0?Math.max(latLng.latitude, maxLat) : latLng.latitude;
        maxLong = countMarker>0?Math.max(latLng.longitude, maxLong) : latLng.longitude;
        countMarker++;

        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet(text));
    }

    protected void changingCameraPosition(LatLng latLng) {
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    protected void zoomIn(LatLng latLng,float zoom) {
        map.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
}
