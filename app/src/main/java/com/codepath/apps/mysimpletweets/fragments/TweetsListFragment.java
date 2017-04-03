package com.codepath.apps.mysimpletweets.fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.listeners.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.toggle;
import static com.codepath.apps.mysimpletweets.R.id.ivProfileImage;
import static com.codepath.apps.mysimpletweets.R.id.lvTweets;

/**
 * Created by seonglee on 3/30/17.
 */

public class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    boolean toggle = true;

    ImageView ivProfileImage;

    protected void populateTimeline(int type) {
    }

    // influation logic
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
                    System.out.println(toggle);
                    populateTimeline(2);
                } else {
                    System.out.println("here");
                    toggle = true;
                }
                return true;
            }
        });

        ivProfileImage = (ImageView) lvTweets.findViewById(R.id.ivProfileImage);

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showUserProfileView(position);
            }
        });

        return v;
    }

    private void showUserProfileView(int position) {
        Intent detailViewIntent = new Intent(getContext(), ProfileActivity.class);
        Tweet t = (Tweet) lvTweets.getItemAtPosition(position);

//        System.out.println(t.getUser().getName());
//        detailViewIntent.putExtra("book", (Serializable) );
        detailViewIntent.putExtra("screen_name", t.getUser().getScreenName());
        detailViewIntent.putExtra("fromList", "YES");

        startActivity(detailViewIntent);
    }

    // creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void add(Tweet tweet) {
        aTweets.insert(tweet, 0);
    }

    public void notifyChanges() {
        System.out.println("RRR");
        aTweets.notifyDataSetChanged();
    }

//    public TweetsArrayAdapter getAdapter() {
//
//    }
}
