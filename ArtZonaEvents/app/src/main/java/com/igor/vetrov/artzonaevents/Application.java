package com.igor.vetrov.artzonaevents;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;

public class Application extends android.app.Application{

    private static final String TAG = "Application";

    VKAccessTokenTracker vkatt = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            Log.w(TAG, "Old token: " + oldToken.accessToken);
            Log.w(TAG, "New token: " + newToken.accessToken);
            if (newToken == null) {
                Intent intent = new Intent(Application.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                TokenLab.get(getApplicationContext()).addToken(new Token().setToken(newToken.accessToken));
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        vkatt.startTracking();
        VKSdk.initialize(this);
    }
}
