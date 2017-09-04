package com.igor.vetrov.artzona;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;

public class Application extends android.app.Application{

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(@NonNull VKAccessToken oldToken, @NonNull VKAccessToken newToken) {
            if (newToken == null) {
                // VKAccessToken is invalid
                Intent intent = new Intent(Application.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

//        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());

        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }
}
