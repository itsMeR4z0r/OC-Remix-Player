package com.r4z0r.ocremixplayer;

import android.app.Application;

import androidx.media3.common.util.UnstableApi;

import lombok.Getter;

@UnstableApi
@Getter
public class OCRemixPlayerApplication extends Application {
    public static OCRemixPlayerApplication mInstance;

    private Global global;

    @UnstableApi
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        global = Global.getInstance();
    }

}
