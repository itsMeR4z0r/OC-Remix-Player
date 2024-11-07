package com.r4z0r.ocremixplayer.tasks;

import android.app.Application;

import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.tasks.interfaces.ResponseResultItemGame;

import org.r4z0r.models.ResultItemGame;

import java.util.List;
import java.util.concurrent.Executors;

public class SearchByGame extends TarefaAbstrata<ResponseResultItemGame> {
    public SearchByGame(Application application) {
        super(((OCRemixPlayerApplication) application).getGlobal(), Executors.newSingleThreadExecutor());
    }

    @Override
    public void execute(ResponseResultItemGame response, String title) {
        executorService.execute(() -> {
            try {
                response.onInit();
                List<ResultItemGame> result = global.getWrapper().searchByGameTitle(title);
                OCRemixPlayerApplication.mInstance.getGlobal().getListaPesquisaJogo().clear();
                OCRemixPlayerApplication.mInstance.getGlobal().getListaPesquisaJogo().addAll(result);
                response.onSuccess(result);
            } catch (Exception e) {
                response.onError(e.getMessage());
            }
        });
    }

    @Override
    public void execute(ResponseResultItemGame response) {
        this.execute(response, "");
    }
}
