package com.miftahjuanda.movies.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miftahjuanda.movies.API.Constant;
import com.miftahjuanda.movies.Model.TvData;
import com.miftahjuanda.movies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.MovieViewHolder> {

    private List<TvData> tvDatas;
    private List<TvData> backup;
    private List<TvData> tvDataSearch;
    private Context context;

    private OnTvItemSelectedListener onTvItemSelectedListener;

    public TvAdapter(Context context) {
        this.context = context;
        tvDatas = new ArrayList<>();
        tvDataSearch = new ArrayList<>();
        backup = new ArrayList<>();
    }

    private void add(TvData item) {
        tvDatas.add(item);
        backup.add(item);
        notifyItemInserted(tvDatas.size() - 1);
    }

    public void addAll(List<TvData> movieDatas) {
        for (TvData movieData : movieDatas) {
            add(movieData);
        }
    }

    private void remove(TvData item) {
        int position = tvDatas.indexOf(item);
        if (position > -1) {
            tvDatas.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public TvData getItem(int position) {
        return tvDatas.get(position);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tvshow, parent, false);
        final MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPos = movieViewHolder.getAdapterPosition();
                TvData tvData = tvDatas.get(adapterPos);
                if (onTvItemSelectedListener != null) {
                    onTvItemSelectedListener.onItemClick(movieViewHolder.itemView,tvData,adapterPos);
                }
            }
        });

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        final TvData tvData = tvDatas.get(position);
        holder.bind(tvData);
    }

    @Override
    public int getItemCount() {
        return tvDatas.size();
    }

    public void setOnTvItemSelectedListener(OnTvItemSelectedListener onTvItemSelectedListener) {
        this.onTvItemSelectedListener = onTvItemSelectedListener;
    }

    public interface OnTvItemSelectedListener {
        void onItemClick(View v, TvData tvData, int position);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView posterImage;
        private TextView posterTitle,
                posterDescription;
        private RatingBar posterRating;

        private MovieViewHolder(View itemView) {
            super(itemView);

            posterImage = itemView.findViewById(R.id.posterImage);
            posterTitle = itemView.findViewById(R.id.posterTitle);
            posterRating = itemView.findViewById(R.id.rating);
            posterDescription = itemView.findViewById(R.id.posterDescription);
        }

        private void bind(TvData tvData) {
            Picasso.get()
                    .load(Constant.IMG_URL + tvData.getPoster_path())
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(posterImage);
            posterTitle.setText(tvData.getName());
            posterRating.setRating((float) (tvData.getVote_average() / 2));
            posterDescription.setText(tvData.getOverview());

        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    tvDataSearch = backup;
                } else {
                    List<TvData> filteredList = new ArrayList<>();
                    for (TvData row : backup) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    tvDataSearch = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = tvDataSearch;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                tvDataSearch = (ArrayList<TvData>) filterResults.values;
                clear();
                tvDatas.addAll(tvDataSearch);
                notifyDataSetChanged();
            }
        };
    }
}
