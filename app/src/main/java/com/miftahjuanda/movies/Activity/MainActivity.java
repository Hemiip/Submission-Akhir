package com.miftahjuanda.movies.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miftahjuanda.movies.Fragment.FragmentFavorite;
import com.miftahjuanda.movies.Fragment.FragmentMovie;
import com.miftahjuanda.movies.Fragment.FragmentTvShow;
import com.miftahjuanda.movies.R;

public class MainActivity extends AppCompatActivity {

    public TextView toolbarTitle, toolbarDescription;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        if (savedInstanceState==null){
            defaultFragment();
        }
    }

    private void initToolbar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.putih));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initComponent();

    }

    private void launchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.content, fragment, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void defaultFragment() {
        toolbarTitle.setText(getString(R.string.fragment_title_movie));
        toolbarDescription.setText(getString(R.string.fragment_description_movie));
        navigation.setSelectedItemId(R.id.navMovie);
        Fragment defaultfragment = new FragmentMovie();
        launchFragment(defaultfragment);
    }


    private void initComponent() {
        navigation = findViewById(R.id.bottomNav);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarDescription = findViewById(R.id.toolbarDescription);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navMovie:
                        toolbarTitle.setText(getString(R.string.fragment_title_movie));
                        toolbarDescription.setText(getString(R.string.fragment_description_movie));
                        Fragment movieFragment = new FragmentMovie();
                        launchFragment(movieFragment);
                        break;
                    case R.id.navTvShow:
                        toolbarTitle.setText(getString(R.string.fragment_title_tv_show));
                        toolbarDescription.setText(getString(R.string.fragment_description_tv_show));
                        Fragment tvshowFragment = new FragmentTvShow();
                        launchFragment(tvshowFragment);
                        break;
                    case R.id.navFav:
                        toolbarTitle.setText(getString(R.string.fragment_title_favorite));
                        toolbarDescription.setText(getString(R.string.fragment_description_favorite));
                        Fragment favorite = new FragmentFavorite();
                        launchFragment(favorite);
                        break;
                }

                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        int back = getSupportFragmentManager().getBackStackEntryCount();
        if (back >= 1) {
            ShowDialogExit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void ShowDialogExit() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(R.string.notificationTitle)
                .setMessage(R.string.notificationMessage)
                .setPositiveButton(R.string.notificationButtonPositive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.notificationButtonNegative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String SAVE = "SAVE";
        outState.putString(SAVE, SAVE);
        super.onSaveInstanceState(outState);
    }
}