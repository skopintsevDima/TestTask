package com.skopincev.testtask.dagger.component;

import com.skopincev.testtask.dagger.App;
import com.skopincev.testtask.dagger.module.AppModule;
import com.skopincev.testtask.dagger.module.DBModule;
import com.skopincev.testtask.dagger.module.GoogleModule;
import com.skopincev.testtask.dagger.module.PrefModule;
import com.skopincev.testtask.dagger.module.PresenterModule;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by raisa on 19.03.17.
 */

@Singleton
@Component(modules = {
        AppModule.class,
        PresenterModule.class,
        GoogleModule.class,
        PrefModule.class,
        DBModule.class
})
/**
 * Application component. Source component for all sub-components.
 */
public interface AppComponent {

    void inject(App app);

    ActivityComponent getActivityComponent();

    final class Initializer {

        private Initializer() {}

        public static AppComponent init(App app) {
            return DaggerAppComponent.builder()
                    .appModule(new AppModule(app))
                    .presenterModule(new PresenterModule())
                    .googleModule(new GoogleModule())
                    .prefModule(new PrefModule())
                    .dBModule(new DBModule())
                    .build();
        }
    }
}
