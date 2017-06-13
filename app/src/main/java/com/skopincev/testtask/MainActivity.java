package com.skopincev.testtask;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.skopincev.testtask.presenter.MainPresenter;
import com.skopincev.testtask.presenter.MainPresenterImpl;
import com.skopincev.testtask.view.MainView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements
        OnConnectionFailedListener,
        MainView {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = MainActivity.class.getSimpleName();

    private MainPresenter presenter;

    @BindView(R.id.btn_sign_in)
    SignInButton btn_sign_in;

    @BindView(R.id.btn_sign_out)
    Button btn_sign_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainPresenterImpl();
        presenter.attach(this);
        initUI();
    }

    private void initUI() {
        if (btn_sign_in != null) {
            btn_sign_in.setSize(SignInButton.SIZE_STANDARD);
        }
        if (btn_sign_out != null) {
            btn_sign_out.setHeight(btn_sign_out.getHeight());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, connectionResult.getErrorMessage());
    }

    @OnClick(R.id.btn_sign_in)
    void signIn() {
        presenter.signIn();
    }

    @OnClick(R.id.btn_sign_out)
    void signOut() {
        presenter.signOut();
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
        //Intent intent = new Intent(this, );
    }

    @Override
    public void onSignInNotSucceed() {
        Toast.makeText(this, R.string.sign_in_failed_message, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onSignOut(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }
}
