package com.r4z0r.ocremixplayer.wrapper.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SongInfor {
    private List<ArtistItem> artistList = new ArrayList<>();
    private String name;
    private List<String> downloadList = new ArrayList<>();
    private String duration;

    @Override
    public String toString() {
        return "SongInfor{" +
                "artistList=" + artistList +
                ", name='" + name + '\'' +
                ", downloadList=" + downloadList +
                ", duration='" + duration + '\'' +
                '}';
    }
}
