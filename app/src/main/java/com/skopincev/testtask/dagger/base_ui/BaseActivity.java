package com.skopincev.testtask.dagger.base_ui;

/**
 * Created by raisa on 19.03.17.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.skopincev.testtask.dagger.App;
import com.skopincev.testtask.dagger.component.ActivityComponent;

import butterknife.ButterKnife;

/** Base activity. */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Injects Presenter.
     */
    private ActivityComponent injector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injector = restoreInjector();
        inject(injector);
    }

    /**
     * Request to execute injection of itself.
     * @param injector injector
     */
    public abstract void inject(ActivityComponent injector);

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    //TODO: WTF?!?!
    /** Restores presenter while configuration change. */
    private ActivityComponent restoreInjector() {
        Object o = getLastCustomNonConfigurationInstance();
        if (o == null) {
            return getApp().getAppComponent().getActivityComponent();
        } else {
            return (ActivityComponent) o;
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return injector;
    }

    public App getApp() {
        return (App) super.getApplication();
    }
}