package com.r4z0r.ocremixplayer.wrapper.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class ResultItemGame {
    private String system;
    private String gameTitle;
    private String publisher;
    private Integer year;
    private List<ArtistResult> artists = new ArrayList<>();
    private Integer remixes;
    private Integer albuns;
    private String systemImage;
    private String publisherUrl;
    private String systemUrl;
    private String gameUrl;
    private String chipTuneUrl;
    private boolean hasChipTune = false;
    public void addArtist(String name, String url){
        ArtistResult artist = new ArtistResult();
        artist.setName(name);
        artist.setUrl(url);
        this.artists.add(artist);
    }

    @Override
    public String toString() {
        return "ResultItemGame{" +
                "system='" + system + '\'' +
                ", gameTitle='" + gameTitle + '\'' +
                ", publisher='" + publisher + '\'' +
                ", year=" + year +
                ", artists=" + artists +
                ", remixes=" + remixes +
                ", albuns=" + albuns +
                ", systemImage='" + systemImage + '\'' +
                ", publisherUrl='" + publisherUrl + '\'' +
                ", systemUrl='" + systemUrl + '\'' +
                ", gameUrl='" + gameUrl + '\'' +
                ", chipTuneUrl='" + chipTuneUrl + '\'' +
                ", hasChipTune=" + hasChipTune +
                '}';
    }
}
@Getter
@Setter
class ArtistResult{
    private String name;
    private String url;

    @Override
    public String toString() {
        return "ArtistResult{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}