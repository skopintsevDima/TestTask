package com.skopincev.testtask.dagger.module;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.skopincev.testtask.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by skopi on 13.06.2017.
 */

@Module(includes = AppModule.class)
public class GoogleModule {

    public static final String TAG = GoogleModule.class.getSimpleName();

    @Singleton
    @Provides
    public GoogleApiClient getClient(GoogleSignInOptions googleSignInOptions, Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, connectionResult.getErrorMessage());
                    }
                })
                .build();
        return googleApiClient;
    }

    @Provides
    public GoogleSignInOptions getSignInOptions(Context context){
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.server_client_id))
                .requestEmail()
                .build();
    }
}
