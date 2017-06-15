package com.skopincev.testtask.dagger.module;

import android.content.Context;

import com.skopincev.testtask.db.DBHelper;
import com.skopincev.testtask.db.storio.StorioSqlApi;
import com.skopincev.testtask.db.storio.StorioSqlApiImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by skopi on 16.06.2017.
 */

@Module(includes = AppModule.class)
public class DBModule {

    @Provides
    @Singleton
    public StorioSqlApi getSqlApi(DBHelper dbHelper) {
        return new StorioSqlApiImpl(dbHelper);
    }

    @Provides
    @Singleton
    public DBHelper getDbHelper(Context context) {
        return new DBHelper(context);
    }
}
