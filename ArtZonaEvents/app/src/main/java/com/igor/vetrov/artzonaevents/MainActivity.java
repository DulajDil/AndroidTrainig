package com.igor.vetrov.artzonaevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private VKAccessToken access_token;
    public static String[] scope = new String[] {
            VKScope.GROUPS, VKScope.MESSAGES, VKScope.FRIENDS
    };

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        VKSdk.login(this, scope);
//
//        TokenLab.get(this).getToken();
//
//        String token = null;
//        try {
//            token = TokenLab.get(this).getToken().getToken();
//        } catch (RuntimeException e) {
//
//        }

//        Log.w(TAG, "Token: " + token);

//        TokenLab.get(this).deleteToken();

//        FragmentManager fm = getSupportFragmentManager();
//        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
//        if (fragment == null) {
//            fragment = VkLoginFragment.newInstance();
//            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                access_token = res;
                String accessToken = access_token.accessToken;
                Log.i(TAG, String.format("access_token: %s", accessToken));
                // успешная авторизация
                TokenLab.get(getApplicationContext()).addToken(new Token().setToken(accessToken));
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
