package com.r4z0r.ocremixplayer.tasks.interfaces;

import com.r4z0r.ocremixplayer.wrapper.models.SongInfor;

public interface ResponseSongInfor {
    void onInit();
    void onSuccess(SongInfor songInfor);
    void onError(String error);
}
