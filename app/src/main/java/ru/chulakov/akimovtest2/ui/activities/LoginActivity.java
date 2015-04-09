package ru.chulakov.akimovtest2.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;

import ru.chulakov.akimovtest2.R;

public class LoginActivity extends BaseActivity {

    private static String KEY_TOKEN = "VK_ACCESS_TOKEN";
    private static final String APPLICATION_ID = "4857678";

    private static final String[] sMyScope = new String[] { VKScope.PHOTOS };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        VKUIHelper.onCreate(this);
        TextView tvText = (TextView) findViewById(R.id.tvText);

        if(isConnectToInternet()) {
            VKSdk.initialize(sdkListener, APPLICATION_ID);
            VKSdk.authorize(sMyScope);
        } else {
            tvText.setText(getString(R.string.no_internet_connection));
        }
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

    private VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(sMyScope);
        }

        @Override
        public void onAccessDenied(VKError authorizationError) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setMessage(authorizationError.errorMessage)
                    .show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            newToken.saveTokenToSharedPreferences(LoginActivity.this, KEY_TOKEN);
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            finish();
            startActivity(i);
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            finish();
            startActivity(i);
        }
    };

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }
}
