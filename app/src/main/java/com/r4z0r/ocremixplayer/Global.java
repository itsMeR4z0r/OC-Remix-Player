package com.r4z0r.ocremixplayer;


import android.os.Bundle;

import androidx.media3.session.SessionToken;
import androidx.navigation.NavController;

import com.google.common.util.concurrent.ListenableFuture;
import com.r4z0r.ocremixplayer.db.boxes.PlaylistBox;
import com.r4z0r.ocremixplayer.db.boxes.RemixBox;
import com.r4z0r.ocremixplayer.wrapper.Wrapper;
import com.r4z0r.ocremixplayer.wrapper.models.ResultItemGame;
import com.r4z0r.ocremixplayer.wrapper.models.ResultItemMusic;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

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

    @Setter
    private Wrapper wrapper;

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

    @Setter
    private RemixBox remixBox;
    @Setter
    private PlaylistBox playlistBox;

    private Global() {
        wrapper = new Wrapper();
    }

    public static Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }

    public static void cleanSearch() {
        OCRemixPlayerApplication.mInstance.getGlobal().setOffsetPesquisa(0);
        OCRemixPlayerApplication.mInstance.getGlobal().getListaPesquisaJogo().clear();
    }
}
