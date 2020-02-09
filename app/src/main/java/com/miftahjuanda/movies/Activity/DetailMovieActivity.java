package com.miftahjuanda.movies.Activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.miftahjuanda.movies.API.ApiService;
import com.miftahjuanda.movies.API.Constant;
import com.miftahjuanda.movies.Adapter.MovieAdapter;
import com.miftahjuanda.movies.Adapter.TvAdapter;
import com.miftahjuanda.movies.Helper.RealmHelper;
import com.miftahjuanda.movies.Model.Favorite;
import com.miftahjuanda.movies.Model.FavoriteTv;
import com.miftahjuanda.movies.Model.Genre;
import com.miftahjuanda.movies.Model.Movie;
import com.miftahjuanda.movies.Model.MovieData;
import com.miftahjuanda.movies.Model.MovieDetail;
import com.miftahjuanda.movies.Model.Trailer;
import com.miftahjuanda.movies.Model.TrailerData;
import com.miftahjuanda.movies.Model.Tv;
import com.miftahjuanda.movies.Model.TvData;
import com.miftahjuanda.movies.Model.TvDetail;
import com.miftahjuanda.movies.R;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.miftahjuanda.movies.API.Constant.BASE_URL_YOUTUBE;
import static com.miftahjuanda.movies.API.Constant.DEFAULT_IMAGE;
import static com.miftahjuanda.movies.Fragment.FragmentMovie.CATEGORY_INTENT_MOVIE;
import static com.miftahjuanda.movies.Fragment.FragmentTvShow.CATEGORY_INTENT_TV;

public class DetailMovieActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";
    public static final String EXTRA_TVSHOW = "EXTRA_TVSHOW";
    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
    public static final String CATEGORY = "CATEGORY";
    public static final String TAG_MOVIE_DATA = "MOVIE_DATA";
    public static final String TAG_TV_DATA = "TV_DATA";
    public static final String TAG_MOVIE_DETAIL = "MOVIE_DETAIL";
    public static final String TAG_TV_DETAIL = "TV_DETAIL";
    public static final String TAG_TRAILER = "TRAILER";
    public static final String TAG_MOVIE = "MOVIE";
    public static final String TAG_TV = "TV";
    private TextView posterTitle,
            posterDuration,
            posterCategory,
            posterReleaseDate,
            posterRatingText,
            posterDescription;
    private RatingBar posterRating;
    private ImageView posterImage, posterCover;
    private RecyclerView recyclerView;
    private String Category;
    private Boolean clicked = true;
    private ApiService apiService;
    private LinearLayout viewTrailers;
    private MovieAdapter movieAdapter;
    private TvAdapter tvAdapter;
    int page = 1;
    private AppBarLayout appBarLayout;
    private ScrollView scrollView;
    private MovieData movieData;
    private MovieDetail movieDetail;
    private Trailer trailer;
    private Movie movie;
    private TvData tvData;
    private TvDetail tvDetail;
    private Tv tv;
    private SwipeRefreshLayout refreshLayout;
    private Realm realm;
    public Favorite favorite;
    public FavoriteTv favoriteTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        initView();

        Realm.init(DetailMovieActivity.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);


        if (savedInstanceState!=null){
            if (CATEGORY_INTENT_MOVIE.equals(savedInstanceState.getString(CATEGORY))){
                RestoreMovie(savedInstanceState);
            }else if (CATEGORY_INTENT_TV.equals(savedInstanceState.getString(CATEGORY))){
                RestoreTv(savedInstanceState);
            }
        }

        initToolbar();
    }

    @SuppressLint("DefaultLocale")
    private void RestoreTv(Bundle savedInstanceState){
        tv = savedInstanceState.getParcelable(TAG_TV);
        tvData = savedInstanceState.getParcelable(TAG_TV_DATA);
        tvDetail = savedInstanceState.getParcelable(TAG_TV_DETAIL);
        trailer = savedInstanceState.getParcelable(TAG_TRAILER);
        if (tvDetail != null) {
            for (int i = 0; i < tvDetail.getRuntime().size(); i++) {
                posterDuration.setText(String.format("%s %d %s",
                        getString(R.string.txt_detail_duration),
                        tvDetail.getRuntime().get(i),
                        getString(R.string.text_menit)));
            }
            for (int i = 0; i < tvDetail.getGenres().size(); i++) {
                Genre genre = tvDetail.getGenres().get(i);

                if (i < tvDetail.getGenres().size() - 1) {
                    posterCategory.append(" " + genre.getName() + ",");
                } else {
                    posterCategory.append(genre.getName());
                }
            }
        }
        if (trailer != null){
            showTrailers(trailer.getResults());
        }
        if (tvAdapter != null) {
            tvAdapter.addAll(tv.getResults());
            recyclerView.setAdapter(tvAdapter);
        }
    }

    @SuppressLint("DefaultLocale")
    private void RestoreMovie(Bundle savedInstanceState){
        movie = savedInstanceState.getParcelable(TAG_MOVIE);
        movieData = savedInstanceState.getParcelable(TAG_MOVIE_DATA);
        movieDetail = savedInstanceState.getParcelable(TAG_MOVIE_DETAIL);
        trailer = savedInstanceState.getParcelable(TAG_TRAILER);
        if (movieDetail != null) {
            posterDuration.setText(String.format("%s%d %s",
                    getString(R.string.txt_detail_duration),
                    movieDetail.getRuntime(),
                    getString(R.string.text_menit)));
            for (int i = 0; i < movieDetail.getGenres().size(); i++) {
                Genre genre = movieDetail.getGenres().get(i);

                if (i < movieDetail.getGenres().size() - 1) {
                    posterCategory.append(" " + genre.getName() + ",");
                } else {
                    posterCategory.append(genre.getName());
                }
            }
        }
        if (trailer != null){
            showTrailers(trailer.getResults());
        }
        if (movieAdapter != null) {
            movieAdapter.addAll(movie.getResults());
            recyclerView.setAdapter(movieAdapter);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Category.equals(CATEGORY_INTENT_MOVIE)) {
            outState.putString(CATEGORY,Category);
            outState.putParcelable(TAG_MOVIE_DATA,movieData);
            outState.putParcelable(TAG_MOVIE_DETAIL,movieDetail);
            outState.putParcelable(TAG_TRAILER,trailer);
            outState.putParcelable(TAG_MOVIE,movie);
        } else if (Category.equals(CATEGORY_INTENT_TV)) {
            outState.putString(CATEGORY,Category);
            outState.putParcelable(TAG_TV_DATA,tvData);
            outState.putParcelable(TAG_TV_DETAIL,tvDetail);
            outState.putParcelable(TAG_TRAILER,trailer);
            outState.putParcelable(TAG_TV,tv);
        }

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        scrollView = findViewById(R.id.scrollView);
        refreshLayout = findViewById(R.id.mainLayout);
        refreshLayout.setEnabled(false);
        refreshLayout.setRefreshing(false);
        appBarLayout = findViewById(R.id.app_bar_layout);
        toolbar.setNavigationIcon(R.drawable.back_button);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(null);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        initData();

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); // For ScrollView
                if (scrollY > 20){
                    appBarLayout.setBackgroundResource(R.color.colorPrimary);
                }else{
                    appBarLayout.setBackgroundResource(R.color.transparent);
                }
            }
        });

    }

    private void initView() {


        recyclerView = findViewById(R.id.dataMovie);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        posterImage = findViewById(R.id.posterImage);
        posterCover = findViewById(R.id.posterCover);
        viewTrailers = findViewById(R.id.view_trailers);
        posterDuration = findViewById(R.id.posterDuration);
        posterCategory = findViewById(R.id.posterCategoty);
        posterReleaseDate = findViewById(R.id.posterReleaseData);
        posterRating = findViewById(R.id.rating);
        posterRatingText = findViewById(R.id.posterRatingText);
        posterDescription = findViewById(R.id.posterDescription);
        posterTitle = findViewById(R.id.posterTitle);
        Category = getIntent().getStringExtra(EXTRA_CATEGORY);
        apiService = new ApiService();
        posterCategory.setText(getString(R.string.txt_detail_category));
        movieAdapter = new MovieAdapter(this);
        tvAdapter = new TvAdapter(this);

        ImageView btnPosterLike = findViewById(R.id.ivPosterLike);
        btnPosterLike.setOnClickListener(this);
    }

    private void loadTvDetail(int id) {
        apiService.getTvDetail(id, new Callback() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                tvDetail = (TvDetail) response.body();

                if (tvDetail != null) {
                    for (int i = 0; i < tvDetail.getRuntime().size(); i++) {
                        posterDuration.setText(String.format("%s %d %s",
                                getString(R.string.txt_detail_duration),
                                tvDetail.getRuntime().get(i),
                                getString(R.string.text_menit)));
                    }
                    for (int i = 0; i < tvDetail.getGenres().size(); i++) {
                        Genre genre = tvDetail.getGenres().get(i);

                        if (i < tvDetail.getGenres().size() - 1) {
                            posterCategory.append(" " + genre.getName() + ",");
                        } else {
                            posterCategory.append(genre.getName());
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_data_text), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_failed), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_connection_error), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = String.format("%s\n%s\n%s", posterTitle.getText().toString(),
                    posterDescription.getText().toString(), getString(R.string.txt_detail_shared));
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.txt_detail_subject);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.txt_detail_title)));
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMovieDetail(int id) {
        apiService.getMovieDetail(id, new Callback() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                movieDetail = (MovieDetail) response.body();

                if (movieDetail != null) {
                    posterDuration.setText(String.format("%s%d %s",
                            getString(R.string.txt_detail_duration),
                            movieDetail.getRuntime(),
                            getString(R.string.text_menit)));
                    for (int i = 0; i < movieDetail.getGenres().size(); i++) {
                        Genre genre = movieDetail.getGenres().get(i);

                        if (i < movieDetail.getGenres().size() - 1) {
                            posterCategory.append(" " + genre.getName() + ",");
                        } else {
                            posterCategory.append(genre.getName());
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_data_text), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_failed), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_connection_error), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showTrailers(List<TrailerData> trailerDatas) {
        viewTrailers.removeAllViews();

        for (int i = 0; i < trailerDatas.size(); i++) {

            final TrailerData trailerData = trailerDatas.get(i);
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_item_trailer, viewTrailers, false);

            ImageView trailerThumb = view.findViewById(R.id.trailer_thumb);
            TextView trailerName = view.findViewById(R.id.trailer_name);

            if (trailerData.getSite().equalsIgnoreCase("youtube")) {
                Picasso.get()
                        .load(BASE_URL_YOUTUBE + trailerData.getKey() + DEFAULT_IMAGE)
                        .into(trailerThumb);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    watchYoutubeVideo(trailerData.getKey());
                }
            });


            trailerName.setText(trailerData.getName());
            viewTrailers.addView(view);
        }
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private void loadTrailersTv(int id) {
        apiService.getTrailersTv(id, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                Trailer trailer = (Trailer) response.body();

                if (trailer != null) {
                    showTrailers(trailer.getResults());
                } else {
                    Toast.makeText(getApplicationContext(), "No Data!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getApplicationContext(), "Request Timeout. Please try again!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadTrailer(int id) {
        apiService.getTrailers(id, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                trailer = (Trailer) response.body();

                if (trailer != null) {
                    showTrailers(trailer.getResults());
                } else {
                    Toast.makeText(getApplicationContext(), "No Data!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getApplicationContext(), "Request Timeout. Please try again!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadDataTv() {
        Random r = new Random();
        page = r.nextInt(100 - 65) + 65;
        ApiService apiService = new ApiService();
        apiService.getPopularTvShow(page, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                tv = (Tv) response.body();
                if (tv != null) {
                    if (tvAdapter != null) {
                        tvAdapter.addAll(tv.getResults());
                        recyclerView.setAdapter(tvAdapter);
                        tvAdapter.setOnTvItemSelectedListener(new TvAdapter.OnTvItemSelectedListener() {
                            @Override
                            public void onItemClick(View v, TvData tvData, int position) {
                                Intent detailMovie = new Intent(DetailMovieActivity.this, DetailMovieActivity.class);
                                detailMovie.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                detailMovie.putExtra(DetailMovieActivity.EXTRA_TVSHOW, tvAdapter.getItem(position));
                                detailMovie.putExtra(DetailMovieActivity.EXTRA_CATEGORY,CATEGORY_INTENT_TV);
                                startActivity(detailMovie);
                            }
                        });
                        refreshLayout.setRefreshing(false);
                        scrollView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_data_text), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_failed), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_connection_error), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadData() {
        ApiService apiService = new ApiService();
        int page = 1;
        apiService.getPopularMovies(page, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                movie = (Movie) response.body();
                if (movie != null) {
                    if (movieAdapter != null) {
                        movieAdapter.addAll(movie.getResults());
                        recyclerView.setAdapter(movieAdapter);
                        movieAdapter.setOnMovieItemSelectedListener(new MovieAdapter.OnMovieItemSelectedListener() {
                            @Override
                            public void onItemClick(View v, MovieData movie, int position) {
                                Intent detailMovie = new Intent(DetailMovieActivity.this, DetailMovieActivity.class);
                                detailMovie.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                detailMovie.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieAdapter.getItem(position));
                                detailMovie.putExtra(DetailMovieActivity.EXTRA_CATEGORY,CATEGORY_INTENT_MOVIE);
                                startActivity(detailMovie);
                            }
                        });
                        refreshLayout.setRefreshing(false);
                        scrollView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_data_text), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_failed), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_connection_error), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initData() {
        if (Category.equals(CATEGORY_INTENT_MOVIE)) {
            LoadMovie();
        } else if (Category.equals(CATEGORY_INTENT_TV)) {
            LoadTV();
        }
    }

    private void LoadMovie() {
        refreshLayout.setRefreshing(true);
        scrollView.setVisibility(View.GONE);
        movieData = getIntent().getParcelableExtra(EXTRA_MOVIE);
        if (movieData != null) {
            loadMovieDetail(movieData.getId());
            loadTrailer(movieData.getId());
            loadData();
            posterTitle.setText(movieData.getTitle());
            posterReleaseDate.setVisibility(View.GONE);
            posterReleaseDate.setText(String.format("%s %s",
                    getString(R.string.txt_detail_release_date), movieData.getReleaseDate()));
            posterRating.setRating((float) (movieData.getVoteAverage() / 2));
            posterDescription.setText(movieData.getOverview());
            posterRatingText.setText(String.valueOf(movieData.getVoteAverage() / 2));
            Picasso.get()
                    .load(Constant.IMG_URL + movieData.getPosterPath())
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(posterImage);

            Picasso.get()
                    .load(Constant.BACKDROP_URL + movieData.getBackdropPath())
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(posterCover);
        }
    }

    private void LoadTV() {
        refreshLayout.setRefreshing(true);
        scrollView.setVisibility(View.GONE);
        tvData = getIntent().getParcelableExtra(EXTRA_TVSHOW);
        if (tvData != null) {
            loadTvDetail(tvData.getId());
            loadTrailersTv(tvData.getId());
            loadDataTv();
            posterTitle.setText(tvData.getName());
            posterReleaseDate.setVisibility(View.GONE);
            posterRating.setRating((float) (tvData.getVote_average() / 2));
            posterDescription.setText(tvData.getOverview());
            posterRatingText.setText(String.valueOf(tvData.getVote_average() / 2));
            Picasso.get()
                    .load(Constant.IMG_URL + tvData.getPoster_path())
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(posterImage);

            Picasso.get()
                    .load(Constant.BACKDROP_URL + tvData.getBackdrop_path())
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(posterCover);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivPosterLike) {
            if (Category.equals(CATEGORY_INTENT_MOVIE)) {
                if (clicked) {
                    ImageView btnLike1 = findViewById(R.id.ivPosterLike);
                    btnLike1.setBackgroundResource(R.drawable.thumb_blue);
                    clicked = false;
                }

                favorite = new Favorite(movieData.getId(),movieData.getTitle(),movieData.getPosterPath(),
                        movieData.getOverview(),movieData.getReleaseDate(),movieData.getBackdropPath(),
                        movieData.getReleaseDate(),movieData.getVoteAverage());
                RealmHelper realmHelper = new RealmHelper(realm);

                realmHelper.AddToFavorite(favorite,this);
            } else if (Category.equals(CATEGORY_INTENT_TV)) {
                if (clicked) {
                    ImageView btnLike1 = findViewById(R.id.ivPosterLike);
                    btnLike1.setBackgroundResource(R.drawable.thumb_blue);
                    clicked = false;
                }

                favoriteTv = new FavoriteTv(tvData.getId(),tvData.getName(),tvData.getPoster_path(),
                        tvData.getOverview(),posterCategory.getText().toString(),tvData.getBackdrop_path(),
                        tvDetail.getGenres().toString(),tvData.getVote_average());
                RealmHelper realmHelper = new RealmHelper(realm);
                realmHelper.AddToFavoriteTv(favoriteTv,this);
            }
        }
    }
}