package com.igor.vetrov.artzonaevents;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.igor.vetrov.artzonaevents.rest_client.RetrofitClient;
import com.igor.vetrov.artzonaevents.rest_client.VkClient;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vk.sdk.VKUIHelper.getApplicationContext;


public class VkLoginFragment extends Fragment{

    private RetrofitClient mService;

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
        mService = VkClient.getClient();
    }

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
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.i(TAG, "URL to request: " + call.request().url());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Failed getting photogallery", t);
            }
        });
        return v;
    }
}
