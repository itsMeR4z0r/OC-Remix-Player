package com.r4z0r.ocremixplayer.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.common.Tracks;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.ui.DefaultTimeBar;
import androidx.media3.ui.TimeBar;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.R;

@UnstableApi
public class MiniPlayer extends MaterialCardView {
    private static final String TAG = "MiniPlayer";
    private ImageButton playPauseButton;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView artistTextView;
    private ProgressBar progressBar;

    private DefaultTimeBar seekBar;

    public MiniPlayer(Context context) {
        super(context);
        init();
    }

    public MiniPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MiniPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        inflate(getContext(), R.layout.mini_player, this);

        playPauseButton = findViewById(R.id.mini_player_play);
        nextButton = findViewById(R.id.mini_player_next);
        previousButton = findViewById(R.id.mini_player_previous);
        titleTextView = findViewById(R.id.mini_player_title);
        artistTextView = findViewById(R.id.mini_player_artist);
        progressBar = findViewById(R.id.mini_player_progress);
        imageView = findViewById(R.id.mini_player_iv);
        seekBar = findViewById(R.id.mini_player_timebar);

        if (OCRemixPlayerApplication.mInstance.getGlobal().getMediaController() == null)
            return;

        setListeners();
    }

    public void setListeners() {
        OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().addListener(new Player.Listener() {
            @Override
            public void onEvents(@NonNull Player player, @NonNull Player.Events events) {
                Player.Listener.super.onEvents(player, events);
                seekBar.setPosition(player.getCurrentPosition());
                seekBar.setDuration(player.getDuration());
                seekBar.setBufferedPosition(player.getBufferedPosition());
            }

            @Override
            public void onTracksChanged(@NonNull Tracks tracks) {
                Player.Listener.super.onTracksChanged(tracks);
            }

            @Override
            public void onIsLoadingChanged(boolean isLoading) {
                Player.Listener.super.onIsLoadingChanged(isLoading);
                if (isLoading) {
                    playPauseButton.setVisibility(GONE);
                    progressBar.setVisibility(VISIBLE);
                } else {
                    playPauseButton.setVisibility(VISIBLE);
                    progressBar.setVisibility(GONE);
                }
            }

            @Override
            public void onMediaMetadataChanged(@NonNull MediaMetadata mediaMetadata) {
                if (mediaMetadata.title != null) {
                    titleTextView.setText(mediaMetadata.title);
                }
                if (mediaMetadata.artist != null) {
                    artistTextView.setText(mediaMetadata.artist);
                }
                if (mediaMetadata.artworkUri != null) {
                    Glide.with(getContext()).load(mediaMetadata.artworkUri).into(imageView);
                } else if (mediaMetadata.artworkData != null) {
                    Glide.with(getContext()).load(mediaMetadata.artworkData).into(imageView);
                } else {
                    imageView.setImageResource(R.drawable.baseline_games_24);
                }
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                if (OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().isPlaying()) {
                    MiniPlayer.this.setVisibility(VISIBLE);
                    playPauseButton.setImageDrawable(getResources().getDrawable(androidx.media3.session.R.drawable.media3_icon_pause, getContext().getTheme()));
                } else {
                    playPauseButton.setImageDrawable(getResources().getDrawable(androidx.media3.session.R.drawable.media3_icon_play, getContext().getTheme()));
                }
            }
        });

        playPauseButton.setOnClickListener(v -> {
            if (OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().isPlaying()) {
                OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().pause();
            } else {
                OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().play();
            }
        });

        nextButton.setOnClickListener(v -> {
            if (OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().hasNextMediaItem())
                OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().seekToNextMediaItem();
        });

        previousButton.setOnClickListener(v -> {
            if (OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().hasPreviousMediaItem())
                OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().seekToPreviousMediaItem();
        });

        titleTextView.setMovementMethod(new ScrollingMovementMethod());
        titleTextView.setHorizontallyScrolling(true);

        artistTextView.setMovementMethod(new ScrollingMovementMethod());
        artistTextView.setHorizontallyScrolling(true);

        seekBar.addListener(new TimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(@NonNull TimeBar timeBar, long position) {

                System.out.println("onScrubStart");
            }

            @Override
            public void onScrubMove(@NonNull TimeBar timeBar, long position) {
            }

            @Override
            public void onScrubStop(@NonNull TimeBar timeBar, long position, boolean canceled) {
                OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().seekTo(position);
            }
        });
    }
}
