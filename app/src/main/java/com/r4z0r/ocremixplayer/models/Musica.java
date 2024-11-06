package com.r4z0r.ocremixplayer.models;

import org.r4z0r.models.SongItem;

public class Musica extends SongItem {
    private String gameTitle;
    private String gameUrl;
    private String gameImageUrl;

    public Musica() {
        this.gameTitle = gameTitle;
        this.gameUrl = gameUrl;
        this.gameImageUrl = gameImageUrl;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public String getGameImageUrl() {
        return gameImageUrl;
    }

    public void setGameImageUrl(String gameImageUrl) {
        this.gameImageUrl = gameImageUrl;
    }
}
