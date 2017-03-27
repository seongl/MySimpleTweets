package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.codepath.apps.mysimpletweets.R.id.ivProfileImage;
import static com.codepath.apps.mysimpletweets.R.id.tvBody;
import static com.codepath.apps.mysimpletweets.R.id.tvName;
import static com.codepath.apps.mysimpletweets.R.id.tvUserName;

/**
 * Created by seonglee on 3/25/17.
 */

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        ViewHolderTweet viewHolder;

        if( convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);

            viewHolder = new ViewHolderTweet();
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(ivProfileImage);
            viewHolder.tvUserName = (TextView) convertView.findViewById(tvUserName);
            viewHolder.tvBody = (TextView) convertView.findViewById(tvBody);
            viewHolder.tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
            viewHolder.tvName = (TextView) convertView.findViewById(tvName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderTweet) convertView.getTag();
        }

        viewHolder.tvUserName.setText(tweet.getUser().getScreenName() + "/");

        viewHolder.tvBody.setText(tweet.getBody());

        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);

        SimpleDateFormat parseFormat =
                new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        Date date = null;
        try {
            date = parseFormat.parse(tweet.getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date currentDate = new Date();

        long diff = currentDate.getTime() - date.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        int diffInDays = (int) ((currentDate.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));

        StringBuilder sb = new StringBuilder();
        if(diffInDays > 0) {
            sb.append(diffInDays).append(" d ");
        }
        if(diffHours > 0) {
            sb.append(diffHours).append(" h ");
        }
        if(diffMinutes > 0) {
            sb.append(diffMinutes).append(" m ");
        }
        viewHolder.tvRelativeTime.setText(sb.toString());

        viewHolder.tvName.setText(tweet.getUser().getName());

        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.ivProfileImage);

        return convertView;
    }
    static class ViewHolderTweet {
        ImageView ivProfileImage;
        TextView tvUserName;
        TextView tvBody;
        TextView tvRelativeTime;
        TextView tvName;
    }

}
