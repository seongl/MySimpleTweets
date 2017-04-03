package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.clients.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.listeners.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.x;
import static com.codepath.apps.mysimpletweets.R.id.etText;
import static com.codepath.apps.mysimpletweets.R.id.lvTweets;

public class TimelineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the viewpager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);

        // Set the viewpager adapter for the pager
        TweetsPagerAdapter tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        Bundle bundle = getIntent().getExtras();
        tweetsPagerAdapter.setBundle(bundle);
        vpPager.setAdapter(tweetsPagerAdapter);








        // Find the sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        // Attach the tabstrip to the viewpager
        tabStrip.setViewPager(vpPager);



    }

    public void goToPostActivity(MenuItem mi) {
        Intent showOtherActivityIntent = new Intent(this, PostActivity.class);
        startActivity(showOtherActivityIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onProfileView(MenuItem mi) {
        System.out.println("BBB");
        // Launch the profile view
        Intent i = new Intent(this, ProfileActivity.class);

        startActivity(i);


    }
//    showOtherActivityIntent.putExtra("newTweet", etText.getText().toString());


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // Return the order of the fragments in the view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        Bundle b;
        final int PAGE_COUNT = 2;
        private String tabTitles[] = { "Home", "Mentions" };

        // Adapter gets the manager insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setBundle(Bundle bundle) {
            b = bundle;
        }


        // the order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if(position == 0) {

                HomeTimelineFragment fragment = new HomeTimelineFragment();
                fragment.setBundle(b);
                return fragment;

            } else if(position == 1) {

                MentionsTimelineFragment fragment = new MentionsTimelineFragment();
                fragment.setBundle(b);
                return fragment;

            } else {
                return null;
            }
        }

        // Return the tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        // How many fragments there are to swipe between?
        @Override
        public int getCount() {
            return tabTitles.length;
        }

    }
}
