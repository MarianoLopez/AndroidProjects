package com.example.marianolopez.innovawifi;

/**
 * Created by MarianoLopez on 11/5/2017.
 */
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by Mariano on 04/03/2017.
 */
public class VolleyCall {
    private RequestQueue queue;
    public VolleyCall(Context context){
        this.queue = Volley.newRequestQueue(context);
    }
    public void call(String url, int method, final Map<String,String> params, Response.Listener onResponse, Response.ErrorListener onError){
        //Request.Method.POST
        StringRequest stringRequest = new StringRequest(method, url, onResponse, onError) {
            @Override
            protected Map<String, String> getParams() {return params;}
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
