package io.ditho.assignment.rest;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

public abstract class BaseApi {

    private RequestQueue requestQueue;

    BaseApi(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(getClass().getName());
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {

        req.setTag(TextUtils.isEmpty(tag) ? getClass().getName() : tag);
        getRequestQueue().add(req);
    }

    public void cancelAll(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

    public void cancelAll() {
        if (requestQueue != null) {
            requestQueue.cancelAll(getClass().getName());
        }
    }

    public void clearCache() {

        if (requestQueue != null) {
            requestQueue.getCache().clear();
        }
    }
}
