package ru.chulakov.akimovtest2.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import ru.chulakov.akimovtest2.Constants;
import ru.chulakov.akimovtest2.R;
import ru.chulakov.akimovtest2.ui.fragments.ImagePagerFragment;

public class ImageActivity extends BaseActivity {

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        long albumId = getIntent().getLongExtra(KEY_ALBUM_ID,-1);
        int position = getIntent().getIntExtra(Constants.Extra.IMAGE_POSITION, -1);

        if (savedInstanceState == null)
            addFragment(ImagePagerFragment.newImagePagerFragment(albumId, position), false);
    }

    public void showDialog(Bitmap image, FacebookCallback<Sharer.Result> facebookCallback ) {

        shareDialog.registerCallback(callbackManager, facebookCallback);

        if (ShareDialog.canShow(ShareLinkContent.class)) {

            SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            shareDialog.show(content);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static void startImageActivity(Context context, long albumId, int position) {
        Intent intent = new Intent(context,ImageActivity.class);
        intent.putExtra(KEY_ALBUM_ID, albumId);
        intent.putExtra(Constants.Extra.IMAGE_POSITION, position);
        context.startActivity(intent);
    }

    private static String KEY_ALBUM_ID = "key_album_id";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.container;
    }
}
