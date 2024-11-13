package com.r4z0r.ocremixplayer.tasks;

import android.app.Application;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;

import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.tasks.interfaces.ResponseSongInfor;

import org.r4z0r.models.ResultItemMusic;
import org.r4z0r.models.SongInfor;

import java.util.concurrent.Executors;

@OptIn(markerClass = UnstableApi.class)
public class GetRemix extends TarefaAbstrata<ResponseSongInfor>{
    public GetRemix(Application application) {
        super(((OCRemixPlayerApplication) application).getGlobal(), Executors.newSingleThreadExecutor());
    }

    @Override
    public void execute(ResponseSongInfor response, String param) {
        executorService.execute(() -> {
            try {
                response.onInit();
                SongInfor songInfor = OCRemixPlayerApplication.mInstance.getGlobal().getWrapper().getRemix(param);
                response.onSuccess(songInfor);
            } catch (Exception e) {
                e.printStackTrace();
                response.onError(e.getMessage());
            }
        });
    }

    @Override
    public void execute(ResponseSongInfor response) {

    }
}
