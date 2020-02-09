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

import com.miftahjuanda.movies.Adapter.TvAdapterFav;
import com.miftahjuanda.movies.Helper.RealmHelper;
import com.miftahjuanda.movies.Helper.RecyclerItemTouchHelperTv;
import com.miftahjuanda.movies.Model.FavoriteTv;
import com.miftahjuanda.movies.R;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTabFavTv extends Fragment implements RecyclerItemTouchHelperTv.RecyclerItemTouchHelperListener {

    private View rootView;
    private RealmHelper realmHelper;
    private LinearLayout emptystate;
    private RecyclerView dataTv;
    private TvAdapterFav tvAdapterFav;
    private ArrayList<FavoriteTv> favoriteList;
    private static String CATEGORY_INTENT_TV = "TV";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tab_fav_tv, container, false);
        initView();
        if (savedInstanceState != null){
            favoriteList = new ArrayList<>(Objects.requireNonNull(savedInstanceState.getParcelableArrayList(CATEGORY_INTENT_TV)));
            tvAdapterFav.addAll(favoriteList);
            savedInstanceState.putParcelableArrayList(CATEGORY_INTENT_TV,favoriteList);

            emptystate.setVisibility(View.GONE);
        }else{
            loadData();
        }
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelperTv(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(dataTv);

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
        dataTv = rootView.findViewById(R.id.dataMovieFav);

        tvAdapterFav = new TvAdapterFav(getContext());

        @SuppressLint("WrongConstant")
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        dataTv.setLayoutManager(gridLayoutManager);
        dataTv.setHasFixedSize(true);
        dataTv.setAdapter(tvAdapterFav);
    }

    private void loadData(){
        tvAdapterFav.clear();
        favoriteList = new ArrayList<>(realmHelper.getFavoritListTv());
        /*for (FavoriteTv member : favoriteList){
            Log.i("Member name: ", member.getTitle());
        }*/

        if (favoriteList.size()==0){
            emptystate.setVisibility(View.VISIBLE);
        }else{
            tvAdapterFav.addAll(favoriteList);
            emptystate.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof TvAdapterFav.MovieViewHolder) {
            tvAdapterFav.remove(viewHolder.getAdapterPosition());
            realmHelper.deleteFavoriteTv(favoriteList.get(position).getId());
            loadData();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(CATEGORY_INTENT_TV,favoriteList);
        super.onSaveInstanceState(outState);
    }
}