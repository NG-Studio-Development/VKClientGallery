package ru.chulakov.akimovtest2.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.util.Log;

import ru.chulakov.akimovtest2.BuildConfig;
import ru.chulakov.akimovtest2.ui.activities.BaseActivity;

public abstract class BaseFragment<ActivityClass extends BaseActivity> extends Fragment {

	public static int sd;
    protected BaseFragment() {
        /* Nothing to do */
	}


	public abstract int getLayoutResID();

    public void findChildViews(View view) {
        /* Optional */
    }


    @Override
    public void onAttach(Activity activity) {
        if (BuildConfig.DEBUG) {
            //noinspection unchecked
            ActivityClass dummyActivity = (ActivityClass) activity;
            Log.d(((Object) this).getClass().getSimpleName(), String.format("Attached to activity: %s", dummyActivity));
        }
        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResID(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert view != null;
        findChildViews(view);
    }

    public ActivityClass getHostActivity() {
        //noinspection unchecked
        return (ActivityClass) getActivity();
    }

    /*public ObjectPool<Fragment> getFragmentPool() {

    }*/

}