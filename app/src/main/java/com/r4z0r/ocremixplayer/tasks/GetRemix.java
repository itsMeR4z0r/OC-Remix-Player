package com.r4z0r.ocremixplayer.tasks;

import android.app.Application;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;

import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.tasks.interfaces.ResponseSongInfor;

import com.r4z0r.ocremixplayer.wrapper.interfaces.OnResponseSongInfo;
import com.r4z0r.ocremixplayer.wrapper.models.SongInfor;

import java.io.IOException;
import java.util.concurrent.Executors;

@OptIn(markerClass = UnstableApi.class)
public class GetRemix extends TarefaAbstrata<ResponseSongInfor> {
    public GetRemix(Application application) {
        super(((OCRemixPlayerApplication) application).getGlobal(), Executors.newSingleThreadExecutor());
    }

    @Override
    public void execute(ResponseSongInfor response, String param) {
        response.onInit();
        try {
            OCRemixPlayerApplication.mInstance.getGlobal().getWrapper().getRemix(param, new OnResponseSongInfo() {
                @Override
                public void onSuccess(SongInfor songInfor) {
                    response.onSuccess(songInfor);
                }

                @Override
                public void onError(Exception e) {
                    response.onError(e.getMessage());
                }
            });
        } catch (IOException e) {
            response.onError(e.getMessage());
        }
    }

    @Override
    public void execute(ResponseSongInfor response) {

    }
}
