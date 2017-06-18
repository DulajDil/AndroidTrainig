package com.igor.vetrov.criminalintent.future;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.igor.vetrov.criminalintent.Crime;
import com.igor.vetrov.criminalintent.CrimeLab;
import com.igor.vetrov.criminalintent.PicturesUtils;
import com.igor.vetrov.criminalintent.R;

import java.io.File;
import java.util.UUID;

public class PhotoMiniaturFragment extends DialogFragment {

    public static final String ARG_PHOTO = "photo";

    private Crime mCrime;
    private ImageView mPhotoView;
    private File mPhotoFile;
    public int width;
    public int height;

    public static PhotoMiniaturFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO, id);

        PhotoMiniaturFragment fragment = new PhotoMiniaturFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        UUID id = (UUID) getArguments().getSerializable(ARG_PHOTO);
        mCrime = CrimeLab.get(getActivity()).getCrime(id);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);
        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo_miniature);
        updatePhotoView();
        return new AlertDialog.Builder(getActivity())
                .setView(v)
//                .setTitle("Миниатюрара аватарки")
                .create();
    }


    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PicturesUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
        ViewTreeObserver viewTreeObserver = mPhotoView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = mPhotoView.getWidth();
                int height = mPhotoView.getHeight();
                Log.i("Getting view size","Height: " + height + " Width: " + width);
                Bitmap bitmap = PicturesUtils.getScaledBitmap(mPhotoFile.getPath(), width, height);
                mPhotoView.setImageBitmap(bitmap);
                mPhotoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}
