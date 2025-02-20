package com.r4z0r.ocremixplayer.wrapper.interfaces;

import com.r4z0r.ocremixplayer.wrapper.models.ResultItemMusic;

import java.util.List;

public interface OnResponseResultItemMusicList {
    void onSuccess(List<ResultItemMusic> resultItems);
    void onError(Exception e);
}
