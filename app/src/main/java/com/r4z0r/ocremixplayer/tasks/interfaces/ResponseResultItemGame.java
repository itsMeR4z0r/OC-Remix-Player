package com.r4z0r.ocremixplayer.tasks.interfaces;

import com.r4z0r.ocremixplayer.wrapper.models.ResultItemGame;

import java.util.List;

public interface ResponseResultItemGame {
    void onInit();

    void onSuccess(List<ResultItemGame> result);

    void onError(String error);
}
