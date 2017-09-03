package com.igor.vetrov.artzonaevents;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class VkLoginFragment extends Fragment{

    private static final String TAG = "VkLoginFragment";
    private VKAccessToken access_token;

    public static VkLoginFragment newInstance() {
        return new VkLoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        VKSdk.login(getActivity(), MainActivity.scope);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                access_token = res;
                Log.i(TAG, String.format("access_token: %s", access_token.toString()));
                // успешная авторизация
                Toast.makeText(getActivity().getApplicationContext(), "ВЫ АВТОРИЗОВАЛИСЬ", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(VKError error) {
                // ошибка авторизации
                Toast.makeText(getActivity().getApplicationContext(), "ОШИБКА АВТОРИЗАЦИИ", Toast.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
