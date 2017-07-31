package com.igor.vetrov.photogallery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoGalleryFragment2 extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";
    private RecyclerView mPhotoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();

    public static PhotoGalleryFragment2 newInstance() {
        return new PhotoGalleryFragment2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup conteiner, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, conteiner, false);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        loadPhotoGalleryItems();

        return v;
    }

    private void loadPhotoGalleryItems() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoGalleryFragment2.PhotoAdapter(mItems));
            Call<List<GalleryItem>> call = new FlickrFetchr2().fetchItems();
            call.enqueue(new Callback<List<GalleryItem>>() {
                @Override
                public void onResponse(Call<List<GalleryItem>> call, Response<List<GalleryItem>> response) {
                    mItems = response.body();
                    Log.i(TAG, "Getting response: " + mItems);
//                    mPhotoRecyclerView.setAdapter(new PhotoGalleryFragment2.PhotoAdapter(mItems));
                }

                @Override
                public void onFailure(Call<List<GalleryItem>> call, Throwable t) {
                    Log.e(TAG, "Failed getting photogallery", t);
                }
            });
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }

        public void bindGalleryItem(GalleryItem item) {
            mTitleTextView.setText(item.toString());
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoGalleryFragment2.PhotoHolder> {

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoGalleryFragment2.PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            TextView textView = new TextView(getActivity());
            return new PhotoGalleryFragment2.PhotoHolder(textView);
        }

        @Override
        public void onBindViewHolder(PhotoGalleryFragment2.PhotoHolder photoHolder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);
            photoHolder.bindGalleryItem(galleryItem);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }
}
