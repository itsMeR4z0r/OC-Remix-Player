package com.r4z0r.ocremixplayer.tasks.interfaces;

import com.r4z0r.ocremixplayer.wrapper.models.ResultItemMusic;

import java.util.List;

public interface ResponseResultItemMusic {
    void onInit();

    void onSuccess(List<ResultItemMusic> result);

    void onError(String error);
}
