package com.hw9.artsyapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private ArtistCard[] cards;
    private Context context;

    public ArtistAdapter(ArtistCard[] cards, Context context) {
        this.cards = cards;
        this.context = context;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.artist_card_item, null);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        // If the pic is missing for some artists, use default artsy logo instead:
        if (Objects.equals(cards[position].getPicURL(), "./assets/images/artsy_logo.svg")) {
            Picasso.get().load(R.drawable.artsy_logo).into(holder.iv);
        } else {
            Picasso.get().load(cards[position].getPicURL()).into(holder.iv);
        }
        holder.tv.setText(cards[position].getName());
    }

    @Override
    public int getItemCount() {
        return cards == null ? 0 : cards.length;
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private TextView tv;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.artist_pic);
            tv = itemView.findViewById(R.id.artist_name);

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
