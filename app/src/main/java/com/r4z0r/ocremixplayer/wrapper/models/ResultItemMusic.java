package com.r4z0r.ocremixplayer.wrapper.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ResultItemMusic {
    private String gameTitle;
    private String gameUrl;
    private List<ArtistItem> composerList = new ArrayList<>();
    private String gameImageUrl;
    private List<SongItem> musicList = new ArrayList<>();
    public void addComposer(String name, String url){
        ArtistItem artistItem = new ArtistItem();
        artistItem.setName(name);
        artistItem.setUrl(url);
        this.composerList.add(artistItem);
    }

    @Override
    public String toString() {
        return "ResultItemMusic{" +
                "gameTitle='" + gameTitle + '\'' +
                ", gameUrl='" + gameUrl + '\'' +
                ", composerList=" + composerList +
                ", gameImageUrl='" + gameImageUrl + '\'' +
                ", musicList=" + musicList +
                '}';
    }

    public int addSong(String name, String url, Date date, String preview){
        SongItem songItem = new SongItem();
        songItem.setName(name);
        songItem.setUrl(url);
        songItem.setDate(date);
        songItem.setPreview(preview);
        this.musicList.add(songItem);
        return this.musicList.size() - 1;


    }

}


