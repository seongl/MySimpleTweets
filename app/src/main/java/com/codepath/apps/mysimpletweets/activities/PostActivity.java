package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.mysimpletweets.R;

public class PostActivity extends AppCompatActivity {

    EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        etText = (EditText) findViewById(R.id.etText);
    }

    public void onTweet(View view) {
        Intent showOtherActivityIntent = new Intent(this, TimelineActivity.class);
        showOtherActivityIntent.putExtra("newTweet", etText.getText().toString());
        startActivity(showOtherActivityIntent);
    }
}
