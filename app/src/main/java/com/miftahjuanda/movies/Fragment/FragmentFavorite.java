package com.miftahjuanda.movies.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.miftahjuanda.movies.Activity.SettingActivity;
import com.miftahjuanda.movies.Adapter.TabAdapter;
import com.miftahjuanda.movies.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFavorite extends Fragment {

    private View rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        showToast();
        initView();
        setHasOptionsMenu(true);
        return rootView;
    }

    private void showToast() {
        LayoutInflater inflater1 = getLayoutInflater();
        View layout = inflater1.inflate(R.layout.custom_toast, rootView.findViewById(R.id.toast_layout));

        final Toast toast = new Toast(getActivity());
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 6000);
    }

    private void initView(){
        ViewPager viewPager = rootView.findViewById(R.id.viewPager);
        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);

        TabAdapter adapter = new TabAdapter(getFragmentManager());
        adapter.addFragment(new FragmentTabFavMovie(), "Movie");

        adapter.addFragment(new FragmentTabFavTv(), "Tv");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.action_search).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
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