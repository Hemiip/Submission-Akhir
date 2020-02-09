package com.miftahjuanda.movies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FavoriteTv extends RealmObject implements Parcelable {

    @PrimaryKey
    private Integer id;
    private String title;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String backdropPath;

    private int runtime;
    private String Category;
    private double voteAverage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }


    public FavoriteTv(Integer id, String title, String posterPath, String overview, String releaseDate, String backdropPath, String category, double voteAverage) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
        this.Category = category;
        this.voteAverage = voteAverage;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.backdropPath);
        dest.writeInt(this.runtime);
        dest.writeString(this.Category);
        dest.writeDouble(this.voteAverage);
    }

    public FavoriteTv() {
    }

    public FavoriteTv(String posterPath, String title){
        this.posterPath = posterPath;
        this.title = title;
    }

    private FavoriteTv(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.backdropPath = in.readString();
        this.runtime = in.readInt();
        this.Category = in.readString();
        this.voteAverage = in.readDouble();
    }

    public static final Creator<FavoriteTv> CREATOR = new Creator<FavoriteTv>() {
        @Override
        public FavoriteTv createFromParcel(Parcel source) {
            return new FavoriteTv(source);
        }

        @Override
        public FavoriteTv[] newArray(int size) {
            return new FavoriteTv[size];
        }
    };
}