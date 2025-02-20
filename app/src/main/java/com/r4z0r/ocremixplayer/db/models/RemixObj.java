package com.r4z0r.ocremixplayer.db.models;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

@Entity
public class RemixObj {
    @Id
    public Long id;
    @Index
    public String remixId;
    public String name;
    public List<String> artists;
    public String gameImageUrl;
    public String gameName;
    public String urlSong;
    public String gameUrl;
}
