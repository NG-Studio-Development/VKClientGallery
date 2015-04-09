package ru.chulakov.akimovtest2.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.vk.sdk.api.model.VKList;

import ru.chulakov.akimovtest2.Constants;
import ru.chulakov.akimovtest2.R;
import ru.chulakov.akimovtest2.model.VKApiPhotoGEO;
import ru.chulakov.akimovtest2.model.db.PhotoDB;
import ru.chulakov.akimovtest2.ui.activities.ImageActivity;
import ru.chulakov.akimovtest2.ui.activities.MapActivity;
import ru.chulakov.akimovtest2.ui.adapters.ImageAdapter;


public class ImagePagerFragment extends BaseFragment<ImageActivity> {

    private static final String PARAM_ID_ALBUM = "param_id_album";

    private long albumId;

    private ViewPager pager;

    ImageAdapter adapter;

    ProgressDialog progress;

    public static ImagePagerFragment newImagePagerFragment(long albumId, int position) {
        ImagePagerFragment fragment = new ImagePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_ID_ALBUM,albumId);
        bundle.putInt(Constants.Extra.IMAGE_POSITION,position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ImagePagerFragment() {
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_image_pager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            albumId = getArguments().getLong(PARAM_ID_ALBUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_pager, container, false);

        pager = (ViewPager) view.findViewById(R.id.pager);

        setHasOptionsMenu(true);

        adapter = new ImageAdapter(getHostActivity(), R.layout.item_pager_image, PhotoDB.getVKApiPhotos(albumId));
        pager.setAdapter(adapter);
        pager.setCurrentItem(getArguments().getInt(Constants.Extra.IMAGE_POSITION, 0));

        adapter.setPlaceButtonClickListener(new ImageAdapter.PlaceButtonClickListener() {
            @Override
            public void onClick(VKList<VKApiPhotoGEO> list, int position) {
                progress = ProgressDialog.show( getHostActivity(),
                        getString(R.string.look_place),
                        getString(R.string.pleas_wait),
                        true );
                VKApiPhotoGEO vkApiPhotoGEO = list.get(position);
                MapActivity.startMapActivity(getHostActivity(), vkApiPhotoGEO.latitude, vkApiPhotoGEO.longitude);
            }
        });

        return view;
    }


    private FacebookCallback createFacebookCallback() {
        return new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) { progress.dismiss(); }

            @Override
            public void onCancel() { progress.dismiss(); }

            @Override
            public void onError(FacebookException e) {
                progress.dismiss();
                Toast.makeText(getHostActivity(),getString(R.string.facebook_share_error),Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if(progress != null)
            progress.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                getHostActivity().showDialog(adapter.getBitmap(pager.getCurrentItem()), createFacebookCallback());
                progress = ProgressDialog.show( getHostActivity(),
                        getString(R.string.share_photo),
                        getString(R.string.pleas_wait),
                        true );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
