package com.miftahjuanda.movies.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.miftahjuanda.movies.Adapter.MovieAdapterFav;
import com.miftahjuanda.movies.Helper.RealmHelper;
import com.miftahjuanda.movies.Helper.RecyclerItemTouchHelper;
import com.miftahjuanda.movies.Model.Favorite;
import com.miftahjuanda.movies.R;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTabFavMovie extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private View rootView;
    private RecyclerView dataMovie;
    private MovieAdapterFav movieAdapter;
    private static String CATEGORY_INTENT_MOVIE = "MOVIE";
    private RealmHelper realmHelper;
    private ArrayList<Favorite> favoriteList;
    private LinearLayout emptystate;

    @SuppressLint("CutPasteId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tab_fav_movie, container, false);

        initView();

        if (savedInstanceState != null){
            favoriteList = new ArrayList<>(Objects.requireNonNull(savedInstanceState.getParcelableArrayList(CATEGORY_INTENT_MOVIE)));
            movieAdapter.addAll(favoriteList);
            savedInstanceState.putParcelableArrayList(CATEGORY_INTENT_MOVIE,favoriteList);

            emptystate.setVisibility(View.GONE);
        }else{
            loadData();
        }

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(dataMovie);

        return rootView;
    }


    private void initView(){

        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        Realm realm = Realm.getInstance(configuration);
        realmHelper = new RealmHelper(realm);

        SwipeRefreshLayout refreshLayout = rootView.findViewById(R.id.refresh);
        emptystate = rootView.findViewById(R.id.emptystate);
        refreshLayout.setEnabled(false);
        refreshLayout.setRefreshing(false);
        dataMovie = rootView.findViewById(R.id.dataMovieFav);

        movieAdapter = new MovieAdapterFav(getContext());

        @SuppressLint("WrongConstant")
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        dataMovie.setLayoutManager(gridLayoutManager);
        dataMovie.setHasFixedSize(true);
        dataMovie.setAdapter(movieAdapter);
    }

    private void loadData(){
        movieAdapter.clear();
        favoriteList = new ArrayList<>(realmHelper.getFavoritList());
        if (favoriteList.size()==0){
            emptystate.setVisibility(View.VISIBLE);
        }else{
            movieAdapter.addAll(favoriteList);
            emptystate.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof MovieAdapterFav.MovieViewHolder) {
            movieAdapter.remove(viewHolder.getAdapterPosition());
            realmHelper.deleteFavorite(favoriteList.get(position).getId());
            loadData();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(CATEGORY_INTENT_MOVIE,favoriteList);
        super.onSaveInstanceState(outState);
    }


}