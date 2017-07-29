package io.ditho.assignment.model.rest;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApiProvider {

    private static ApiProvider instance;

    private RequestQueue requestQueue;
    private ContactApi contactApi;

    private ApiProvider(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
        contactApi = new ContactApi(requestQueue);
    }

    public static synchronized ApiProvider getInstance(Context context) {

        if (instance == null) {
            instance = new ApiProvider(context);
        }

        return instance;
    }

    public ContactApi getContactApi() {
        return  contactApi;
    }
}
