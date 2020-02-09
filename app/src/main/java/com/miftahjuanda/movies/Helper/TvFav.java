package com.miftahjuanda.movies.Helper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.miftahjuanda.movies.Model.Favorite;
import com.miftahjuanda.movies.Model.FavoriteTv;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class TvFav extends ContentProvider {
    static final String PROVIDER_NAME = "com.miftahjuanda.movies";
    static final String URL = "content://" + PROVIDER_NAME;
    public static final Uri CONTENT_URI = Uri.parse(URL);
    private Realm realm;
    private RealmHelper realmHelper;

    public TvFav() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);
        realmHelper = new RealmHelper(realm);
        return getCursorFromList(realmHelper.getFavoritListTv(),realmHelper.getFavoritList());
    }

    public Cursor getCursorFromList(List<FavoriteTv> FavTv, List<Favorite> Movie) {
        MatrixCursor cursor = new MatrixCursor(new String[] {"image", "title","releasedate",
                "description","vote"}
        );

        for ( FavoriteTv favoriteTv : FavTv ) {
            cursor.newRow()
                    .add("image", favoriteTv.getPosterPath())
                    .add("title", favoriteTv.getTitle())
                    .add("releasedate", favoriteTv.getReleaseDate())
                    .add("description", favoriteTv.getOverview())
                    .add("vote", favoriteTv.getVoteAverage());
        }

        for ( Favorite favorite : Movie ) {
            cursor.newRow()
                    .add("image", favorite.getPosterPath())
                    .add("title", favorite.getTitle())
                    .add("releasedate", favorite.getReleaseDate())
                    .add("description", favorite.getOverview())
                    .add("vote", favorite.getVoteAverage());
        }

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
