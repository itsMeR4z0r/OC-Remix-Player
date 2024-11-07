package com.r4z0r.ocremixplayer.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.r4z0r.ocremixplayer.listeners.EndlessRecyclerViewScrollListener;

import lombok.Getter;
import lombok.Setter;

public class RvMusica extends RecyclerView {
    @Setter
    private RvMusicaInterface listener;
    @Getter
    private MaterialDividerItemDecoration divider;
    @Getter
    private LinearLayoutManager layoutManager;

    @Getter
    private EndlessRecyclerViewScrollListener scrollListener;

    public RvMusica(@NonNull Context context) {
        super(context);
        setPropriedades();
    }

    public RvMusica(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setPropriedades();
    }

    public RvMusica(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPropriedades();
    }

    private void setPropriedades() {
        divider = new MaterialDividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        divider.setDividerColor(Color.TRANSPARENT);
        divider.setDividerThickness(20);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (listener == null) return;
                listener.onLoadMore();
            }
        };

        setLayoutManager(layoutManager);
        addItemDecoration(divider);
        setVerticalFadingEdgeEnabled(true);
        setFadingEdgeLength(24);
        addOnScrollListener(scrollListener);
    }

    public interface RvMusicaInterface {
        void onLoadMore();
    }
}
