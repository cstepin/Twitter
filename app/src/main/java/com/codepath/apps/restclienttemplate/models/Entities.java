package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Entities {
    public String displayUrl;

    // For parceler
    public Entities() {}

    public static Entities fromJson(JSONObject jsonObject) throws JSONException {
        Entities entities = new Entities();
        Log.i("in entities", jsonObject.toString());
        JSONArray jsonArray = jsonObject.getJSONArray("media");
        JSONObject media = jsonArray.getJSONObject(0);
       // entities.displayUrl  = media.getString("media_url_https");
       entities.displayUrl = String.format("%s:large", media.getString("media_url_https"));

        return entities;
    }
}
