package com.igor.vetrov.artzona;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class VkLoginActivity extends FragmentActivity {

    private String[] scope = new String[] {
            VKScope.GROUPS, VKScope.MESSAGES, VKScope.FRIENDS
    };
    private VKAccessToken access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk_login);

        VKSdk.login(VkLoginActivity.this, scope);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                access_token = res;
                Log.w(String.format("access_token: %s", access_token.toString()), "");
                // успешная авторизация
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
                if (fragment == null) {
                    fragment = createFragment();
                    fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
                }

                Toast.makeText(getApplicationContext(), "ВЫ АВТОРИЗОВАЛИСЬ", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(VKError error) {
                // ошибка авторизации
                Toast.makeText(getApplicationContext(), "ОШИБКА АВТОРИЗАЦИИ", Toast.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
