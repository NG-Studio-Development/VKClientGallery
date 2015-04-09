package ru.chulakov.akimovtest2.ui.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

public abstract class BaseActivity extends ActionBarActivity {

    public void switchFragment(Fragment fragment, boolean saveInBackStack) {
        updateFragment(fragment, saveInBackStack, false);
    }

    public void addFragment(Fragment fragment, boolean saveInBackStack) {
        updateFragment(fragment, saveInBackStack, true);
    }

    protected void updateFragment(Fragment fragment, boolean saveInBackStack, boolean add) {
        final String fragmentTag = ((Object) fragment).getClass().getName();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (add) {
            ft.add(getFragmentContainerId(), fragment, fragmentTag);
        } else {
            ft.replace(getFragmentContainerId(), fragment, fragmentTag);
        }
        if (saveInBackStack) {
            ft.addToBackStack(fragmentTag);
        }
        ft.commit();
    }

    public boolean isConnectToInternet() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null;
    }

    protected abstract int getFragmentContainerId();





}

