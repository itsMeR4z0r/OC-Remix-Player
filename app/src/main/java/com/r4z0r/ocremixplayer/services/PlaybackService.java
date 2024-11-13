package com.r4z0r.ocremixplayer.services;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.database.StandaloneDatabaseProvider;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.datasource.cache.NoOpCacheEvictor;
import androidx.media3.datasource.cache.SimpleCache;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.offline.DownloadManager;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;

import java.io.File;
import java.util.concurrent.Executor;

import lombok.Getter;


@UnstableApi
public class PlaybackService extends MediaSessionService {
    private MediaSession mediaSession = null;
    private Player player;

    @Getter
    private DownloadManager downloadManager;
    private StandaloneDatabaseProvider databaseProvider;
    private SimpleCache downloadCache;
    private DataSource.Factory dataSourceFactory;
    private DataSource.Factory cacheDataSourceFactory;
    private Executor downloadExecutor;

    private void initializeSessionAndPlayer() {
        File cacheDir = new File(getCacheDir() + File.separator + "media");

        if (!cacheDir.exists()) {
            if (cacheDir.mkdirs()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory not created");
            }
        }

        // Note: This should be a singleton in your app.
        databaseProvider = new StandaloneDatabaseProvider(getApplicationContext());

        // A download cache should not evict media, so should use a NoopCacheEvictor.
        downloadCache = new SimpleCache(cacheDir, new NoOpCacheEvictor(), databaseProvider);

        // Create a factory for reading the data from the network.
        dataSourceFactory = new DefaultHttpDataSource.Factory();

        downloadExecutor = Runnable::run;

        // Create the download manager.
        downloadManager =
                new DownloadManager(
                        getApplicationContext(), databaseProvider, downloadCache, dataSourceFactory, downloadExecutor);


        // Create a read-only cache data source factory using the download cache.
        cacheDataSourceFactory =
                new CacheDataSource.Factory()
                        .setCache(downloadCache)
                        .setUpstreamDataSourceFactory(dataSourceFactory);

        player = new ExoPlayer
                .Builder(this)
                .setMediaSourceFactory(
                        new DefaultMediaSourceFactory(getApplicationContext())
                                .setDataSourceFactory(cacheDataSourceFactory))
                .build();

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
