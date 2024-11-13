package com.r4z0r.ocremixplayer.tasks;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;

import com.r4z0r.ocremixplayer.Global;

import java.util.concurrent.ExecutorService;

@OptIn(markerClass = UnstableApi.class)
public abstract class TarefaAbstrata<T> {
    protected final Global global;
    protected final ExecutorService executorService;

    public TarefaAbstrata(Global global, ExecutorService executorService) {
        this.global = global;
        this.executorService = executorService;
    }

    public abstract void execute(T response,String param);
    public abstract void execute(T response);
}
