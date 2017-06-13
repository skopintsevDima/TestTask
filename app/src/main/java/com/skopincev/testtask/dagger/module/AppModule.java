package com.skopincev.testtask.dagger.module;

/**
 * Created by raisa on 19.03.17.
 */


import android.content.Context;

import com.skopincev.testtask.dagger.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/** Application module. Used to provide application level context. */
@Module
public class AppModule {

    /** Application instance. */
    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context getContext() {
        return app;
    }

    @Provides
    public App getApp() {
        return app;
    }

}