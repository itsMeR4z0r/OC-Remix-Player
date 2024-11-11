package com.r4z0r.ocremixplayer.tasks.interfaces;

import org.r4z0r.models.SongInfor;

public interface ResponseSongInfor {
    void onInit();
    void onSuccess(SongInfor songInfor);
    void onError(String error);
}
