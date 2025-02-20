package com.r4z0r.ocremixplayer.tasks;

import android.app.Application;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;

import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.tasks.interfaces.ResponseResultItemMusic;

import com.r4z0r.ocremixplayer.wrapper.interfaces.OnResponseResultItemMusicList;
import com.r4z0r.ocremixplayer.wrapper.models.ResultItemMusic;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

@OptIn(markerClass = UnstableApi.class)
public class SearchByTitle extends TarefaAbstrata<ResponseResultItemMusic> {

    public SearchByTitle(Application application) {
        super(((OCRemixPlayerApplication) application).getGlobal(), Executors.newSingleThreadExecutor());
    }

    @Override
    public void execute(ResponseResultItemMusic response, String title) {
        response.onInit();
        try {
            global.getWrapper().searchByMusicTitle(global.getOffsetPesquisa(), title, new OnResponseResultItemMusicList() {
                @Override
                public void onSuccess(List<ResultItemMusic> list) {
                    global.setOffsetPesquisa(global.getOffsetPesquisa() + list.size());
                    response.onSuccess(list);
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
    public void execute(ResponseResultItemMusic response) {
        execute(response, "");
    }
}
