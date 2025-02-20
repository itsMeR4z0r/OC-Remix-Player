package com.r4z0r.ocremixplayer.wrapper.interfaces;

import com.r4z0r.ocremixplayer.wrapper.models.ResultItemGame;

import java.util.List;

public interface OnResponseResultItemGameList {
    void onSuccess(List<ResultItemGame> resultItems);
    void onError(Exception e);
}
