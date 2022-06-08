package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    //Pass in context and the list of tweets

    Context context;
    List<Tweet> tweets;

    //For each row, need to inflate the layout

    public TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    //Then we need to bind the values, based on the pos of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }


    //Define a viewholder

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivTweetPic;
        TextView tvTimeAgo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            ivTweetPic = itemView.findViewById(R.id.ivTweetPic);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            Glide.with(context).load(tweet.user.publicImageUrl).into(ivProfileImage);
            if(!(tweet.displayUrl.equals(""))) {
                ivTweetPic.setVisibility(View.VISIBLE);
                Log.i("got into entities!", "inside");
                Glide.with(context).load(tweet.displayUrl).into(ivTweetPic);
            }
            else{
                ivTweetPic.setVisibility(View.GONE);
            }
            tvTimeAgo.setText(getRelativeTimeAgo(tweet.createdAt));
        }

        private static final int SECOND_MILLIS = 1000;
        private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

        public String getRelativeTimeAgo(String rawJsonDate) {
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);

            try {
                long time = sf.parse(rawJsonDate).getTime();
                long now = System.currentTimeMillis();

                final long diff = now - time;
                if (diff < MINUTE_MILLIS) {
                    return "just now";
                } else if (diff < 2 * MINUTE_MILLIS) {
                    return "a minute ago";
                } else if (diff < 50 * MINUTE_MILLIS) {
                    return diff / MINUTE_MILLIS + " m";
                } else if (diff < 90 * MINUTE_MILLIS) {
                    return "an hour ago";
                } else if (diff < 24 * HOUR_MILLIS) {
                    return diff / HOUR_MILLIS + " h";
                } else if (diff < 48 * HOUR_MILLIS) {
                    return "yesterday";
                } else {
                    return diff / DAY_MILLIS + " d";
                }
            } catch (ParseException e) {
                Log.i("tweetsadaptertime", "getRelativeTimeAgo failed");
                e.printStackTrace();
            }

            return "";
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }
}
