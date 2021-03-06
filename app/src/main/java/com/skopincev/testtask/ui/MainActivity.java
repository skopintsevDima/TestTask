package com.skopincev.testtask.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.skopincev.testtask.R;
import com.skopincev.testtask.dagger.base.BaseActivity;
import com.skopincev.testtask.dagger.component.ActivityComponent;
import com.skopincev.testtask.presenter.MainPresenter;
import com.skopincev.testtask.view.MainView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements
        MainView {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";

    @Inject
    GoogleApiClient googleApiClient;
    @Inject
    MainPresenter presenter;

    @BindView(R.id.btn_sign_in)
    SignInButton btn_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.attach(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                presenter.silentSignIn();
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });
        googleApiClient.registerConnectionFailedListener(new OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                initUI();
                showMessage(getString(R.string.message_connection_failed));
            }
        });
        googleApiClient.connect();
    }

    @Override
    public void inject(ActivityComponent injector) {
        try {
            injector.inject(this);
        }
        catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void initUI() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (btn_sign_in != null) {
            btn_sign_in.setSize(SignInButton.SIZE_STANDARD);
        }
    }

    @OnClick(R.id.btn_sign_in)
    void signIn() {
        presenter.signIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            presenter.onSignInResult(data);
        }
    }

    @Override
    public void startValidationActivity(Intent signInIntent) {
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void openContacts(String userEmail) {
        Intent intent = new Intent(this, ContactsActivity.class);
        intent.putExtra(KEY_USER_EMAIL, userEmail);
        startActivity(intent);
        finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }
}
