package com.r4z0r.ocremixplayer;

import android.app.Application;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.r4z0r.ocremixplayer.db.ObjectBox;
import com.r4z0r.ocremixplayer.db.boxes.PlaylistBox;
import com.r4z0r.ocremixplayer.db.boxes.RemixBox;

import com.r4z0r.ocremixplayer.wrapper.Wrapper;

import lombok.Getter;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

@Getter
public class OCRemixPlayerApplication extends Application {
    public static OCRemixPlayerApplication mInstance;

    private Global global;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initGlobal();
        initDb();
    }

    private void initGlobal() {
        global = Global.getInstance();
        global.setWrapper(
                new Wrapper(
                        new OkHttpClient
                                .Builder()
                                .connectionPool(
                                        new ConnectionPool(
                                                5,
                                                5,
                                                java.util.concurrent.TimeUnit.MINUTES
                                        )
                                )
                                .retryOnConnectionFailure(true)
                                .cookieJar(
                                        new PersistentCookieJar(
                                                new SetCookieCache(),
                                                new SharedPrefsCookiePersistor(getApplicationContext())
                                        )
                                )
                                .fastFallback(true)
                                .cache(
                                        new Cache(
                                                OCRemixPlayerApplication.mInstance.getApplicationContext().getCacheDir(),
                                                500L * 1024L * 1024L
                                        )
                                )
                                .build()
                )
        );
    }

    private void initDb() {
        ObjectBox.init(getApplicationContext());
        global.setRemixBox(new RemixBox());
        global.setPlaylistBox(new PlaylistBox());
    }
}
