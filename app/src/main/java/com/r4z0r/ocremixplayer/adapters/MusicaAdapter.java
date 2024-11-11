package com.r4z0r.ocremixplayer.adapters;

import static com.r4z0r.ocremixplayer.Constants.URL_BASE;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.R;
import com.r4z0r.ocremixplayer.converter.MusicaConverter;
import com.r4z0r.ocremixplayer.models.Musica;
import com.r4z0r.ocremixplayer.tasks.GetRemix;
import com.r4z0r.ocremixplayer.tasks.interfaces.ResponseSongInfor;
import com.r4z0r.ocremixplayer.viewHolders.MusicaViewHolder;

import org.r4z0r.models.ArtistItem;
import org.r4z0r.models.ResultItemMusic;
import org.r4z0r.models.SongInfor;

import java.util.List;

public class MusicaAdapter extends RecyclerView.Adapter<MusicaViewHolder> {
    private static final String TAG = "ItemMusicAdapter";
    private Context mContext;
    private List<Musica> mMusicList;

    public MusicaAdapter(Context context, List<ResultItemMusic> musicList) {
        mContext = context;
        mMusicList = MusicaConverter.converterResultItem(musicList);
    }

    @OptIn(markerClass = UnstableApi.class)
    public void addAll(List<ResultItemMusic> musicList, boolean atualizacao) {
        int positionStart = getItemCount();
        mMusicList.addAll(MusicaConverter.converterResultItem(musicList));

        if (atualizacao)
            OCRemixPlayerApplication.mInstance.getGlobal().getListaUltimasMusicas().addAll(musicList);
        else
            OCRemixPlayerApplication.mInstance.getGlobal().getListaPesquisaMusica().addAll(0, musicList);

        notifyItemRangeInserted(positionStart, musicList.size());
    }

    public void setAll(List<ResultItemMusic> musicList) {
        mMusicList.clear();
        mMusicList.addAll(MusicaConverter.converterResultItem(musicList));
    }

    @NonNull
    @Override
    public MusicaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_musica, parent, false);
        return new MusicaViewHolder(view);
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onBindViewHolder(@NonNull MusicaViewHolder holder, int position) {
        Musica musica = mMusicList.get(position);

        holder.title.setText(musica.getName().replace("\"", "").trim());

        String artist = "";
        for (ArtistItem artistItem : musica.getArtistItemList()) {
            artist += artistItem.getName() + ", ";
        }
        artist = artist.substring(0, artist.length() - 2);
        holder.artist.setText(artist);

        Glide.with(mContext).load(URL_BASE + musica.getGameImageUrl()).placeholder(R.drawable.baseline_games_24).centerCrop().into(holder.imageView);

        holder.relativeLayout.setOnClickListener(view -> {
            new GetRemix(OCRemixPlayerApplication.mInstance).execute(new ResponseSongInfor() {
                @Override
                public void onInit() {

                }

                @Override
                public void onSuccess(SongInfor songInfor) {
                    Handler handler = new Handler(OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().getApplicationLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Uri uri = Uri.parse(songInfor.getDownloadList().get(0));
                            MediaItem mediaItem = MediaItem.fromUri(uri);
                            OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().setMediaItem(mediaItem);
                            OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().prepare();
                            OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().setPlayWhenReady(true);

                        }
                    });
                }

                @Override
                public void onError(String error) {

                }
            }, musica.getUrl());
        });
    }

    @Override
    public int getItemCount() {
        return mMusicList.size();
    }
}
