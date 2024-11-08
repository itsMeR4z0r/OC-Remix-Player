package com.r4z0r.ocremixplayer.fragments;

import android.animation.Animator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.R;
import com.r4z0r.ocremixplayer.databinding.SongInfoFragmentBinding;
import com.r4z0r.ocremixplayer.models.Musica;

import org.r4z0r.models.ArtistItem;
import org.r4z0r.models.SongItem;

public class SongInfoFragment extends Fragment {
    private SongInfoFragmentBinding binding;

    private ExtendedFloatingActionButton fabOpcoes;
    private RelativeLayout layoutOpcoes;

    private Musica musica;
    private ScrollView scrollView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = SongInfoFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        fabOpcoes = binding.songInfoFabOpcoes;
        layoutOpcoes = view.findViewById(R.id.song_info_rl_opcoes);
        scrollView = binding.songInfoScrollView;
        if (OCRemixPlayerApplication.mInstance.getGlobal().getBundle() != null
                && OCRemixPlayerApplication.mInstance.getGlobal().getBundle().containsKey("musica")
                && OCRemixPlayerApplication.mInstance.getGlobal().getBundle().getSerializable("musica") != null) {
            musica = (Musica) OCRemixPlayerApplication.mInstance.getGlobal().getBundle().getSerializable("musica");
        } else {
            OCRemixPlayerApplication.mInstance.getGlobal().getNavController().popBackStack();
        }

        fabOpcoes.setOnClickListener(view1 -> showOpcoes(!layoutOpcoes.isShown()));

        binding.songInfoTituloTextView.setText(musica.getName());

        for (ArtistItem artista : musica.getArtistItemList()) {
            Chip chip = new Chip(getContext());
            chip.setId(View.generateViewId());
            chip.setText(artista.getName());
            binding.songInfoChipGroup.addView(chip);
        }

        for (SongItem.OriginalSong originalSong : musica.getOriginalSongList()) {
            Chip chip = new Chip(getContext());
            chip.setId(View.generateViewId());
            chip.setText(originalSong.getName());
            binding.songInfoCgMusicasOriginais.addView(chip);
        }

        Chip chip = new Chip(getContext());
        chip.setId(View.generateViewId());
        chip.setText(musica.getGameTitle());
        binding.songInfoCgJogos.addView(chip);
    }


    private void showOpcoes(boolean show) {
        if (!show) {
            layoutOpcoes.animate()
                    .translationY(layoutOpcoes.getHeight())
                    .alpha(0)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(@NonNull Animator animator) {
                            layoutOpcoes.setVisibility(View.VISIBLE);
                            scrollView.setEnabled(true);
                            scrollView.animate().alpha(1F);
                        }

                        @Override
                        public void onAnimationEnd(@NonNull Animator animator) {
                            layoutOpcoes.setVisibility(View.GONE);
                            fabOpcoes.setIcon(getResources().getDrawable(R.drawable.baseline_keyboard_arrow_up_24, getContext().getTheme()));
                        }

                        @Override
                        public void onAnimationCancel(@NonNull Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(@NonNull Animator animator) {

                        }
                    });
        } else {
            layoutOpcoes.animate()
                    .translationY(0)
                    .alpha(1)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(@NonNull Animator animator) {
                            layoutOpcoes.setVisibility(View.VISIBLE);
                            scrollView.setEnabled(false);
                            scrollView.animate().alpha(0.2F);
                        }

                        @Override
                        public void onAnimationEnd(@NonNull Animator animator) {
                            fabOpcoes.setIcon(getResources().getDrawable(R.drawable.baseline_keyboard_arrow_down_24, getContext().getTheme()));
                        }

                        @Override
                        public void onAnimationCancel(@NonNull Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(@NonNull Animator animator) {

                        }
                    });
        }
    }
}
