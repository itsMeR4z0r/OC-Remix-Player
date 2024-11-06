package com.r4z0r.ocremixplayer.tasks;

import org.r4z0r.models.ResultItemMusic;

import java.util.List;

public interface ResponseResultItemMusic {
    void onInit();

    void onSuccess(List<ResultItemMusic> result);

    void onError(String error);
}
