package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.clients.TwitterClient;
import com.codepath.apps.mysimpletweets.listeners.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    long maxId = Long.MAX_VALUE;
    boolean toggle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.create:
                        goToPostActivity();
                        break;
                }
                return false;
            }
        };

        toolbar.setOnMenuItemClickListener(listener);

        lvTweets = (ListView) findViewById(R.id.lvTweets);

        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);

        lvTweets.setAdapter(aTweets);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if(toggle) {
                    populateTimeline(2);
                } else {
                    toggle = true;
                }
                return true;
            }
        });

        client = TwitterApplication.getRestClient();


        //first
        populateTimeline();

        String newTweetString = getIntent().getStringExtra("newTweet");
        if(newTweetString != null) {
            client.post(newTweetString, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Tweet tweet = Tweet.fromJSON(response);
                    tweets.add(0, tweet);
                    toggle = false;
                    aTweets.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            });
        }
    }

    private void goToPostActivity() {
        Intent showOtherActivityIntent = new Intent(this, PostActivity.class);
        startActivity(showOtherActivityIntent);
    }


    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());

                ArrayList<Tweet> list = Tweet.fromJSONArray(json);
                aTweets.addAll(list);

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


    // Send an API request to get the timeline json
    // Fill the listview by createing the tweet objects from the json
    private void populateTimeline(int typed) {

        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());
                ArrayList<Tweet> list = Tweet.fromJSONArray(json);
                aTweets.addAll(list);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
