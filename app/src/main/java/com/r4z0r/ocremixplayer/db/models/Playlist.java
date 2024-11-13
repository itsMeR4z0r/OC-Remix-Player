package com.r4z0r.ocremixplayer.db.models;

import java.util.Date;
import java.util.List;

import io.objectbox.annotation.ConflictStrategy;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Unique;

@Entity
public class Playlist {
    @Id
    long id;

    @Unique(onConflict = ConflictStrategy.REPLACE)
    String name;

    List<Remix> songs;

    Date createdAt;

    Date updatedAt;
}
