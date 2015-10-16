package com.andela.www.currencycalculator.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kamiye on 10/16/15.
 */
public class CurrencyConverter {

    public static final String CONVERSION_URL = "https://openexchangerates.org/api/latest.json?app_id=6b8d176d6e1741af8a8028ed6c17d51d";
    private JSONObject rates;
    private String baseCurrency;
    private String currency;
    private RequestQueue requestQueue;
    private String conversion;

    public CurrencyConverter(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void convertCurrency(String baseCurrency, final String convertedCurrency, final ConfirmationCallback callback) {
        // check payment with paypal rest api
        JsonObjectRequest conversionRequest = new JsonObjectRequest(Request.Method.GET,
                CONVERSION_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    rates = response.getJSONObject("rates");
                    currency = rates.get(convertedCurrency).toString();
                    Log.i("Currency", currency);
                    callback.onSuccess(currency);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error request", error.toString());
                // set error message to the user
                callback.onFailure();
                requestQueue.stop();
            }
        });

        requestQueue.add(conversionRequest);
    }

    public interface ConfirmationCallback {
        void onSuccess(String currency);

        void onFailure();
    }
}