package com.r4z0r.ocremixplayer.tasks;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;

import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.converter.MusicaConverter;
import com.r4z0r.ocremixplayer.db.boxes.RemixBox;
import com.r4z0r.ocremixplayer.db.models.RemixObj;
import com.r4z0r.ocremixplayer.models.Musica;
import com.r4z0r.ocremixplayer.tasks.interfaces.ResponseResultItemMusic;

import com.r4z0r.ocremixplayer.wrapper.interfaces.OnResponseResultItemMusicList;
import com.r4z0r.ocremixplayer.wrapper.interfaces.OnResponseSongInfo;
import com.r4z0r.ocremixplayer.wrapper.models.ArtistItem;
import com.r4z0r.ocremixplayer.wrapper.models.ResultItemGame;
import com.r4z0r.ocremixplayer.wrapper.models.ResultItemMusic;
import com.r4z0r.ocremixplayer.wrapper.models.SongInfor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@OptIn(markerClass = UnstableApi.class)
public class GetLastSongs extends TarefaAbstrata<ResponseResultItemMusic> {
    public GetLastSongs(Application application) {
        super(((OCRemixPlayerApplication) application).getGlobal(), Executors.newSingleThreadExecutor());
    }

    @Override
    public void execute(ResponseResultItemMusic response, String param) {
        execute(response);
    }

    @Override
    public void execute(ResponseResultItemMusic response) {
        response.onInit();
        global.getWrapper().getLastSongs(global.getOffsetAtualizacoes(), new OnResponseResultItemMusicList() {
            @Override
            public void onSuccess(List<ResultItemMusic> list) {
                global.setOffsetAtualizacoes(global.getOffsetAtualizacoes() + list.size());
                RemixBox remixBox = OCRemixPlayerApplication.mInstance.getGlobal().getRemixBox();
                List<Musica> musicaList = MusicaConverter.converterResultItem(list);
                for (Musica item : musicaList) {
                    String idRemix = item.getUrl().substring(item.getUrl().lastIndexOf("/") + 1);
                    if (!remixBox.containsId(idRemix)) {
                        try {
                            global.getWrapper().getRemix(item.getUrl(), new OnResponseSongInfo() {
                                @Override
                                public void onSuccess(SongInfor songInfor) {
                                    try {
                                        RemixObj remixObj = new RemixObj();
                                        remixObj.remixId = idRemix;
                                        remixObj.gameImageUrl = item.getGameImageUrl();
                                        remixObj.name = item.getName();
                                        remixObj.urlSong = songInfor.getDownloadList().get(0);
                                        remixObj.artists = new ArrayList<>();

                                        for (ArtistItem artistItem : item.getArtistItemList()) {
                                            remixObj.artists.add(artistItem.getName());
                                        }

                                        remixObj.gameName = item.getGameTitle();
                                        remixObj.gameUrl = item.getGameUrl();

                                        remixBox.put(remixObj);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                response.onSuccess(list);
            }

            @Override
            public void onError(Exception e) {
                response.onError(e.getMessage());
            }
        });
    }
}
