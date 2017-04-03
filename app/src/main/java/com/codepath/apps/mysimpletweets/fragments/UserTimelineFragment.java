package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.clients.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by seonglee on 4/2/17.
 */

public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    long maxId = Long.MAX_VALUE;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();

        populateTimeline();

    }

    public static UserTimelineFragment newInstance(String screen_name) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);

        userFragment.setArguments(args);
        return userFragment;
    }

    @Override
    protected void populateTimeline(int type) {
        String screenName = getArguments().getString("screen_name");

        client.getUserTimeline(maxId, screenName, new JsonHttpResponseHandler() {
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
        String screenName = getArguments().getString("screen_name");

        if(maxId != Long.MAX_VALUE) {
            client.getUserTimeline(maxId, screenName, new JsonHttpResponseHandler() {
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
            client.getUserTimeline(screenName, new JsonHttpResponseHandler() {
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
