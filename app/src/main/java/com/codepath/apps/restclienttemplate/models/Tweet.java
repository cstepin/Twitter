package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public String displayUrl;
    public Entities entities;
    public Long id;

    // Needed by Parceler Library
    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        if(jsonObject.has("full_text")) {
            tweet.body = jsonObject.getString("full_text");
        } else {
            tweet.body = jsonObject.getString("text");
        }
       // tweet.body = jsonObject.getString("text");
        if(jsonObject.has("extended_entities")){
            Log.i("entities", "found entities " + jsonObject);

            JSONObject jsonObject1 = jsonObject.getJSONObject("extended_entities");
            Log.i("entities", "2 " + jsonObject1);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("media");
            Log.i("entities", "3 " + jsonArray1);
            JSONObject media = jsonArray1.getJSONObject(0);
            tweet.displayUrl = String.format("%s:large", media.getString("media_url_https"));

         //   tweet.entities = Entities.fromJson(jsonObject.getJSONObject("entities"));
        }
        else{
            tweet.displayUrl = "";
        }

        tweet.createdAt = jsonObject.getString("created_at");

        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        tweet.id = jsonObject.getLong("id");
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException{
        List<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            Tweet newTweet = fromJson(jsonArray.getJSONObject(i));
            Log.i("tweetId", "tweetID is: " + newTweet.id);
            tweets.add(newTweet);
        }
        return tweets;
    }
}
