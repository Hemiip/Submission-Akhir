package com.miftahjuanda.movies.Helper;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.miftahjuanda.movies.Model.Favorite;
import com.miftahjuanda.movies.Model.FavoriteTv;
import com.miftahjuanda.movies.R;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {
    private Realm realm;

    public  RealmHelper(Realm realm){
        this.realm = realm;
    }

    public void AddToFavorite(final Favorite favorite, final Activity activity){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                Favorite fav = realm.where(Favorite.class).equalTo("id",favorite.getId()).findFirst();
                if (fav!=null){
                    Toast.makeText(activity,activity.getString(R.string.toast_text_fav_exist),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(activity,activity.getString(R.string.txt_detail_clicked),Toast.LENGTH_SHORT).show();
                    realm.copyToRealm(favorite);
                }
            }
        });
    }

    public void AddToFavoriteTv(final FavoriteTv favorite, final Activity activity){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                FavoriteTv fav = realm.where(FavoriteTv.class).equalTo("id",favorite.getId()).findFirst();
                if (fav!=null){
                    Toast.makeText(activity,activity.getString(R.string.toast_text_fav_exist),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(activity,activity.getString(R.string.txt_detail_clicked),Toast.LENGTH_SHORT).show();
                    realm.copyToRealm(favorite);
                }
            }
        });
    }



    public List<Favorite> getFavoritList(){
        return realm.where(Favorite.class).findAll();
    }
    public List<FavoriteTv> getFavoritListTv(){
        return realm.where(FavoriteTv.class).findAll();
    }

    public void deleteFavorite(Integer id){
        final RealmResults<Favorite> model = realm.where(Favorite.class).equalTo("id", id).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                model.deleteAllFromRealm();
            }
        });
    }
    public void deleteFavoriteTv(Integer id){
        final RealmResults<FavoriteTv> model = realm.where(FavoriteTv.class).equalTo("id", id).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                model.deleteAllFromRealm();
            }
        });
    }
}