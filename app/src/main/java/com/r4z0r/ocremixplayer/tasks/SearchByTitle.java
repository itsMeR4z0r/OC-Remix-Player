package com.r4z0r.ocremixplayer.tasks;

import android.app.Application;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;

import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.tasks.interfaces.ResponseResultItemMusic;

import org.r4z0r.models.ResultItemMusic;

import java.util.List;
import java.util.concurrent.Executors;

@OptIn(markerClass = UnstableApi.class)
public class SearchByTitle extends TarefaAbstrata<ResponseResultItemMusic> {

    public SearchByTitle(Application application) {
        super(((OCRemixPlayerApplication) application).getGlobal(), Executors.newSingleThreadExecutor());
    }

    @Override
    public void execute(ResponseResultItemMusic response, String title) {
        executorService.execute(() -> {
            try {
                response.onInit();
                List<ResultItemMusic> result = global.getWrapper().searchByMusicTitle(title);
                OCRemixPlayerApplication.mInstance.getGlobal().getListaPesquisaMusica().clear();
                OCRemixPlayerApplication.mInstance.getGlobal().getListaPesquisaMusica().addAll(result);
                response.onSuccess(result);
            } catch (Exception e) {
                response.onError(e.getMessage());
            }
        });
    }

    @Override
    public void execute(ResponseResultItemMusic response) {
        execute(response, "");
    }

}
