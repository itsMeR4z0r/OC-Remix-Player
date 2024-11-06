package com.r4z0r.ocremixplayer;

import android.app.Application;

import com.r4z0r.ocremixplayer.tasks.GetLastSongs;

import org.r4z0r.models.ResultItemMusic;

import java.util.List;

import lombok.Getter;

@Getter
public class OCRemixPlayerApplication extends Application {
    public static OCRemixPlayerApplication mInstance;

    private Global global;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        global = Global.getInstance();
    }


}
