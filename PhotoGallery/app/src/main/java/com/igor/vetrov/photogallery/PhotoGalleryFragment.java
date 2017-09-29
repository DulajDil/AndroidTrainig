package com.igor.vetrov.photogallery;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import com.igor.vetrov.photogallery.model.GalleryItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryFragment extends Fragment{

    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhotoRecyclerView;
    private PhotoAdapter mAdapter;
    private List<GalleryItem> mItems = new ArrayList<>();
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

    private GridLayoutManager mLayoutManager;

    private int totalItemCount;
    private int lastVisibleItemPosition;
    private int currentPage = 1;
    private boolean loading = true;


    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute(currentPage);

        Handler responseHandler = new Handler(); // обработчик постановки в очередь запросов в паралельном потоке
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
                    @Override
                    public void onThumbnailDownloaded(PhotoHolder photoHolder, Bitmap bitmap) {
                        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
                        photoHolder.bindDrawable(drawable);
                    }
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
        setupAdapter();

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

                        new FetchItemsTask().execute(currentPage);
                    }
                }
            }
        });
        return v;
    }

    /**
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearCache();
        mThumbnailDownloader.clearQueue(); //при разрушении вьюхи зачишаем очередь с сообщениями
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();  // завершение потока
        Log.i(TAG, "Background thread destroyed");
    }

    private void setupAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(mAdapter);
        }
    }

    /**
     * holder
     */
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

    /**
     * adapter
     */
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, viewGroup, false);

            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);

            Bitmap cachedImage = mThumbnailDownloader.getCachedImage(galleryItem.getUrl());

            Drawable placeholder;
            if (cachedImage == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    placeholder = getResources().getDrawable(R.drawable.anime_load_photo, getActivity().getTheme());
                } else {
                    placeholder = getResources().getDrawable(R.drawable.anime_load_photo);
                }
                photoHolder.bindDrawable(placeholder);
                mThumbnailDownloader.queueThumnail(photoHolder, galleryItem.getUrl());
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

        /**
         *
         * @param position текущая позиция фокуса
         */
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

                String url = mGalleryItems.get(i).getUrl();
                mThumbnailDownloader.preloadImage(url);
            }
        }
    }

    private class FetchItemsTask extends AsyncTask<Integer, Void, List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(Integer... page) {
//            try {
//                String result = new FlickrFetchr().getUrlString("https://www.bignerdranch.com");
//                Log.i(TAG, "Fetched contents of URL: " + result);
//            } catch (IOException ioe) {
//                Log.e(TAG, "Failed to fetch URL: ", ioe);
//            }
            Integer integer = page[0];
            Log.i(TAG, "Page " + integer);
            return new FlickrFetchr().fetchItems(integer);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            if (currentPage == 1) {
                mItems = items;
//                setupAdapter();
            } else {
                for (int i = 0; i < items.size(); i++) {
                    mItems.add(items.get(i));
                }
//                mAdapter.updatePhotoGallery(mItems);
            }
            mAdapter.updatePhotoGallery(mItems);
            Log.i(TAG, String.format("Load %s page", currentPage));
            loading = true;
        }
    }



    /**
     * назначаем слушателя
     * для проверки шарины текущего положения экрана
     * для установки количества ячеек
     */
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
