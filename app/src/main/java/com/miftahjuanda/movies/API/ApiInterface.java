package com.miftahjuanda.movies.API;

import com.miftahjuanda.movies.Model.Movie;
import com.miftahjuanda.movies.Model.MovieDetail;
import com.miftahjuanda.movies.Model.Trailer;
import com.miftahjuanda.movies.Model.Tv;
import com.miftahjuanda.movies.Model.TvDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET(Constant.MOVIE_PATH)
    Call<Movie> popularMovies(
            @Query("page") int page);

    @GET(Constant.TV_PATH)
    Call<Tv> popularTvShow(
            @Query("page") int page);

    @GET(Constant.DETAIL_MOVIE + "/{movie_id}")
    Call<MovieDetail> movieDetail(
            @Path("movie_id") int movieId);

    @GET(Constant.DETAIL_TV + "/{tv_id}")
    Call<TvDetail> tvDetail(
            @Path("tv_id") int tvId);

    @GET(Constant.DETAIL_MOVIE + "/{movie_id}/" + Constant.VIDEOS)
    Call<Trailer> trailers(
            @Path("movie_id") int movieId);

    @GET(Constant.DETAIL_TV + "/{tv_id}/" + Constant.VIDEOS)
    Call<Trailer> trailers_tv(
            @Path("tv_id") int movieId);

    @GET(Constant.UPCOMING)
    Call<Movie> upcoming(
            @Query("primary_release_date.gte") String currentdate,@Query("primary_release_date.lte") String date);

    @GET(Constant.SEARCHMOVIE)
    Call<Movie>searchmovie(
            @Query("query") String search);

    @GET(Constant.SEARCHTV)
    Call<Tv>searchTv(
            @Query("query") String search);
}