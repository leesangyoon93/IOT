package com.example.leesangyoon.iot;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class volley extends Application {

    public static final String TAG=Volley.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private static volley mInstance;

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance=this;
    }

    public static synchronized volley getInstance(){
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public<T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}