package com.codepath.apps.mysimpletweets.models;

import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by seonglee on 3/25/17.
 *
 *
 * [
 *  {
 *      "text": "...", "retweeeted": false,
 *
 *
 *  }
 * ]
 *
 *
 */

// Parse the JSON + Store the data, encapsulate state logic or display logic
public class Tweet implements Serializable {
    // list out the attributes
    private String body;
    private long uid; // unique id for the tweet
    private User user;
    private String createdAt;

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // Deserialize the JSON and build Tweet objects
    // Tweet.fromJSON( "{...}" ) ==> <Tweet>
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        // Extract the values from the json, store them
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // REturn the tweet object

        return tweet;
    }

    // Tweet.fromJSONArray()
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for( int i=0; i < jsonArray.length(); ++i) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                System.out.println("Tweet=" + tweet.getUid() + " " + tweet.getBody());
                if( tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }


        }

        return tweets;


    }






}
