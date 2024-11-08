package com.r4z0r.ocremixplayer.fragments;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.button.MaterialButton;
import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.adapters.MusicaAdapter;
import com.r4z0r.ocremixplayer.components.RvMusica;
import com.r4z0r.ocremixplayer.databinding.LastUpdatesFragmentBinding;
import com.r4z0r.ocremixplayer.tasks.GetLastSongs;
import com.r4z0r.ocremixplayer.tasks.interfaces.ResponseResultItemMusic;

import org.r4z0r.models.ResultItemMusic;

import java.util.ArrayList;
import java.util.List;

public class LastUpdatesFragment extends Fragment {
    private LastUpdatesFragmentBinding binding;
    private RvMusica rvMusica;
    private ProgressBar progressBar;
    private LinearLayout layoutError;
    private MaterialButton btnTryAgain;
    private MusicaAdapter adapter;

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

        btnTryAgain.setOnClickListener(view1 -> {
            getActivity().runOnUiThread(()->{
                rvMusica.getScrollListener().resetState();
            });
            loadMore();
        });

        rvMusica.setAdapter(adapter);
        rvMusica.setListener(this::loadMore);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadMore() {
        loadingVisivel(true);
        new GetLastSongs(OCRemixPlayerApplication.mInstance).execute(new ResponseResultItemMusic() {
            @Override
            public void onInit() {
                System.out.println("LastUpdatesFragment ==> iniciando consulta");
                layoutError.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(List<ResultItemMusic> result) {
                if (getActivity() != null)
                    getActivity().runOnUiThread(() -> {
                        adapter.addAll(result, true);
                        loadingVisivel(false);
                    });
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null)
                    getActivity().runOnUiThread(() -> {
                        layoutError.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        rvMusica.getScrollListener().resetState();
                        loadingVisivel(false);
                    });
            }
        });
    }

    private void loadingVisivel(boolean visivel) {
        if (visivel) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) progressBar.getLayoutParams();
            if (rvMusica.getAdapter() != null && rvMusica.getAdapter().getItemCount() == 0) {
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            } else {
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
        if (rvMusica.getLayoutManager() != null)
            OCRemixPlayerApplication.mInstance.getGlobal().setPositionListaAtualizacoes(((LinearLayoutManager) rvMusica.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (OCRemixPlayerApplication.mInstance.getGlobal().getListaUltimasMusicas().isEmpty())
            loadMore();
        else
            adapter.setAll(OCRemixPlayerApplication.mInstance.getGlobal().getListaUltimasMusicas());

        if (OCRemixPlayerApplication.mInstance.getGlobal().getPositionListaAtualizacoes() != -1 && rvMusica.getLayoutManager() != null) {
            rvMusica.getLayoutManager().scrollToPosition(OCRemixPlayerApplication.mInstance.getGlobal().getPositionListaAtualizacoes());
        }
    }
}
