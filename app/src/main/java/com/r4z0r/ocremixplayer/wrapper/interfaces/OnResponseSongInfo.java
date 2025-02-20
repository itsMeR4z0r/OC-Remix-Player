package com.r4z0r.ocremixplayer.wrapper.interfaces;

import com.r4z0r.ocremixplayer.wrapper.models.SongInfor;

public interface OnResponseSongInfo {
    void onSuccess(SongInfor songInfor);
    void onError(Exception e);
}
