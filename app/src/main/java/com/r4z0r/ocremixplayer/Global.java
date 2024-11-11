package com.r4z0r.ocremixplayer;


import android.os.Bundle;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.SessionToken;
import androidx.navigation.NavController;

import com.google.common.util.concurrent.ListenableFuture;
import com.r4z0r.ocremixplayer.components.MiniPlayer;

import org.r4z0r.Wrapper;
import org.r4z0r.models.ResultItemGame;
import org.r4z0r.models.ResultItemMusic;
import org.r4z0r.models.SongInfor;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@UnstableApi
@Getter
public class Global {
    private static Global instance = null;

    @Setter
    private int offsetAtualizacoes = 0;

    @Setter
    private int offsetPesquisa = 0;

    @Setter
    private int positionListaAtualizacoes = 0;

    @Setter
    private int positionListaPesquisa = 0;

    @Setter
    private NavController navController;

    private final List<ResultItemMusic> listaUltimasMusicas = new ArrayList<>();

    private final List<ResultItemMusic> listaPesquisaMusica = new ArrayList<>();

    private final List<ResultItemGame> listaPesquisaJogo = new ArrayList<>();

    private final Wrapper wrapper;

    @Setter
    private Bundle searchBundle;

    @Setter
    private Bundle bundle = new Bundle();

    @Setter
    private String nomeMusica, artistaMusica;

    @Setter
    private SessionToken sessionToken;

    @Setter
    private androidx.media3.session.MediaController mediaController;

    @Setter
    private
    ListenableFuture<androidx.media3.session.MediaController> mediaControllerFuture;

    private Global() {
        wrapper = new Wrapper();
    }

    public static Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }
}
