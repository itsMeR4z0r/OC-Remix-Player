package com.r4z0r.ocremixplayer;


import android.os.Bundle;

import androidx.navigation.NavController;

import com.r4z0r.ocremixplayer.tasks.GetLastSongs;

import org.r4z0r.Wrapper;
import org.r4z0r.models.ResultItemGame;
import org.r4z0r.models.ResultItemMusic;

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

    private final Wrapper wrapper;

    @Setter
    private Bundle searchBundle;

    @Setter
    private Bundle bundle = new Bundle();

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
