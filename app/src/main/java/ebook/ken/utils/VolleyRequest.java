package ebook.ken.utils;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ken on 27/11/2015.
 */
public class VolleyRequest {

    public static final String TAG = MyApp.class.getSimpleName();
    private static VolleyRequest mInstance = null;
    private static RequestQueue mRequestQueue = null;

    private VolleyRequest() {
    }

    /**
     * get instance
     */
    public static synchronized VolleyRequest getInstance() {
        if (mInstance == null) {
            mInstance = new VolleyRequest();
        }
        return mInstance;
    }

    /**
     * build request queue when app is open first
     */
    public void buildRequestQueue(Context context) {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * get request queue
     */
    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    /**
     * add to request queue
     */
    public <T> void addToRequestQueue(Request<T> req, String tag, Context context) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue(context).add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, Context context) {
        req.setTag(TAG);
        getRequestQueue(context).add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
