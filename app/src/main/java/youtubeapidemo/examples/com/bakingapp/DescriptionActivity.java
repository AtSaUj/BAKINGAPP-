package youtubeapidemo.examples.com.bakingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

/**
 * Created by 1515012 on 11-07-2017.
 */

public class DescriptionActivity extends AppCompatActivity {
    private static final String TAG = DescriptionActivity.class.getSimpleName();
    /*  @BindView(R.id.description)
       TextView description;
       @BindView(R.id.description_position)
       TextView description_position;
       @BindView(R.id.next_button)
       Button nextButton;
       @BindView(R.id.previous_button)
       Button previousButton;
       @BindView(R.id.video_view)*/
    SimpleExoPlayerView playerView;
    private int currentPosition = 0;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow;
    private long playbackPosition;
    private ArrayList<Description> descriptionArrayList;
    private TextView description, description_position;
    private Button previousButton, nextButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Log.e(TAG, "onCreate() called");
        //  ButterKnife.bind(this);
        description = (TextView) findViewById(R.id.description);
        playerView = (SimpleExoPlayerView) findViewById(R.id.video_view);
        previousButton = (Button) findViewById(R.id.previous_button);
        nextButton = (Button) findViewById(R.id.next_button);
        description_position = (TextView) findViewById(R.id.description_position);


        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        if (extra != null)
            descriptionArrayList = extra.getParcelableArrayList("list");
        if (savedInstanceState == null && this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            display();
        }

//        Log.i(TAG, descriptionArrayList.size() + "");


    }

    private void display() {
        description.setText(descriptionArrayList.get(currentPosition).getDescription());

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition++;
                if (currentPosition == descriptionArrayList.size() - 1) {
                    nextButton.setVisibility(View.INVISIBLE);
                }

                description.setText(descriptionArrayList.get(currentPosition).getDescription());
                previousButton.setVisibility(View.VISIBLE);
                description_position.setText("" + (currentPosition) + "/" + (descriptionArrayList.size() - 1));
                description_position.setVisibility(View.VISIBLE);
                releasePlayer();
                if ((Util.SDK_INT <= 23 || player == null)) {
                    initializePlayer();
                } else if (Util.SDK_INT > 23) {
                    initializePlayer();
                }

            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition--;
                if (currentPosition == 0) {
                    previousButton.setVisibility(View.INVISIBLE);
                    description_position.setVisibility(View.INVISIBLE);
                }

                description.setText(descriptionArrayList.get(currentPosition).getDescription());
                nextButton.setVisibility(View.VISIBLE);
                description_position.setText("" + (currentPosition) + "/" + (descriptionArrayList.size() - 1));
                description.setText(descriptionArrayList.get(currentPosition).getDescription());
                releasePlayer();
                if ((Util.SDK_INT <= 23 || player == null)) {
                    initializePlayer();
                } else if (Util.SDK_INT > 23) {
                    initializePlayer();
                }
            }
        });

    }


    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);
//        Log.i(TAG, descriptionArrayList.get(currentPosition).getVideoURL());
        String url = descriptionArrayList.get(currentPosition).getVideoURL();
            Uri uri = Uri.parse(url);
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);
            player.setPlayWhenReady(playWhenReady);

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart() called");
        if (Util.SDK_INT > 23) {
            initializePlayer();
            player.seekTo(currentWindow, playbackPosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause() called");
        if (Util.SDK_INT <= 23) {
            releasePlayer();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop() called");
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume() called");
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUi();
        }
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
            player.seekTo(currentWindow, playbackPosition);
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG, "onRestoreInstance() called");
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("current_position");
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                display();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState() called");
        outState.putInt("current_position", currentPosition);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDistroy() called");
    }
}