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

public class SearchByTitle {
    private final Global global;
    private final ExecutorService executorService;

    public SearchByTitle(Application application) {
        executorService = Executors.newSingleThreadExecutor();
        global = ((OCRemixPlayerApplication) application).getGlobal();
    }

    public void execute(ResponseResultItemMusic response, String title) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    response.onInit();
                    List<ResultItemMusic> result = global.getWrapper().searchByMusicTitle(title);
                    response.onSuccess(result);
                } catch (Exception e) {
                    response.onError(e.getMessage());
                }
            }
        });
    }

}
