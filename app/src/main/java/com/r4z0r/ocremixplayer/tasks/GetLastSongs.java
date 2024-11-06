package com.r4z0r.ocremixplayer.tasks;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.r4z0r.ocremixplayer.Global;
import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;

import org.r4z0r.models.ResultItemMusic;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetLastSongs {
    private Global global;
    private ExecutorService executorService;
    private Handler mainHandler;

    public GetLastSongs(Application application) {
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        global = ((OCRemixPlayerApplication) application).getGlobal();
    }

    public void execute(Response response) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    response.onInit();
                    List<ResultItemMusic> result = global.getWrapper().getLastSongs(global.getOffsetAtualizacoes());
                    global.setOffsetAtualizacoes(global.getOffsetAtualizacoes() + result.size());
                    response.onSuccess(result);
                } catch (Exception e) {
                    response.onError(e.getMessage());
                }
            }
        });
    }

    public interface Response {
        void onInit();

        void onSuccess(List<ResultItemMusic> result);

        void onError(String error);
    }
}
