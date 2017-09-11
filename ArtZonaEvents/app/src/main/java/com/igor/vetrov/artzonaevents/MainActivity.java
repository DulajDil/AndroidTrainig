package com.igor.vetrov.artzonaevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.igor.vetrov.artzonaevents.rest_client.RetrofitClient;
import com.igor.vetrov.artzonaevents.rest_client.VkClient;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RetrofitClient mService;
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

        mService = VkClient.getClient();

        Map<String, String> params = new HashMap<>();
        params.put("token", TokenLab.get(getApplicationContext()).getTokens().get(0).getToken());
        Call<ResponseBody> call = mService.checkToken(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.i(TAG, "URL to request: " + call.request().url());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Failed getting photogallery", t);
            }
        });

//        VKSdk.login(this, scope);
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

//        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName()); // получение отпечатка через sdk

//        VKSdk.login(this, scope);

//        TokenLab.get(this).deleteTokens();

//        String token = TokenLab.get(this).getToken().getToken();
//        Log.w(TAG, "Token: " + token);

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
                Log.w(TAG, String.format("access_token: %s", accessToken));
                // успешная авторизация
                if (TokenLab.get(getApplicationContext()).getTokens().size() >= 1) {
                    TokenLab.get(getApplicationContext()).updateToken(new Token().setToken(accessToken));
                } else  {
                    TokenLab.get(getApplicationContext()).addToken(new Token().setToken(accessToken));
                }
                Log.w(TAG, "Read token table, count size: " + TokenLab.get(getApplicationContext()).getTokens().size());
            }
            @Override
            public void onError(VKError error) {
                // ошибка авторизации
                Toast.makeText(getApplicationContext(), "AUTHORIZATION ERROR", Toast.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
