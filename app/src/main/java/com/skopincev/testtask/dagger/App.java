package com.skopincev.testtask.dagger;

/**
 * Created by raisa on 19.03.17.
 */


import android.app.Application;

import com.skopincev.testtask.dagger.component.AppComponent;

public class App extends Application {

    private static App app;
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        app = App.this;
        component = AppComponent.Initializer.init(this);
    }

    public static AppComponent getAppComponent() {
        return app.component;
    }
}
