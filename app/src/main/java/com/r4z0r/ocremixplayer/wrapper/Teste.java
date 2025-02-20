package com.r4z0r.ocremixplayer.wrapper;

import com.r4z0r.ocremixplayer.wrapper.interfaces.OnResponseResultItemMusicList;
import com.r4z0r.ocremixplayer.wrapper.models.ResultItemMusic;

import java.util.ArrayList;
import java.util.List;

public class Teste {
    public static void main(String[] args) {
        Wrapper wrapper = new Wrapper();
        List<ResultItemMusic> result = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            wrapper.getLastSongs(result.size(), new OnResponseResultItemMusicList() {
                @Override
                public void onSuccess(List<ResultItemMusic> resultItems) {
                    result.addAll(resultItems);
                    for (ResultItemMusic resultItem : resultItems) {
                        System.out.println(resultItem);
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
