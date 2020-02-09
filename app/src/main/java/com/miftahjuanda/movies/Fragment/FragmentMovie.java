package com.miftahjuanda.movies.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.miftahjuanda.movies.API.ApiService;
import com.miftahjuanda.movies.Activity.DetailMovieActivity;
import com.miftahjuanda.movies.Activity.SettingActivity;
import com.miftahjuanda.movies.Adapter.MovieAdapter;
import com.miftahjuanda.movies.Helper.LoadMore;
import com.miftahjuanda.movies.Model.Movie;
import com.miftahjuanda.movies.Model.MovieData;
import com.miftahjuanda.movies.R;

import java.net.SocketTimeoutException;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMovie extends Fragment implements MovieAdapter.OnMovieItemSelectedListener {

    private View rootView;
    private RecyclerView dataMovie;
    private GridLayoutManager gridLayoutManager;
    private MovieAdapter movieAdapter;
    private SwipeRefreshLayout refreshLayout;
    private int page = 1;
    public static String CATEGORY_INTENT_MOVIE = "MOVIE";
    private LoadMore loadMore;
    private Movie movie;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        initView();
        if (savedInstanceState != null){
            movie = savedInstanceState.getParcelable(CATEGORY_INTENT_MOVIE);
            movieAdapter.addAll(movie != null ? movie.getResults() : null);
            savedInstanceState.putParcelable(CATEGORY_INTENT_MOVIE,movie);
            addScroll();
        }else{
            loadData();
            addScroll();
        }
        setHasOptionsMenu(true);
        return rootView;
    }

    @SuppressLint("WrongConstant")
    private void initView(){
        refreshLayout = rootView.findViewById(R.id.refresh);
        refreshLayout.setEnabled(false);
        refreshLayout.setRefreshing(false);
        dataMovie = rootView.findViewById(R.id.dataMovie);

        movieAdapter = new MovieAdapter(getContext());
        movieAdapter.setOnMovieItemSelectedListener(this);
        gridLayoutManager = new GridLayoutManager(getContext(), 1,GridLayoutManager.VERTICAL, false);
        dataMovie.setLayoutManager(gridLayoutManager);
        dataMovie.setHasFixedSize(true);
        dataMovie.setAdapter(movieAdapter);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        if(movieAdapter != null) {
            movieAdapter.clear();
        }
        page = 1;

        removeScroll();
        addScroll();

        loadData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Cari");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                movieAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                movieAdapter.getFilter().filter(s);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void removeScroll() {
        dataMovie.removeOnScrollListener(loadMore);
    }

    private void addScroll() {
        loadMore = new LoadMore(gridLayoutManager, page) {
            @Override
            public void onLoadMore(int next) {
                page = next;
                loadData();
            }
        };

        dataMovie.addOnScrollListener(loadMore);
    }

    private void loadData(){
        Random r = new Random();
        page = r.nextInt(40 - 20) + 15;
        if (refreshLayout != null) {
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(true);
                }
            });
        }
        ApiService apiService = new ApiService();
        apiService.getPopularMovies(page, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                movie = (Movie) response.body();
                if(movie != null) {
                    if(movieAdapter != null) {
                        movieAdapter.addAll(movie.getResults());
                    }
                }else{
                    Toast.makeText(getContext(), getString(R.string.no_data_text), Toast.LENGTH_LONG).show();
                }

                if (refreshLayout != null)
                    refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                if(t instanceof SocketTimeoutException) {
                    Toast.makeText(getContext(), getString(R.string.message_failed), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), getString(R.string.message_connection_error), Toast.LENGTH_LONG).show();
                }

                if (refreshLayout != null)
                    refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onItemClick(View v, MovieData movieData, int position) {
        Intent detailMovie = new Intent(getActivity(), DetailMovieActivity.class);
        detailMovie.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieAdapter.getItem(position));
        detailMovie.putExtra(DetailMovieActivity.EXTRA_CATEGORY,CATEGORY_INTENT_MOVIE);
        startActivity(detailMovie);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CATEGORY_INTENT_MOVIE, movie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnSetting) {
            Intent mIntent = new Intent(getActivity(), SettingActivity.class);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
