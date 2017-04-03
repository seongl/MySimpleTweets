package com.codepath.apps.mysimpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.listeners.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import static com.codepath.apps.mysimpletweets.R.id.tvUserName;

/**
 * Created by seonglee on 3/30/17.
 */

public class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    boolean toggle = true;

    protected void populateTimeline(int type) {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);

        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
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

        return v;
    }


    // creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);

        aTweets.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView username = (TextView) v.findViewById(tvUserName);
                String s = username.getText().toString();
                String s2 = s.substring(0, s.length()-1);
                showUserProfileView(s2);
            }
        });
    }

    private void showUserProfileView(String screenName) {
        Intent detailViewIntent = new Intent(getContext(), ProfileActivity.class);
        detailViewIntent.putExtra("screen_name", screenName);
        detailViewIntent.putExtra("fromList", "YES");

        startActivity(detailViewIntent);
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void add(Tweet tweet) {
        aTweets.insert(tweet, 0);
    }

    public void notifyChanges() {
        aTweets.notifyDataSetChanged();
    }
}
