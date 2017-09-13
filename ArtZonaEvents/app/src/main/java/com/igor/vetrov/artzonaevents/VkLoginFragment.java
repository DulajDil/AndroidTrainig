package com.igor.vetrov.artzonaevents;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

<<<<<<< HEAD
import com.igor.vetrov.artzonaevents.rest_client.ClientRetrofit;
=======
>>>>>>> origin/master
import com.igor.vetrov.artzonaevents.rest_client.RetrofitClient;
import com.igor.vetrov.artzonaevents.rest_client.VkClient;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

<<<<<<< HEAD
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
=======
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vk.sdk.VKUIHelper.getApplicationContext;

>>>>>>> origin/master

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VkLoginFragment extends Fragment {

    private RetrofitClient mService;

    private static final String TAG = "VkLoginFragment";
    private VKAccessToken access_token;
    private RetrofitClient mService;
    private boolean logout = false;
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
<<<<<<< HEAD

        mService = VkClient.getClient();

        Log.d(TAG, Arrays.asList(VKUtil.getCertificateFingerprint(
                getActivity(),
                getActivity().getPackageName())).toString().replace("[", "").replace("]", ""));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup conteiner, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.login_fragment, conteiner, false);
        v.findViewById(R.id.fragmentContainer2);

        if (TokenLab.get(getActivity()).getTokens().size() >= 1) {
            checkToken();
        } else {
            logout = true;
        }

        if (logout) {
            VKSdk.login(this, scope);
        }

        return v;
    }

=======
        mService = VkClient.getClient();
    }

>>>>>>> origin/master
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup conteiner, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.login_fragment, conteiner, false);
        v.findViewById(R.id.fragmentContainer2);

        VKSdk.login(getActivity(), scope);

        Map<String, String> params = new HashMap<>();
        params.put("token", TokenLab.get(getActivity()).getTokens().get(0).getToken());
        Call<ResponseBody> call = mService.checkToken(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
<<<<<<< HEAD
            public void onResult(VKAccessToken res) {
                // успешная авторизация
                Toast.makeText(getActivity().getApplicationContext(), "ВЫ АВТОРИЗОВАЛИСЬ", Toast.LENGTH_SHORT).show();
                access_token = res;
                String accessToken = access_token.accessToken;
                Log.w(TAG, String.format("access_token: %s", accessToken));
                // успешная авторизация
                if (TokenLab.get(getActivity()).getTokens().size() >= 1) {
                    TokenLab.get(getActivity()).updateToken(new Token().setToken(accessToken));
                } else  {
                    TokenLab.get(getActivity()).addToken(new Token().setToken(accessToken));
                }
                Log.w(TAG, "Read token table, count size: " + TokenLab.get(getActivity()).getTokens().size());
=======
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.i(TAG, "URL to request: " + call.request().url());
>>>>>>> origin/master
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Failed getting photogallery", t);
            }
        });
        return v;
    }

    private void checkToken() {
        Map<String, String> params = new HashMap<>();
        params.put("token", TokenLab.get(getActivity()).getTokens().get(0).getToken());
        Call<ResponseBody> call = mService.checkToken(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "URL to request: " + call.request().url());
                try {
                    Log.i(TAG, "Response body: " + response.body().string());
                } catch (IOException e) {
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Failed getting photogallery", t);
            }
        });

    }
}
