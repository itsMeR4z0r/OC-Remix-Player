package com.r4z0r.ocremixplayer.wrapper.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SongItem {
    private String name;
    private String url;
    private String preview;
    private Date date;
    private List<ArtistItem> artistItemList = new ArrayList<>();
    private List<OriginalSong> originalSongList = new ArrayList<>();

    public void addArtist(String name, String url) {
        ArtistItem artistItem = new ArtistItem();
        artistItem.setName(name);
        artistItem.setUrl(url);
        this.artistItemList.add(artistItem);
    }

    public void addOriginalSong(String name, String url) {
        OriginalSong originalSong = new OriginalSong();
        originalSong.setName(name);
        originalSong.setUrl(url);
        this.originalSongList.add(originalSong);
    }

    @Override
    public String toString() {
        return "SongItem{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", preview='" + preview + '\'' +
                ", date=" + date +
                ", artistItemList=" + artistItemList +
                ", originalSongList=" + originalSongList +
                '}';
    }

    @Getter
    @Setter
    public class OriginalSong {
        private String name;
        private String url;
    }
}


