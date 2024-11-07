package com.r4z0r.ocremixplayer.adapters;

import static com.r4z0r.ocremixplayer.Constants.URL_BASE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.R;
import com.r4z0r.ocremixplayer.converter.MusicaConverter;
import com.r4z0r.ocremixplayer.models.Jogo;
import com.r4z0r.ocremixplayer.models.Musica;
import com.r4z0r.ocremixplayer.viewHolders.MusicaViewHolder;

import org.r4z0r.models.ArtistItem;
import org.r4z0r.models.ResultItemGame;
import org.r4z0r.models.ResultItemMusic;

import java.util.List;

public class JogoAdapter extends RecyclerView.Adapter<MusicaViewHolder> {
    private static final String TAG = "JogoAdapter";
    private Context mContext;
    private List<ResultItemGame> mJogoList;

    public JogoAdapter(Context context, List<ResultItemGame> gameList) {
        mContext = context;
        mJogoList = gameList;
    }

    public void addAll(List<ResultItemGame> gameList) {
        int positionStart = getItemCount();
        mJogoList.addAll(gameList);
        OCRemixPlayerApplication.mInstance.getGlobal().getListaPesquisaJogo().addAll(gameList);
        notifyItemRangeInserted(positionStart, gameList.size());
    }

    public void setAll(List<ResultItemGame> gameList) {
        mJogoList.clear();
        mJogoList.addAll(gameList);
    }

    @NonNull
    @Override
    public MusicaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_musica, parent, false);
        return new MusicaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicaViewHolder holder, int position) {
        ResultItemGame jogo = mJogoList.get(position);

        holder.title.setText(jogo.getGameTitle().replace("\"", "").trim());
        holder.artist.setText(jogo.getSystem());

        Glide
                .with(mContext)
                .load(URL_BASE + jogo.getSystemImage())
                .placeholder(R.drawable.baseline_games_24)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mJogoList.size();
    }
}
