package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.clients.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.R.id.list;

/**
 * Created by seonglee on 3/30/17.
 */

public class HomeTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    long maxId = Long.MAX_VALUE;

    Bundle b;

    public void setBundle(Bundle bundle) {
        b = bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();

        populateTimeline();

        if(b != null) {
            String newTweetString = b.getString("newTweet");
            if(newTweetString != null) {
                client.post(newTweetString, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Tweet tweet = Tweet.fromJSON(response);
                        add(tweet);
//                    toggle = false;
                        notifyChanges();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("DEBUG", errorResponse.toString());
                    }
                });
            }
        }
    }

    public void onProfileView(MenuItem mi) {
        System.out.println("DDD");
        // Launch the profile view


    }

    @Override
    protected void populateTimeline(int type) {
        System.out.println("HomeTimeline populateTimeline arg");
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());

                ArrayList<Tweet> list = Tweet.fromJSONArray(json);
                addAll(list);

                long nextMax = Long.MAX_VALUE;
                for(int i=0; i < list.size(); ++i) {
                    Tweet t = (Tweet)list.get(i);
                    nextMax = t.getUid() < nextMax ? t.getUid() : nextMax;
                }
                maxId = nextMax < maxId ? nextMax : maxId;
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    private void populateTimeline() {
        System.out.println("HomeTimeline populateTimeline no");
        if(maxId != Long.MAX_VALUE) {
            client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
                // SUCCESS
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("DEBUG", json.toString());

                    ArrayList<Tweet> list = Tweet.fromJSONArray(json);
                    addAll(list);

                    long nextMax = Long.MAX_VALUE;
                    for(int i=0; i < list.size(); ++i) {
                        Tweet t = (Tweet)list.get(i);
                        nextMax = t.getUid() < nextMax ? t.getUid() : nextMax;
                    }
                    maxId = nextMax < maxId ? nextMax : maxId;
                }

                // FAILURE
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            });

        } else {
            client.getHomeTimeline(new JsonHttpResponseHandler() {
                // SUCCESS
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("DEBUG", json.toString());

                    ArrayList<Tweet> list = Tweet.fromJSONArray(json);
                    addAll(list);

                    long nextMax = Long.MAX_VALUE;
                    for(int i=0; i < list.size(); ++i) {
                        Tweet t = (Tweet)list.get(i);
                        nextMax = t.getUid() < nextMax ? t.getUid() : nextMax;
                    }
                    maxId = nextMax < maxId ? nextMax : maxId;
                }

                // FAILURE
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            });

        }


    }




}
