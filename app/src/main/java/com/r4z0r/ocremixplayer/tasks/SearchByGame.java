package com.r4z0r.ocremixplayer.tasks;

import android.app.Application;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;

import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.tasks.interfaces.ResponseResultItemGame;

import com.r4z0r.ocremixplayer.wrapper.interfaces.OnResponseResultItemGameList;
import com.r4z0r.ocremixplayer.wrapper.models.ResultItemGame;

import java.util.List;
import java.util.concurrent.Executors;

@OptIn(markerClass = UnstableApi.class)
public class SearchByGame extends TarefaAbstrata<ResponseResultItemGame> {
    public SearchByGame(Application application) {
        super(((OCRemixPlayerApplication) application).getGlobal(), Executors.newSingleThreadExecutor());
    }

    @Override
    public void execute(ResponseResultItemGame response, String title) {
        response.onInit();
        global.getWrapper().searchByGameTitle(global.getOffsetPesquisa(), title, new OnResponseResultItemGameList() {
            @Override
            public void onSuccess(List<ResultItemGame> list) {
                global.setOffsetPesquisa(global.getOffsetPesquisa() + list.size());
                response.onSuccess(list);
            }

            @Override
            public void onError(Exception e) {
                response.onError(e.getMessage());
            }
        });
    }

    @Override
    public void execute(ResponseResultItemGame response) {
        this.execute(response, "");
    }

}
