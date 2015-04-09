package ru.chulakov.akimovtest2.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ru.chulakov.akimovtest2.R;
import ru.chulakov.akimovtest2.ui.fragments.MapFragment;

public class MapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        MapFragment fragment = MapFragment.createMapFragment(null,
                null,
                getIntent().getDoubleExtra(KEY_LATITUDE, Double.MIN_VALUE),
                getIntent().getDoubleExtra(KEY_LONGITUDE, Double.MIN_VALUE) );

        addFragment(fragment,false);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.container;
    }

    public static void startMapActivity(Context context, double latitude, double longitude) {
        Intent intent = new Intent(context,MapActivity.class);
        intent.putExtra(KEY_LATITUDE, latitude);
        intent.putExtra(KEY_LONGITUDE, longitude);
        context.startActivity(intent);
    }

    private static String KEY_LATITUDE = "key_latitude";
    private static String KEY_LONGITUDE = "key_longitude";
}
