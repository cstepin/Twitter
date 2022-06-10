package com.codepath.apps.restclienttemplate;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends DialogFragment {

    private EditText mEditText;
    private Button btnTweet;
    TwitterClient client;

    public ComposeFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ComposeFragment newInstance(String username) {
        ComposeFragment frag = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.etCompose);
        btnTweet = view.findViewById(R.id.btnTweet);
        client = TwitterApp.getRestClient(getContext());

        mEditText.setText(getArguments().getString("username"));

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "What's Happening ...");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Make an API call to Twitter
                String tweetContent = mEditText.getText().toString();
                if(tweetContent.isEmpty()){
                    Toast.makeText(getContext(), "Text must NOT be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(tweetContent.length() > 280){
                    Toast.makeText(getContext(), "Text cannot be greater than 140 characters", Toast.LENGTH_LONG).show();
                    return;
                }
                //  Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show();
                // return;
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i("asdf", "on success to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i("asdf", "published tweet: " + tweet.body);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            getActivity().setResult(RESULT_OK, intent);
                            getActivity().finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.i("asdf", "on failure to publish tweet: " + response);
                    }
                });

            }
        });
    }
}