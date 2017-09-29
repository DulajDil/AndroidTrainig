package com.igor.vetrov.photogallery.http_client;

import android.support.v4.app.Fragment;

import com.igor.vetrov.photogallery.SingleFragmentActivity;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
