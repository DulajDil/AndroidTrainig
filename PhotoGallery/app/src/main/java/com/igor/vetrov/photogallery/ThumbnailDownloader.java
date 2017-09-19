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
    private static final int MESSAGE_DOWNLOAD = 0; // Индентификатор сообщений заропосв на загрузку
    private static final int MESSAGE_PRELOAD = 1;
    private static final int CACHE_SIZE = 400;
    private Handler mRequestHandler;  // объект Handler отвечает за постановку в очередь запрососов в фотовом потоке
                                      // а так же за обработку сообщений при извлечении из очереди
    private ConcurrentMap<T,String> mRequestMap = new ConcurrentHashMap<>();
    private Handler mResponseHandler;
    private ThumbnailDownloadListener<T> mThumbnailDownloadListener;
    private LruCache<String, Bitmap> mCache;  // map кеша

    /**
     * создание слушателя на обработку изображения
     * @param <T>
     */
    public interface ThumbnailDownloadListener<T> {
        void onThumbnailDownloaded(T target, Bitmap thumbnail);
    }

    /**
     *
     * @param listener
     */
    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T> listener) {
        mThumbnailDownloadListener = listener;
    }

    /**
     *
     * @param responseHandle
     */
    public ThumbnailDownloader(Handler responseHandle) {
        super(TAG);
        mResponseHandler = responseHandle;
        mCache = new LruCache<>(CACHE_SIZE);
    }

    /**
     * запись сообщений для выполнения
     * what - описывающее сообщение
     */
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

    /**
     *
     * @param url
     * @return
     */
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

    /**
     * загрузка изображения в кеш
     * @param url
     */
    private void cacheLoad(String url) {
        if (url == null) {
            return;
        }
        Bitmap bitmap = getBitmap(url);
        mCache.put(url, bitmap);
    }

    /**
     * осуществление загрузки
     * @param target
     */
    private void handleRequest(T target) {
        final String url = mRequestMap.get(target);
        if (url == null) {  // проверяем существования URL-а
            return;
        }
        if (mCache.get(url) == null) {  // проверяем в кеше значение с ключем по URl-у
            cacheLoad(url);
        }
        final Bitmap bitmap = mCache.get(url);  // извлекаем из кеша изображение

        mResponseHandler.post(new Runnable() {
            public void run () {
                if (mRequestMap.get(target) != url) {
                    return;
                }
                mRequestMap.remove(target);
                mThumbnailDownloadListener.onThumbnailDownloaded(target, bitmap);  // передача загруженного изображения
            }
        });
    }

    /**
     *
     */
    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    /**
     * получение сообщения и отправка приемнику
     * @param target объект с типом T идентификатор загрузки
     * @param url
     */
    public void queueThumnail(T target, String url) {
        Log.i(TAG, "Got a URL: " + url);
        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler
                    .obtainMessage(MESSAGE_DOWNLOAD, target)  // назначаем применика без создания нового объекта Message
                    .sendToTarget();  // отправка сообщения обработчику
        }
    }
}
