package com.r4z0r.ocremixplayer.db.models;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.IndexType;

@Entity
public class Remix {
    @Id
    Long id;
    @Index
    String remixId;
    String name;
    List<String> artists;
    String gameImageUrl;
    String gameName;
    String urlSong;
}
