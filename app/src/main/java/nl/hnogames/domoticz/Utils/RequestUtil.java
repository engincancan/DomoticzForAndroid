/*
 * Copyright (C) 2015 Domoticz
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package nl.hnogames.domoticz.Utils;

import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import nl.hnogames.domoticz.Domoticz.Domoticz;
import nl.hnogames.domoticz.Interfaces.JSONParserInterface;
import nl.hnogames.domoticz.app.AppController;

public class RequestUtil {

    private static final String TAG = RequestUtil.class.getSimpleName();

    public static void makeJsonVersionRequest(@Nullable final JSONParserInterface parser,
                                              final String username,
                                              final String password,
                                              String url) {

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.GET,
                        url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String jsonString;

                        try {
                            jsonString = response.getString(Domoticz.Json.Field.VERSION);
                            if (parser != null)
                                parser.parseResult(jsonString);
                        } catch (JSONException e) {
                            if (parser != null)
                                parser.onError(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        errorHandling(volleyError);
                        if (parser != null) parser.onError(volleyError);
                    }
                }) {

                    @Override
                    // HTTP basic authentication
                    // Taken from: http://blog.lemberg.co.uk/volley-part-1-quickstart
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return createBasicAuthHeader(username, password);
                    }

                };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public static void makeJsonGetRequest(@Nullable final JSONParserInterface parser,
                                          final String username,
                                          final String password,
                                          String url) {

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.GET,
                        url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (parser != null)
                            parser.parseResult(response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        errorHandling(volleyError);
                        if (parser != null) parser.onError(volleyError);
                    }
                }) {

                    @Override
                    // HTTP basic authentication
                    // Taken from: http://blog.lemberg.co.uk/volley-part-1-quickstart
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return createBasicAuthHeader(username, password);
                    }

                };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    /**
     * Method to get json object request where json response starts with {
     */
    public static void makeJsonGetResultRequest(@Nullable final JSONParserInterface parser,
                                                final String username,
                                                final String password,
                                                String url) {

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.GET,
                        url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String jsonString;

                        try {
                            jsonString = response.getString(Domoticz.Json.Field.RESULT);
                            if (parser != null)
                                parser.parseResult(jsonString);
                        } catch (JSONException e) {
                            if (parser != null)
                                parser.onError(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        errorHandling(volleyError);
                        if (parser != null)
                            parser.onError(volleyError);
                    }
                }) {

                    @Override
                    // HTTP basic authentication
                    // Taken from: http://blog.lemberg.co.uk/volley-part-1-quickstart
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return createBasicAuthHeader(username, password);
                    }

                };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    /**
     * Method to put a JSON object to a url
     */
    public static void makeJsonPutRequest(@Nullable final JSONParserInterface parser,
                                          final String username,
                                          final String password,
                                          String url) {

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.PUT, url,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                String jsonString;

                                try {
                                    jsonString = response.getString(Domoticz.Json.Field.STATUS);
                                    if (parser != null)
                                        parser.parseResult(jsonString);
                                } catch (JSONException e) {
                                    if (parser != null)
                                        parser.onError(e);
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        errorHandling(volleyError);
                        if (parser != null)
                            parser.onError(volleyError);
                    }
                }) {

                    @Override
                    // HTTP basic authentication
                    // Taken from: http://blog.lemberg.co.uk/volley-part-1-quickstart
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return createBasicAuthHeader(username, password);
                    }

                };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    /**
     * Method to create a basic HTTP base64 encrypted authentication header
     *
     * @param username Username
     * @param password Password
     * @return Base64 encrypted header map
     */
    static Map<String, String> createBasicAuthHeader(String username, String password) {

        Map<String, String> headerMap = new HashMap<>();

        String credentials = username + ":" + password;
        String base64EncodedCredentials =
                Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headerMap.put("Authorization", "Basic " + base64EncodedCredentials);

        return headerMap;
    }

    /**
     * Local error handling
     *
     * @param volleyError Volley error holding the error
     */
    private static void errorHandling(VolleyError volleyError) {
        Log.e(TAG, "RequestUtil volley error");
        if (volleyError.getMessage() != null) Log.e(TAG, volleyError.getMessage());
    }
}