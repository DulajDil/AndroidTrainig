package com.igor.vetrov.photogallery;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.igor.vetrov.photogallery.model.GalleryItem;
import com.igor.vetrov.photogallery.model.Photos;
import com.igor.vetrov.photogallery.model.ResponsePhotogallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoGalleryFragment2 extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhotoRecyclerView;
    private PhotoAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private List<GalleryItem> mItems = new ArrayList<>();
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

    private int totalItemCount;
    private int lastVisibleItemPosition;
    private int currentPage = 1;
    private boolean loading = true;
    private PhotoGalleryClient mService;

    public static PhotoGalleryFragment2 newInstance() {
        return new PhotoGalleryFragment2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mService = FlickrFetchr2.fetchItems();

        mThumbnailDownloader = new ThumbnailDownloader<>();
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup conteiner, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, conteiner, false);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        updateView();
        mPhotoRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PhotoGalleryFragment2.PhotoAdapter(mItems);
        mPhotoRecyclerView.setAdapter(mAdapter);

        loadPhotoGalleryItems();

        mPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = mLayoutManager.getItemCount();
                lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();

                if (loading) {
                    if (lastVisibleItemPosition ==  totalItemCount - 1) {
                        loading = false;
                        currentPage++;
                        Log.i(TAG, "Total item count " + totalItemCount);
                        Log.i(TAG, "Last visible item count position " + lastVisibleItemPosition);

                        loadPhotoGalleryItems();
                    }
                }
            }
        });
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    private void loadPhotoGalleryItems() {
        Map<String, String> params = new HashMap<>();
        params.put("method", "flickr.photos.getRecent");
        params.put("api_key", FlickrFetchr2.API_KEY);
        params.put("format", "json");
        params.put("nojsoncallback", "1");
        params.put("extras", "url_s");
        params.put("page", String.valueOf(currentPage));

        Call<ResponsePhotogallery> call = mService.fetchItems(params, String.valueOf(currentPage));
        call.enqueue(new Callback<ResponsePhotogallery>() {
            @Override
            public void onResponse(Call<ResponsePhotogallery> call, Response<ResponsePhotogallery> response) {

                Log.i(TAG, "URL to request: " + call.request().url());

                ResponsePhotogallery body = response.body();
                Log.i(TAG, "Received response object: " + body);

                Photos photos = body.getPhotos();
                Log.i(TAG, "Received object photos: " + photos);

                if (currentPage > 1) {
                    List<GalleryItem> items = photos.getPhoto();
                    for (int i = 0; i < items.size(); i++) {
                        mItems.add(items.get(i));
                    }
                } else {
                    mItems = photos.getPhoto();
                }
                Log.i(TAG, "Received gallery items list objects: " + mItems);

                Log.i(TAG, "Getting response: " + mItems);
                mAdapter.updatePhotoGallery(mItems);

                loading = true;
                Log.i(TAG, String.format("Load %s page", currentPage));
            }

            @Override
            public void onFailure(Call<ResponsePhotogallery> call, Throwable t) {
                Log.e(TAG, "Failed getting photogallery", t);
            }
        });
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView mItemImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_gallery_image_view);
        }

        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoGalleryFragment2.PhotoHolder> {

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoGalleryFragment2.PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, viewGroup, false);

            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoGalleryFragment2.PhotoHolder photoHolder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);
            Drawable placeholder = getResources().getDrawable(R.drawable.anime_load_photo);
            photoHolder.bindDrawable(placeholder);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }

        public void updatePhotoGallery(List<GalleryItem> items) {
            mGalleryItems = items;
            notifyDataSetChanged();
        }
    }

    private void updateView() {
        ViewTreeObserver vto = mPhotoRecyclerView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout() {

                int width = mLayoutManager.getWidth();
                Log.i(TAG, String.format("width size: %s", width));
                if (width > 1080) {
                    mLayoutManager.setSpanCount(4);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mPhotoRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mPhotoRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }
}
