package com.miftahjuanda.movies.API;

public class Constant {
    static final String BASE_URL = "https://api.themoviedb.org";
    public static final String BASE_URL_YOUTUBE = "http://img.youtube.com/vi/";
    public static final String DEFAULT_IMAGE = "/default.jpg";
    public static final String IMG_URL = "http://image.tmdb.org/t/p/w185";
    public static final String BACKDROP_URL = "http://image.tmdb.org/t/p/w780";
    static final String API_KEY = "8714c0388fc4e53742a3a9b881a6120e";
    private static final String VERSION = "/3";
    private static final String MOVIE = "/movie";
    private static final String TVSHOW = "/tv";
    private static final String DISCOVER = "/discover";
    private static final String UP_COMING = "/discover/movie/";
    private static final String SEARCH_MOVIE = "/search/movie";
    private static final String SEARCH_TV = "/search/tv";
    static final String VIDEOS = "videos";
    static final String LANG_EN = "en-US";
    static final String MOVIE_PATH = VERSION + DISCOVER + MOVIE;
    static final String TV_PATH = VERSION + DISCOVER + TVSHOW;
    static final String DETAIL_MOVIE = VERSION + MOVIE;
    static final String DETAIL_TV = VERSION + TVSHOW;
    static final String UPCOMING = VERSION + UP_COMING;
    static final String SEARCHMOVIE = VERSION + SEARCH_MOVIE;
    static final String SEARCHTV = VERSION + SEARCH_TV;
}
