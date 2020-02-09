package com.miftahjuanda.movies.Widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.miftahjuanda.movies.API.Constant;
import com.miftahjuanda.movies.Model.FavoriteTv;
import com.miftahjuanda.movies.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.miftahjuanda.movies.Helper.TvFav.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<FavoriteTv> favoriteList = new ArrayList<>();
    private final Context mContext;
    private Cursor cursor;
    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        if (cursor != null ) {
            favoriteList.clear();
        }

        cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        if (cursor != null) cursor.close();
        favoriteList.clear();
    }

    @Override
    public int getCount() {
        if (cursor == null){
            favoriteList.clear();
            return 0;
        }
        else {
            return cursor.getCount();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            String image,title;
            while(!cursor.isAfterLast()) {
                image = cursor.getString(cursor.getColumnIndex("image"));
                title = cursor.getString(cursor.getColumnIndex("title"));
                FavoriteTv favoriteTv = new FavoriteTv(image,title);
                favoriteList.add(favoriteTv);
                cursor.moveToNext();
            }

            Bitmap bmp = null;
            String link = Constant.IMG_URL + favoriteList.get(position).getPosterPath();
            String movie_title = favoriteList.get(position).getTitle();
            try {

                bmp = Glide.with(mContext)
                        .load(link)
                        .asBitmap()
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

            } catch (InterruptedException | ExecutionException e) {
                Log.d("Widget Load Error", "error");
            }
            rv.setImageViewBitmap(R.id.imageView, bmp);

            Bundle extras = new Bundle();
            extras.putString(ImageBannerWidget.EXTRA_ITEM, movie_title);

            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);

            rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        }

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (cursor.moveToPosition(position)) {
            return cursor.getCount();
        } else return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
