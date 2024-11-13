package com.r4z0r.ocremixplayer.db;

import android.content.Context;

import com.r4z0r.ocremixplayer.db.models.MyObjectBox;

import io.objectbox.BoxStore;

public class ObjectBox {
    private static BoxStore store;

    public static void init(Context context) {
        store = MyObjectBox.builder()
                .androidContext(context)
                .build();
    }

    public static BoxStore get() {
        return store;
    }

}
