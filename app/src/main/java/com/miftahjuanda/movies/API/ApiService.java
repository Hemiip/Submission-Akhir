package com.miftahjuanda.movies.API;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private ApiInterface apiInterface;

    public ApiService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(builder())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    private OkHttpClient builder() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder();
        okHttpClient.connectTimeout(20, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(20, TimeUnit.SECONDS);
        okHttpClient.readTimeout(90, TimeUnit.SECONDS);

        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url()
                        .newBuilder()
                        .addQueryParameter("api_key", Constant.API_KEY)
                        .addQueryParameter("language", Constant.LANG_EN)
                        .build();

                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        });

        return okHttpClient.build();
    }

    public void getUpcoming(String currentdate,String date,Callback callback){
        apiInterface.upcoming(currentdate,date).enqueue(callback);
    }

    public void searchmovie(String searchtext,Callback callback){
        apiInterface.searchmovie(searchtext).enqueue(callback);
    }

    public void searchTv(String searchtext,Callback callback){
        apiInterface.searchTv(searchtext).enqueue(callback);
    }

    public void getPopularMovies(int page, Callback callback) {
        apiInterface.popularMovies(page).enqueue(callback);
    }
    public void getPopularTvShow(int page, Callback callback) {
        apiInterface.popularTvShow(page).enqueue(callback);
    }
    public void getMovieDetail(int movieId, Callback callback) {
        apiInterface.movieDetail(movieId).enqueue(callback);
    }
    public void getTvDetail(int tvId, Callback callback) {
        apiInterface.tvDetail(tvId).enqueue(callback);
    }
    public void getTrailers(int movieId, Callback callback) {
        apiInterface.trailers(movieId).enqueue(callback);
    }
    public void getTrailersTv(int tvId, Callback callback) {
        apiInterface.trailers_tv(tvId).enqueue(callback);
    }
}