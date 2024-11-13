package com.r4z0r.ocremixplayer;

import android.net.Uri;
import android.os.Handler;

import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;

import com.r4z0r.ocremixplayer.models.Musica;

import org.r4z0r.models.SongInfor;

@UnstableApi
public class AudioControler {
    private final Global global;
    private final Handler handler;

    public AudioControler() {
        this.global = OCRemixPlayerApplication.mInstance.getGlobal();
        this.handler = new Handler(global.getMediaController().getApplicationLooper());
    }

    public void play(SongInfor songInfor, Musica musica) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Uri uri = Uri.parse(songInfor.getDownloadList().get(0));
                MediaItem mediaItem = MediaItem.fromUri(uri);
                OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().setMediaItem(mediaItem);
                OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().prepare();
                OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().setPlayWhenReady(true);
            }
        });
    }
}
