package com.miftahjuanda.movies.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import com.miftahjuanda.movies.Adapter.TvAdapter;
import com.miftahjuanda.movies.Helper.LoadMore;
import com.miftahjuanda.movies.Model.Tv;
import com.miftahjuanda.movies.Model.TvData;
import com.miftahjuanda.movies.R;

import java.net.SocketTimeoutException;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTvShow extends Fragment implements TvAdapter.OnTvItemSelectedListener {

    private View rootView;
    private RecyclerView dataTvShow;
    private GridLayoutManager gridLayoutManager;
    private TvAdapter tvAdapter;
    private SwipeRefreshLayout refreshLayout;
    private int page = 1;
    public static String CATEGORY_INTENT_TV = "TV";
    private LoadMore loadMore;
    private Tv tv;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tv_show, container, false);
        initView();
        if (savedInstanceState != null){
            tv = savedInstanceState.getParcelable(CATEGORY_INTENT_TV);
            tvAdapter.addAll(tv != null ? tv.getResults() : null);
            savedInstanceState.putParcelable(CATEGORY_INTENT_TV,tv);
            addScroll();
        }else{
            loadData();
            addScroll();
        }

        return rootView;
    }

    @SuppressLint("WrongConstant")
    private void initView(){
        refreshLayout = rootView.findViewById(R.id.refresh);
        dataTvShow = rootView.findViewById(R.id.dataTvShow);

        tvAdapter = new TvAdapter(getContext());
        tvAdapter.setOnTvItemSelectedListener(this);
        gridLayoutManager = new GridLayoutManager(getContext(), 1,GridLayoutManager.VERTICAL, false);
        dataTvShow.setLayoutManager(gridLayoutManager);
        dataTvShow.setHasFixedSize(true);
        dataTvShow.setAdapter(tvAdapter);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        setHasOptionsMenu(true);
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
                tvAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                tvAdapter.getFilter().filter(s);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void refreshData() {
        if(tvAdapter != null) {
            tvAdapter.clear();
        }
        page = 1;

        removeScroll();
        addScroll();

        loadData();
    }

    private void removeScroll() {
        dataTvShow.removeOnScrollListener(loadMore);
    }

    private void addScroll() {
        loadMore = new LoadMore(gridLayoutManager, page) {
            @Override
            public void onLoadMore(int next) {
                page = next;
                loadData();
            }
        };

        dataTvShow.addOnScrollListener(loadMore);
    }

    private void loadData(){
        Random r = new Random();
        page = r.nextInt(100 - 25) + 65;
        if (refreshLayout != null) {
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(true);
                }
            });
        }
        ApiService apiService = new ApiService();
        apiService.getPopularTvShow(page, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                tv = (Tv) response.body();

                if(tv != null) {
                    if(tvAdapter != null) {
                        tvAdapter.addAll(tv.getResults());
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
    public void onItemClick(View v, TvData tvData, int position) {
        Intent detailMovie = new Intent(getActivity(), DetailMovieActivity.class);
        detailMovie.putExtra(DetailMovieActivity.EXTRA_TVSHOW, tvAdapter.getItem(position));
        detailMovie.putExtra(DetailMovieActivity.EXTRA_CATEGORY,CATEGORY_INTENT_TV);
        startActivity(detailMovie);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CATEGORY_INTENT_TV, tv);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnSetting) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}
