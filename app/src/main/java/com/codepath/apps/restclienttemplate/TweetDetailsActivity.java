package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {

    Tweet tweet;
    TextView tvUserName;
    TextView tvTweetBody;
    ImageView ivProfileImage;
    ImageView ivTweetPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        tvTweetBody = findViewById(R.id.tvTweetBody);
        tvUserName = findViewById(R.id.tvUserName);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        ivTweetPic = findViewById(R.id.ivTweetPic);

        // unwrap the movie passed in via intent, using its simple name as a key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvUserName.setText(tweet.user.screenName);
        tvTweetBody.setText(tweet.body);
        Glide.with(this).load(tweet.user.publicImageUrl)
                .transform(new CircleCrop())
                .into(ivProfileImage);
        if(!(tweet.displayUrl.equals(""))) {
            ivTweetPic.setVisibility(View.VISIBLE);
            Glide.with(this).load(tweet.displayUrl)
                    .transform(new RoundedCorners(30))
                    .into(ivTweetPic);
        }
        else{
            ivTweetPic.setVisibility(View.GONE);
        }
    }
}