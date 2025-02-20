package com.r4z0r.ocremixplayer.converter;

import com.r4z0r.ocremixplayer.models.Musica;

import com.r4z0r.ocremixplayer.wrapper.models.ResultItemMusic;
import com.r4z0r.ocremixplayer.wrapper.models.SongItem;

import java.util.ArrayList;
import java.util.List;

public class MusicaConverter {
    public static List<Musica> converter(ResultItemMusic resultItemMusic) {
        List<Musica> musicas = new ArrayList<>();
        if (resultItemMusic == null) return musicas;
        for (SongItem songItem : resultItemMusic.getMusicList()) {
            Musica musica = new Musica();
            musica.setDate(songItem.getDate());
            musica.setName(songItem.getName());
            musica.setArtistItemList(songItem.getArtistItemList());
            musica.setOriginalSongList(songItem.getOriginalSongList());
            musica.setPreview(songItem.getPreview());
            musica.setUrl(songItem.getUrl());
            musica.setGameImageUrl(resultItemMusic.getGameImageUrl());
            musica.setGameTitle(resultItemMusic.getGameTitle());
            musica.setGameUrl(resultItemMusic.getGameUrl());
            musicas.add(musica);
        }
        return musicas;
    }

    public static List<Musica> converterResultItem(List<ResultItemMusic> resultItemMusics) {
        List<Musica> musicas = new ArrayList<>();
        for (ResultItemMusic resultItemMusic : resultItemMusics) {
            musicas.addAll(converter(resultItemMusic));
        }
        return musicas;
    }
}
