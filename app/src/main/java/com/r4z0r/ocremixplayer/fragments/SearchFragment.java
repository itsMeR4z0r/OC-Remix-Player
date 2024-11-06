package com.r4z0r.ocremixplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.adapters.MusicaAdapter;
import com.r4z0r.ocremixplayer.databinding.SearchFragmentBinding;
import com.r4z0r.ocremixplayer.listeners.EndlessRecyclerViewScrollListener;
import com.r4z0r.ocremixplayer.tasks.ResponseResultItemMusic;
import com.r4z0r.ocremixplayer.tasks.SearchByTitle;

import org.r4z0r.models.ResultItemMusic;

import java.util.List;

public class SearchFragment extends Fragment {
    private SearchFragmentBinding binding;
    private RecyclerView rvMusica;
    private ProgressBar progressBar;
    private MaterialButton btnSearch;
    private TextInputEditText textInputEditText;
    private MusicaAdapter adapter;
    private LinearLayoutManager layoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    private MaterialRadioButton rbJogo, rbTitulo;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = SearchFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMusica = binding.searchFragmentRv;
        progressBar = binding.searchFragmentPb;
        btnSearch = binding.searchFragmentIbSearch;
        rbJogo = binding.searchFragmentRgFilterPorJogo;
        rbTitulo = binding.searchFragmentRgFilterPorTitulo;
        textInputEditText = binding.searchFragmentEtSearch;
        adapter = new MusicaAdapter(getContext(),)

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadMore() {
        if (rbTitulo.isChecked()) {
            new SearchByTitle(OCRemixPlayerApplication.mInstance).execute(new ResponseResultItemMusic() {
                @Override
                public void onInit() {
                    getActivity().runOnUiThread(
                            () -> loadingVisivel(true)
                    );
                }

                @Override
                public void onSuccess(List<ResultItemMusic> result) {
                    getActivity().runOnUiThread(
                            () -> {
                                adapter.setAll(result);
                                loadingVisivel(false);
                            });
                }

                @Override
                public void onError(String error) {
                    getActivity().runOnUiThread(
                            () -> loadingVisivel(false)
                    );
                }
            }, "titulo");
        } else {

        }
    }

    private void loadingVisivel(boolean visivel) {
        if (visivel) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
