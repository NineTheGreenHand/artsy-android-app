package com.hw9.artsyapp;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkAdapter.ArtworkViewHolder>{

    private ArtistArtwork[] artistArtworks;
    private Context context;

    public ArtworkAdapter(ArtistArtwork[] artistArtworks, Context context) {
        this.artistArtworks = artistArtworks;
        this.context = context;
    }

    @NonNull
    @Override
    public ArtworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.artwork_card_item, null);
        return new ArtworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtworkViewHolder holder, int position) {
        Picasso.get().load(artistArtworks[position].getArtworkPicURL()).into(holder.iv);
        holder.tv.setText(artistArtworks[position].getArtworkName());
    }

    @Override
    public int getItemCount() {
        return artistArtworks == null ? 0 : artistArtworks.length;
    }

    public class ArtworkViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tv;


        public ArtworkViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.artwork_pic);
            tv = itemView.findViewById(R.id.artwork_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRecyclerItemClickListener != null) {
                        onRecyclerItemClickListener.onRecyclerItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener listener) {
        onRecyclerItemClickListener = listener;
    }

    public interface OnRecyclerItemClickListener {
        void onRecyclerItemClick(int position);
    }
}