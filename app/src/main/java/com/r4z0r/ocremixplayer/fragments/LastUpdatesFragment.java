package com.r4z0r.ocremixplayer.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.adapters.MusicaAdapter;
import com.r4z0r.ocremixplayer.databinding.LastUpdatesFragmentBinding;
import com.r4z0r.ocremixplayer.listeners.EndlessRecyclerViewScrollListener;
import com.r4z0r.ocremixplayer.tasks.GetLastSongs;

import org.r4z0r.models.ResultItemMusic;

import java.util.ArrayList;
import java.util.List;

public class LastUpdatesFragment extends Fragment {
    private LastUpdatesFragmentBinding binding;
    private RecyclerView rvMusica;
    private ProgressBar progressBar;
    private LinearLayout layoutError;
    private MaterialButton btnTryAgain;
    private MusicaAdapter adapter;

    private LinearLayoutManager layoutManager;

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = LastUpdatesFragmentBinding.inflate(inflater, container, false);
        adapter = new MusicaAdapter(getContext(), new ArrayList<>());
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMusica = binding.lastUpdateFragmentRv;
        progressBar = binding.lastUpdateFragmentPb;
        layoutError = binding.lastUpdateFragmentLl;
        btnTryAgain = binding.lastUpdateFragmentBtn;

        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        divider.setDividerColor(Color.TRANSPARENT);
        divider.setDividerThickness(20);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        rvMusica.setLayoutManager(layoutManager);
        rvMusica.addItemDecoration(divider);
        rvMusica.setAdapter(adapter);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollListener.resetState();
                loadMore();
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMore();
            }
        };

        rvMusica.addOnScrollListener(scrollListener);

        if (OCRemixPlayerApplication.mInstance.getGlobal().getListaUltimasMusicas().isEmpty())
            loadMore();
        else
            adapter.setAll(OCRemixPlayerApplication.mInstance.getGlobal().getListaUltimasMusicas());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadMore() {
        loadingVisivel(true);
        new GetLastSongs(getActivity().getApplication()).execute(new GetLastSongs.Response() {
            @Override
            public void onInit() {
                System.out.println("LastUpdatesFragment ==> iniciando consulta");
                layoutError.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(List<ResultItemMusic> result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addAll(result);
                        loadingVisivel(false);
                    }
                });
            }

            @Override
            public void onError(String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layoutError.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        scrollListener.resetState();
                        loadingVisivel(false);
                    }
                });
            }
        });
    }

    private void loadingVisivel(boolean visivel) {
        if (visivel) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) progressBar.getLayoutParams();
            if(rvMusica.getAdapter().getItemCount() == 0){
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            }else{
                layoutParams.removeRule(RelativeLayout.CENTER_IN_PARENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        OCRemixPlayerApplication.mInstance.getGlobal().setPositionListaAtualizacoes(layoutManager.findFirstCompletelyVisibleItemPosition());
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (OCRemixPlayerApplication.mInstance.getGlobal().getPositionListaAtualizacoes() != -1) {
            layoutManager.scrollToPosition(OCRemixPlayerApplication.mInstance.getGlobal().getPositionListaAtualizacoes());
        }
    }
}
