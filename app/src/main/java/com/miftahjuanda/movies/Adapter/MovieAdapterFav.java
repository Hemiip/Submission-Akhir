package com.miftahjuanda.movies.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miftahjuanda.movies.API.Constant;
import com.miftahjuanda.movies.Model.Favorite;
import com.miftahjuanda.movies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapterFav extends RecyclerView.Adapter<MovieAdapterFav.MovieViewHolder> {

    private List<Favorite> favmovie;
    private List<Favorite> backup;
    private Context context;

    public MovieAdapterFav(Context context) {
        this.context = context;
        favmovie = new ArrayList<>();
        backup = new ArrayList<>();
    }

    private void add(Favorite item) {
        favmovie.add(item);
        backup.add(item);
        notifyItemInserted(favmovie.size() - 1);
        notifyDataSetChanged();
    }

    public void addAll(List<Favorite> favmovie) {
        for (Favorite favorite : favmovie) {
            add(favorite);
        }
        notifyDataSetChanged();
    }

    public void remove(int item) {
        favmovie.remove(item);
        notifyItemRemoved(item);
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem());
        }
    }

    private void remove(Favorite item) {
        int position = favmovie.indexOf(item);
        if (position > -1) {
            favmovie.remove(position);
            notifyItemRemoved(position);
        }
    }

    private Favorite getItem() {
        return favmovie.get(0);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_fav, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        final Favorite favorite = favmovie.get(position);
        holder.bind(favorite);
    }

    @Override
    public int getItemCount() {
        return favmovie.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView posterImage;
        private TextView posterTitle,
                posterReleaseDate,
                posterDescription;
        private RatingBar posterRating;
        public RelativeLayout viewForeground;


        @SuppressLint("CutPasteId")
        private MovieViewHolder(View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.posterImage);
            posterTitle = itemView.findViewById(R.id.posterTitle);
            posterRating = itemView.findViewById(R.id.rating);
            posterDescription = itemView.findViewById(R.id.posterDescription);
            posterReleaseDate = itemView.findViewById(R.id.posterReleaseDate);
            viewForeground = itemView.findViewById(R.id.view_foreground);

        }

        private void bind(Favorite favorite) {
            Picasso.get()
                    .load(Constant.IMG_URL + favorite.getPosterPath())
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(posterImage);
            posterTitle.setText(favorite.getTitle());
            posterRating.setRating((float) (favorite.getVoteAverage() / 2));
            posterDescription.setText(favorite.getOverview());
            posterReleaseDate.setText(String.format("%s %s",
                    context.getString(R.string.txt_detail_release_date), favorite.getReleaseDate()));
        }
    }


}