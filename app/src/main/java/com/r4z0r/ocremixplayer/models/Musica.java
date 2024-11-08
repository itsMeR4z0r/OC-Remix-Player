package com.r4z0r.ocremixplayer.models;

import android.os.Parcelable;

import org.r4z0r.models.SongItem;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Musica extends SongItem implements Serializable {
    private String gameTitle;
    private String gameUrl;
    private String gameImageUrl;

    public Musica() {
        this.gameTitle = gameTitle;
        this.gameUrl = gameUrl;
        this.gameImageUrl = gameImageUrl;
    }

}
