package com.r4z0r.ocremixplayer.services;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;

import kotlin.jvm.internal.markers.KMutableList;


@UnstableApi
public class PlaybackService extends MediaSessionService {

    private MediaSession mediaSession = null;
    private Player player;

    private void initializeSessionAndPlayer() {
        player = new ExoPlayer.Builder(this).build();
        mediaSession = new MediaSession.Builder(this, player).build();
    }

    @Override
    public void onDestroy() {
        mediaSession.getPlayer().release();
        mediaSession.release();
        mediaSession = null;
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (mediaSession != null && player != null) {
            if (player.isPlaying() && player.getPlaybackState() == Player.STATE_READY && player.getPlayWhenReady()) {
                player.pause();
            }
        }
        stopSelf();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            stopSelf();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        stopSelf();
    }

    private void onAddMediaItems(MediaSession mediaSession, MediaSession.ControllerInfo controller, KMutableList mediaItems) {
    }

    @UnstableApi
    public void onCreate() {
        super.onCreate();
        initializeSessionAndPlayer();
    }

    @Nullable
    @Override
    public MediaSession onGetSession(MediaSession.ControllerInfo controllerInfo) {
        return mediaSession;
    }

}
