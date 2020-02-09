package com.miftahjuanda.myfavorite.Model;

public class Favorite {
    private String title;
    private String posterPath;
    private String overview;
    private String releaseDate;

    private double voteAverage;

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }


    public double getVoteAverage() {
        return voteAverage;
    }

    public Favorite(String posterPath, String title, String releaseDate, String overview, double voteAverage) {
        this.posterPath = posterPath;
        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.voteAverage = voteAverage;
    }
}
