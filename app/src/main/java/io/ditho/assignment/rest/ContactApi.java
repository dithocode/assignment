package io.ditho.assignment.rest;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.ditho.assignment.rest.model.ContactResponse;

public class ContactApi extends BaseApi {

    public static final String GET_CONTACT = "https://firebasestorage.googleapis.com/v0/b/contacts-8d05b.appspot.com/o/contacts.json?alt=media&token=431c2754-b3f9-4485-8c5d-0365d5f8f0e5";

    ContactApi(RequestQueue requestQueue) {
        super(requestQueue);
    }

    public void getContactList(
            Response.Listener<ContactResponse> successListener,
            Response.ErrorListener errorListener) {
        getContactList("", successListener, errorListener);
    }

    public void getContactList(
            String requestId,
            Response.Listener<ContactResponse> successListener,
            Response.ErrorListener errorListener) {

        JsonRequest<ContactResponse> request = new JsonRequest<ContactResponse>(
                Request.Method.GET, GET_CONTACT, "", successListener, errorListener) {

            @Override
            protected Response<ContactResponse> parseNetworkResponse(NetworkResponse response) {
                ObjectMapper objectMapper = new ObjectMapper();
                ContactResponse contactResponse = null;

                try {
                    contactResponse = objectMapper.readValue(response.data, ContactResponse.class);
                } catch (Exception ex) {
                    Log.e(getClass().getName(), ex.getMessage(), ex);
                }

                return Response.success(contactResponse, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        addToRequestQueue(request, requestId);
    }
}
