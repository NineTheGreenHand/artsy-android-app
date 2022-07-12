package com.hw9.artsyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context context;
    private List<FavoriteItem> favoriteItemList;

    public FavoriteAdapter(List<FavoriteItem> favoriteItemList, Context context) {
        this.context = context;
        this.favoriteItemList = favoriteItemList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.favorite_item, null);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.tv_name.setText(favoriteItemList.get(position).getFavName());
        holder.tv_nationality.setText(favoriteItemList.get(position).getFavNationality());
        holder.tv_birthday.setText(favoriteItemList.get(position).getFavBirthday());
        holder.tv_id.setText(favoriteItemList.get(position).getFavId());
    }

    @Override
    public int getItemCount() {
        return favoriteItemList == null ? 0 : favoriteItemList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_nationality;
        private TextView tv_birthday;
        private TextView tv_id;
        private Button fav_button;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.fav_name);
            tv_nationality = itemView.findViewById(R.id.fav_nationality);
            tv_birthday = itemView.findViewById(R.id.fav_birthday);
            tv_id = itemView.findViewById(R.id.fav_id);
            fav_button = itemView.findViewById(R.id.fav_button);

            // Define click event for the button at the favorite section:
            fav_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (buttonClickListener != null) {
                        buttonClickListener.buttonClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    private FavoriteAdapter.ButtonClickListener buttonClickListener;

    public void setButtonClickListener(FavoriteAdapter.ButtonClickListener listener) {
        buttonClickListener = listener;
    }

    public interface ButtonClickListener {
        void buttonClick(int position);
    }
}