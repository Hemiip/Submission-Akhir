package com.miftahjuanda.myfavorite.Fragment;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.miftahjuanda.myfavorite.Adapter.MovieAdapterFav;
import com.miftahjuanda.myfavorite.Model.Favorite;
import com.miftahjuanda.myfavorite.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMovie extends Fragment {

    private View rootView;
    private MovieAdapterFav movieAdapter;
    private LinearLayout emptystate;
    private static final String PROVIDER_NAME = "com.miftahjuanda.movies";
    private static final String URL = "content://" + PROVIDER_NAME;
    private static final Uri CONTENT_URI = Uri.parse(URL);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        initView();
        return rootView;
    }


    @SuppressLint("Recycle")
    private void loadData() {
        Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            movieAdapter.clear();
            ArrayList<Favorite> favoriteList = new ArrayList<>();
            String image, title, description, releasedata;
            double voteAverage;
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    image = cursor.getString(cursor.getColumnIndex("image"));
                    title = cursor.getString(cursor.getColumnIndex("title"));
                    releasedata = cursor.getString(cursor.getColumnIndex("releasedate"));
                    description = cursor.getString(cursor.getColumnIndex("description"));
                    voteAverage = cursor.getDouble(cursor.getColumnIndex("vote"));
                    Favorite favoriteTv = new Favorite(image, title, releasedata, description, voteAverage);
                    favoriteList.add(favoriteTv);
                    cursor.moveToNext();
                }
            }
            if (favoriteList.size() == 0) {
                emptystate.setVisibility(View.VISIBLE);
            } else {
                movieAdapter.addAll(favoriteList);
                emptystate.setVisibility(View.GONE);
            }
        } else {
            emptystate.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        SwipeRefreshLayout refreshLayout = rootView.findViewById(R.id.refresh);
        emptystate = rootView.findViewById(R.id.emptystate);
        refreshLayout.setEnabled(false);
        refreshLayout.setRefreshing(false);
        RecyclerView dataMovie = rootView.findViewById(R.id.dataMovieFav);

        movieAdapter = new MovieAdapterFav(getContext());

        @SuppressLint("WrongConstant")
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        dataMovie.setLayoutManager(gridLayoutManager);
        dataMovie.setHasFixedSize(true);
        dataMovie.setAdapter(movieAdapter);
        loadData();
    }
}
