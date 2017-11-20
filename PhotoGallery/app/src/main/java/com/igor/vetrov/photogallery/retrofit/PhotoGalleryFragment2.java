package com.igor.vetrov.photogallery.retrofit;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import com.igor.vetrov.photogallery.R;
import com.igor.vetrov.photogallery.model.GalleryItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryFragment2 extends Fragment {

    private static final String TAG = "PhotoGalleryFragment2";

    private RecyclerView mPhotoRecyclerView;
    private PhotoAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private List<GalleryItem> mItems = new ArrayList<>();
    private ThumbnailDownloader2<PhotoHolder> mThumbnailDownloader;

    private int totalItemCount;
    private int lastVisibleItemPosition;
    private int currentPage = 1;
    public final static CheckLoading loading = CheckLoading.get();

    public static PhotoGalleryFragment2 newInstance() {
        return new PhotoGalleryFragment2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        FlickrFetchr2.initClient();

        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader2<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                (photoHolder, bitmap) -> {
                    BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
                    photoHolder.bindDrawable(drawable);
                }
        );
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
        mAdapter = new PhotoAdapter(mItems);
        mPhotoRecyclerView.setAdapter(mAdapter);

        FlickrFetchr2.sItems = mItems;
        FlickrFetchr2.loadPhotos(mAdapter, currentPage);

        mPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = mLayoutManager.getItemCount();
                lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();

                if (loading.getRunning()) {
                    if (lastVisibleItemPosition ==  totalItemCount - 1) {
                        loading.setRunning(false);
                        currentPage++;
                        Log.i(TAG, "Total item count " + totalItemCount);
                        Log.i(TAG, "Last visible item count position " + lastVisibleItemPosition);

                        FlickrFetchr2.sItems = mItems;
                        FlickrFetchr2.loadPhotos(mAdapter, currentPage);
                    }
                }
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearCache();
        mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
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

    public class PhotoAdapter extends RecyclerView.Adapter<PhotoGalleryFragment2.PhotoHolder> {

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

            Bitmap cachedImage = mThumbnailDownloader.getCachedImage(galleryItem.getMUrl());

            Drawable placeholder;
            if (cachedImage == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    placeholder = getResources().getDrawable(R.drawable.anime_load_photo, getActivity().getTheme());
                } else {
                    placeholder = getResources().getDrawable(R.drawable.anime_load_photo);
                }
                photoHolder.bindDrawable(placeholder);
                mThumbnailDownloader.queueThumnail(photoHolder, galleryItem.getMUrl());
            } else {
                Log.w(TAG, "Loaded image from cache");
                photoHolder.bindDrawable(new BitmapDrawable(getResources(), cachedImage));
            }
            preloadImages(position);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }

        public void updatePhotoGallery(List<GalleryItem> items) {
            mGalleryItems = items;
            notifyDataSetChanged();
        }

        private void preloadImages(int position) {
            final int imageBufferSize = 10; // колличество изображений загружаемых в кеш до и после текущей позиции

            //Set the Indexes for the images to preload
            int startIndex = Math.max(position - imageBufferSize, 0); // стартовый индекс должен быть >= 0
            int endIndex = Math.min(position + imageBufferSize, mGalleryItems.size() - 1); //Ending index must be <= number of galleryItems - 1

            //Loop over mGallery items using our index bounds
            for (int i = startIndex; i <= endIndex; i++) {
                //We don't need to preload the "current" item, as it is being
                //displayed already.
                if (i == position) continue;

                String url = mGalleryItems.get(i).getMUrl();
                mThumbnailDownloader.preloadImage(url);
            }
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
                    int count = new BigDecimal((double) width / 360).setScale(0, RoundingMode.UP).intValue();
                    mLayoutManager.setSpanCount(count);
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
