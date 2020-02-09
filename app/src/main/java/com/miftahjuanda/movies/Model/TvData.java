package com.miftahjuanda.movies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TvData implements Parcelable {

    @SerializedName("original_name")
    private String originalName;
    @SerializedName("name")
    private String name;
    @SerializedName("popularity")
    private double popularity;
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("first_air_date")
    private String firstAirDate;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("id")
    private int id;
    @SerializedName("vote_average")
    private Double voteAverage;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("genre_ids")
    private List<Integer> genreIds;
    @SerializedName("origin_country")
    private List<String> originCountry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackdrop_path() {
        return backdropPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVote_average() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.originalName);
        dest.writeString(this.name);
        dest.writeDouble(this.popularity);
        dest.writeInt(this.voteCount);
        dest.writeString(this.firstAirDate);
        dest.writeString(this.backdropPath);
        dest.writeString(this.originalLanguage);
        dest.writeInt(this.id);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
        dest.writeList(this.genreIds);
        dest.writeStringList(this.originCountry);
    }

    private TvData(Parcel in) {
        this.originalName = in.readString();
        this.name = in.readString();
        this.popularity = in.readDouble();
        this.voteCount = in.readInt();
        this.firstAirDate = in.readString();
        this.backdropPath = in.readString();
        this.originalLanguage = in.readString();
        this.id = in.readInt();
        this.voteAverage = in.readDouble();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.genreIds = new ArrayList<>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
        this.originCountry = in.createStringArrayList();
    }

    public static final Creator<TvData> CREATOR = new Creator<TvData>() {
        @Override
        public TvData createFromParcel(Parcel source) {
            return new TvData(source);
        }

        @Override
        public TvData[] newArray(int size) {
            return new TvData[size];
        }
    };
}