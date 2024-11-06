package com.r4z0r.ocremixplayer.tasks;

import android.app.Application;

import com.r4z0r.ocremixplayer.Global;
import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;

import org.r4z0r.models.ResultItemMusic;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetLastSongs {
    private final Global global;
    private final ExecutorService executorService;

    public GetLastSongs(Application application) {
        executorService = Executors.newSingleThreadExecutor();
        global = ((OCRemixPlayerApplication) application).getGlobal();
    }

    public void execute(ResponseResultItemMusic response) {
        executorService.execute(() -> {
            try {
                response.onInit();
                List<ResultItemMusic> result = global.getWrapper().getLastSongs(global.getOffsetAtualizacoes());
                global.setOffsetAtualizacoes(global.getOffsetAtualizacoes() + result.size());
                response.onSuccess(result);
            } catch (Exception e) {
                response.onError(e.getMessage());
            }
        });
    }
}
