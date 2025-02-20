package com.r4z0r.ocremixplayer.adapters;

import static com.r4z0r.ocremixplayer.Constants.URL_BASE;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.widget.PopupMenu;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.R;
import com.r4z0r.ocremixplayer.converter.MusicaConverter;
import com.r4z0r.ocremixplayer.db.ObjectBox;
import com.r4z0r.ocremixplayer.db.boxes.RemixBox;
import com.r4z0r.ocremixplayer.db.models.RemixObj;
import com.r4z0r.ocremixplayer.models.Musica;
import com.r4z0r.ocremixplayer.tasks.GetRemix;
import com.r4z0r.ocremixplayer.tasks.interfaces.ResponseSongInfor;
import com.r4z0r.ocremixplayer.viewHolders.MusicaViewHolder;

import com.r4z0r.ocremixplayer.wrapper.models.ArtistItem;
import com.r4z0r.ocremixplayer.wrapper.models.ResultItemMusic;
import com.r4z0r.ocremixplayer.wrapper.models.SongInfor;

import java.util.ArrayList;
import java.util.List;

public class MusicaAdapter extends RecyclerView.Adapter<MusicaViewHolder> {
    private static final String TAG = "ItemMusicAdapter";
    private Context mContext;
    private Activity mActivity;
    private List<Musica> mMusicList;

    public MusicaAdapter(Context context, Activity activity, List<ResultItemMusic> musicList) {
        mContext = context;
        mActivity = activity;
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

    private void setLoading(boolean carregando, MusicaViewHolder holder) {
        if (carregando) {
            holder.progressBar.setVisibility(View.VISIBLE);
        } else {
            holder.progressBar.setVisibility(View.GONE);
        }
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
            RemixBox remixBox = OCRemixPlayerApplication.mInstance.getGlobal().getRemixBox();
            String idRemix = musica.getUrl().substring(musica.getUrl().lastIndexOf("/") + 1);
            if (remixBox.containsId(idRemix)) {
                Uri uri = Uri.parse(remixBox.get(idRemix).urlSong);
                MediaItem mediaItem = MediaItem.fromUri(uri);
                OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().setMediaItem(mediaItem);
                OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().prepare();
                OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().setPlayWhenReady(true);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setLoading(false, holder);
                    }
                });
            } else {
                new GetRemix(OCRemixPlayerApplication.mInstance).execute(new ResponseSongInfor() {
                    @Override
                    public void onInit() {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setLoading(true, holder);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(SongInfor songInfor) {
                        RemixObj remixObj = new RemixObj();
                        remixObj.remixId = idRemix;
                        remixObj.gameImageUrl = musica.getGameImageUrl();
                        remixObj.name = musica.getName();
                        remixObj.urlSong = songInfor.getDownloadList().get(0);
                        remixObj.artists = new ArrayList<>();

                        for(ArtistItem artistItem : musica.getArtistItemList()){
                            remixObj.artists.add(artistItem.getName());
                        }

                        remixObj.gameName = musica.getGameTitle();
                        remixObj.gameUrl = musica.getGameUrl();

                        remixBox.put(remixObj);

                        Handler handler = new Handler(OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().getApplicationLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Uri uri = Uri.parse(songInfor.getDownloadList().get(0));
                                MediaItem mediaItem = MediaItem.fromUri(uri);
                                OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().setMediaItem(mediaItem);
                                OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().prepare();
                                OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().setPlayWhenReady(true);
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setLoading(false, holder);
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setLoading(false, holder);
                                Toast.makeText(holder.relativeLayout.getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, musica.getUrl());
            }
        });


        holder.moreButton.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(mContext, holder.moreButton);
            popup.getMenuInflater().inflate(R.menu.menu_item_musica, popup.getMenu());

            popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) item -> {
                if (item.getItemId() == R.id.menu_item_musica_baixar) {
                    return true;
                } else if (item.getItemId() == R.id.menu_item_musica_compartilhar) {
                    return true;
                } else if (item.getItemId() == R.id.menu_item_musica_detalhes) {
                    OCRemixPlayerApplication.mInstance.getGlobal().getNavController().navigate(R.id.songInfoFragment);
                    OCRemixPlayerApplication.mInstance.getGlobal().getBundle().putSerializable("musica", musica);
                    return true;
                } else {
                    return false;
                }
            });

            popup.setForceShowIcon(true);
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return mMusicList.size();
    }
}
