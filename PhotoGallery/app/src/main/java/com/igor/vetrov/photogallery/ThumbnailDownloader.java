package com.igor.vetrov.photogallery;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class ThumbnailDownloader<T> extends HandlerThread {

    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;
    private static final int MESSAGE_PRELOAD = 1;
    private static final int CACHE_SIZE = 400;
    private Handler mRequestHandler;
    private ConcurrentMap<T,String> mRequestMap = new ConcurrentHashMap<>();
    private Handler mResponseHandler;
    private ThumbnailDownloadListener<T> mThumbnailDownloadListener;
    private LruCache<String, Bitmap> mCache;

    public interface ThumbnailDownloadListener<T> {
        void onThumbnailDownloaded(T target, Bitmap thumbnail);
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T> listener) {
        mThumbnailDownloadListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandle) {
        super(TAG);
        mResponseHandler = responseHandle;
        mCache = new LruCache<>(CACHE_SIZE);
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    Log.i(TAG, "Got a request for URL: " +
                    mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }

    private Bitmap getBitmap(String url) {
        try {
            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created");
            return bitmap;
        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
        return null;
    }

    private void cacheLoad(String url) {
        if (url == null) {
            return;
        }
        Bitmap bitmap = getBitmap(url);
        mCache.put(url, bitmap);
    }

    private void handleRequest(T target) {
        final String url = mRequestMap.get(target);
        if (url == null) {
            return;
        }
        if (mCache.get(url) == null) {
            cacheLoad(url);
        }
        final Bitmap bitmap = mCache.get(url);

        mResponseHandler.post(new Runnable() {
            public void run () {
                if (mRequestMap.get(target) != url) {
                    return;
                }
                mRequestMap.remove(target);
                mThumbnailDownloadListener.onThumbnailDownloaded(target, bitmap);
            }
        });
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    public void queueThumnail(T target, String url) {
        Log.i(TAG, "Got a URL: " + url);
        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler
                    .obtainMessage(MESSAGE_DOWNLOAD, target)
                    .sendToTarget();
        }
    }
}
