package com.r4z0r.ocremixplayer.wrapper.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistItem {
    private String name;
    private String url;

    @Override
    public String toString() {
        return "ArtistItem{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}