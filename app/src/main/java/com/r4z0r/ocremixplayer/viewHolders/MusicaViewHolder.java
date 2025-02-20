package com.r4z0r.ocremixplayer.viewHolders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.r4z0r.ocremixplayer.R;

public class MusicaViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView artist;
    public ImageButton moreButton;
    public RelativeLayout relativeLayout;
    public ImageView imageView;
    public ProgressBar progressBar;

    public MusicaViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.item_musica_search_title);
        artist = (TextView) itemView.findViewById(R.id.item_musica_search_artist);
        moreButton = (ImageButton) itemView.findViewById(R.id.item_musica_search_more);
        relativeLayout = (RelativeLayout) itemView.findViewById(R.id.item_musica_search_rl);
        imageView = (ImageView) itemView.findViewById(R.id.item_musica_search_iv);
        progressBar = (ProgressBar) itemView.findViewById(R.id.item_musica_pb);
    }

}
