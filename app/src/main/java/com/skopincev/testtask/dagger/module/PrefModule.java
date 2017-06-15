package com.skopincev.testtask.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by skopi on 15.06.2017.
 */

@Module(includes = AppModule.class)
public class PrefModule {

    private static final String prefName = "MainPref";

    @Singleton
    @Provides
    public SharedPreferences getPrefs(Context context){
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }
}
