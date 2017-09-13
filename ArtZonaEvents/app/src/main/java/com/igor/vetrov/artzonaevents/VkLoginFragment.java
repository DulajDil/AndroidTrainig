package com.igor.vetrov.artzonaevents;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;


public class VkLoginFragment extends Fragment{

    private static final String TAG = "VkLoginFragment";
    private VKAccessToken access_token;
    public static String[] scope = new String[] {
            VKScope.GROUPS, VKScope.MESSAGES, VKScope.FRIENDS
    };

    public static VkLoginFragment newInstance() {
        VkLoginFragment fragment = new VkLoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        VKSdk.login(getActivity(), scope);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup conteiner, Bundle savedInstance) {
//        View v = inflater.inflate(R.layout.login_fragment, conteiner, false);
//        v.findViewById(R.id.fragmentContainer2);
//        return v;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getActivity().getApplicationContext(), "БЕЗ ПРОВЕРКИ АВТОРИЗАЦИИ", Toast.LENGTH_SHORT).show();
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                access_token = res;
                Log.i(TAG, String.format("access_token: %s", access_token.toString()));
                // успешная авторизация
                Toast.makeText(getActivity().getApplicationContext(), "ВЫ АВТОРИЗОВАЛИСЬ", Toast.LENGTH_SHORT).show();
                TokenLab.get(getActivity()).addToken(new Token().setToken(access_token.toString()));
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
