package edu.uw.comchat.io;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.collection.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Class used for request queue in lab 3.
 * This is for testing for purpose only, subject to modification later on - Hung Vu.
 */
public class RequestQueueSingleton {
    private static edu.uw.comchat.io.RequestQueueSingleton instance;
    private static Context context;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private RequestQueueSingleton(Context context) {
        edu.uw.comchat.io.RequestQueueSingleton.context = context;
        mRequestQueue = getmRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized edu.uw.comchat.io.RequestQueueSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new edu.uw.comchat.io.RequestQueueSingleton(context);
        }
        return instance;
    }

    public RequestQueue getmRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getmRequestQueue().add(req);
    }

    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }
}
