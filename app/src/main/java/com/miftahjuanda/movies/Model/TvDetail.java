package com.miftahjuanda.movies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TvDetail implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("episode_run_time")
    private List<Integer> runtime;
    @SerializedName("genres")
    private List<Genre> genres;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<Integer> getRuntime() {
        return runtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeList(this.runtime);
        dest.writeList(this.genres);
    }

    private TvDetail(Parcel in) {
        this.id = in.readInt();
        this.runtime = new ArrayList<>();
        in.readList(this.runtime, Integer.class.getClassLoader());
        this.genres = new ArrayList<>();
        in.readList(this.genres, Genre.class.getClassLoader());
    }

    public static final Parcelable.Creator<TvDetail> CREATOR = new Parcelable.Creator<TvDetail>() {
        @Override
        public TvDetail createFromParcel(Parcel source) {
            return new TvDetail(source);
        }

        @Override
        public TvDetail[] newArray(int size) {
            return new TvDetail[size];
        }
    };
}