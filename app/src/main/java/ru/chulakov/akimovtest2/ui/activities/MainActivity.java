package ru.chulakov.akimovtest2.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import com.vk.sdk.VKUIHelper;
import ru.chulakov.akimovtest2.R;
import ru.chulakov.akimovtest2.ui.fragments.GalleryFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        VKUIHelper.onCreate(this);

        if (savedInstanceState == null)
            addFragment(GalleryFragment.newGalleryFragmentAlbum(),false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.container;
    }
}
