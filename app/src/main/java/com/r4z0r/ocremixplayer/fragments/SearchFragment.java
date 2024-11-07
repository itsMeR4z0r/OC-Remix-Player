package com.r4z0r.ocremixplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.adapters.JogoAdapter;
import com.r4z0r.ocremixplayer.adapters.MusicaAdapter;
import com.r4z0r.ocremixplayer.components.RvMusica;
import com.r4z0r.ocremixplayer.databinding.SearchFragmentBinding;
import com.r4z0r.ocremixplayer.tasks.SearchByGame;
import com.r4z0r.ocremixplayer.tasks.interfaces.ResponseResultItemGame;
import com.r4z0r.ocremixplayer.tasks.interfaces.ResponseResultItemMusic;
import com.r4z0r.ocremixplayer.tasks.SearchByTitle;

import org.r4z0r.models.ResultItemGame;
import org.r4z0r.models.ResultItemMusic;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private SearchFragmentBinding binding;
    private RvMusica rvMusica;
    private ProgressBar progressBar;
    private MaterialButton btnSearch;
    private TextInputEditText textInputEditText;
    private MaterialRadioButton rbJogo, rbTitulo;
    private Bundle savedInstanceState;

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

        btnSearch.setOnClickListener(view1 -> loadMore());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OCRemixPlayerApplication.mInstance.getGlobal().getListaPesquisaMusica().isEmpty()) {
            rvMusica.setAdapter(new MusicaAdapter(getContext(), OCRemixPlayerApplication.mInstance.getGlobal().getListaPesquisaMusica()));
        } else if (!OCRemixPlayerApplication.mInstance.getGlobal().getListaPesquisaJogo().isEmpty()) {
            rvMusica.setAdapter(new JogoAdapter(getContext(), OCRemixPlayerApplication.mInstance.getGlobal().getListaPesquisaJogo()));
        }

        if (OCRemixPlayerApplication.mInstance.getGlobal().getSearchBundle() != null) {
            savedInstanceState = OCRemixPlayerApplication.mInstance.getGlobal().getSearchBundle();

            rbTitulo.setChecked(savedInstanceState.getBoolean(String.valueOf(rbTitulo.getId())));
            rbJogo.setChecked(savedInstanceState.getBoolean(String.valueOf(rbJogo.getId())));
            textInputEditText.setText(savedInstanceState.getString(String.valueOf(textInputEditText.getId())));
        }

        if (rvMusica.getLayoutManager() != null)
            rvMusica.scrollToPosition(OCRemixPlayerApplication.mInstance.getGlobal().getPositionListaPesquisa());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (rbTitulo.isChecked()) {
            OCRemixPlayerApplication.mInstance.getGlobal().getListaPesquisaJogo().clear();
        }

        if (rbJogo.isChecked()) {
            OCRemixPlayerApplication.mInstance.getGlobal().getListaPesquisaMusica().clear();
        }

        savedInstanceState = new Bundle();
        savedInstanceState.putBoolean(String.valueOf(rbJogo.getId()), rbJogo.isChecked());
        savedInstanceState.putBoolean(String.valueOf(rbTitulo.getId()), rbTitulo.isChecked());
        savedInstanceState.putString(String.valueOf(textInputEditText.getId()), textInputEditText.getText().toString());

        OCRemixPlayerApplication.mInstance.getGlobal().setSearchBundle(savedInstanceState);

        if (rvMusica.getLayoutManager() != null)
            OCRemixPlayerApplication.mInstance.getGlobal().setPositionListaPesquisa(((LinearLayoutManager) rvMusica.getLayoutManager()).findFirstCompletelyVisibleItemPosition());

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
                    if (getActivity() != null)
                        getActivity().runOnUiThread(
                                () -> {
                                    loadingVisivel(true);
                                }
                        );
                }

                @Override
                public void onSuccess(List<ResultItemMusic> result) {
                    if (getActivity() != null)
                        getActivity()
                                .runOnUiThread(() -> {
                                    rvMusica.setAdapter(new MusicaAdapter(getContext(), result));
                                    loadingVisivel(false);
                                });
                }

                @Override
                public void onError(String error) {
                    if (getActivity() != null)
                        getActivity()
                                .runOnUiThread(() -> loadingVisivel(false));
                }
            }, textInputEditText.getText().toString());
        } else if (rbJogo.isChecked()) {
            new SearchByGame(OCRemixPlayerApplication.mInstance).execute(new ResponseResultItemGame() {
                @Override
                public void onInit() {
                    if (getActivity() != null)
                        getActivity().runOnUiThread(
                                () -> {
                                    loadingVisivel(true);
                                }
                        );
                }

                @Override
                public void onSuccess(List<ResultItemGame> result) {
                    if (getActivity() != null)
                        getActivity()
                                .runOnUiThread(() -> {
                                    rvMusica.setAdapter(new JogoAdapter(getContext(), result));
                                    loadingVisivel(false);
                                });
                }

                @Override
                public void onError(String error) {
                    if (getActivity() != null)
                        getActivity()
                                .runOnUiThread(() -> loadingVisivel(false));
                }
            }, textInputEditText.getText().toString());
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
