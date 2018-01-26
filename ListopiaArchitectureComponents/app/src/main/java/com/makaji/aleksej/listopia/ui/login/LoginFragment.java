package com.makaji.aleksej.listopia.ui.login;

import android.content.Context;

import android.content.Intent;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.makaji.aleksej.listopia.R;

import com.makaji.aleksej.listopia.binding.FragmentDataBindingComponent;
import com.makaji.aleksej.listopia.databinding.FragmentLoginBinding;
import com.makaji.aleksej.listopia.di.module.Injectable;
import com.makaji.aleksej.listopia.ui.common.OnFragmentToolbarInteraction;
import com.makaji.aleksej.listopia.util.AutoClearedValue;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 1/21/2018.
 */

public class LoginFragment extends Fragment implements Injectable, GoogleApiClient.OnConnectionFailedListener {

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentLoginBinding> binding;

    private OnFragmentToolbarInteraction onFragmentToolbarInteraction;

    private GoogleApiClient googleApiClient;
    private int RC_SIGN_IN = 0;

    @Inject
    GoogleSignInOptions googleSignInOptions;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentToolbarInteraction) {
            onFragmentToolbarInteraction = (OnFragmentToolbarInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         FragmentLoginBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login,
                container, false, dataBindingComponent);
        //Bind listeners
        //....
        //Clear values
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Timber.d("GOOGLE: " + googleSignInOptions);

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        binding.get().buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (optionalPendingResult.isDone()) {

            Timber.d("Got cached sign-in");
            GoogleSignInResult result = optionalPendingResult.get();
            handleSignInResult(result);
        } else {
            Timber.d("Got cached sign-in NOT NOT NOT NOT");
           /* showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });*/
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Timber.d("HandleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();

            binding.get().textGoogleEmail.setText(account.getEmail());
            binding.get().textGoogleName.setText(account.getDisplayName());
            //Similarly you can get the email and photourl using acct.getEmail() and  acct.getPhotoUrl()

           /* if(acct.getPhotoUrl() != null)
            new LoadProfileImage(imgProfilePic).execute(account.getPhotoUrl().toString());

        updateUI(true);
    } else {
        // Signed out, show unauthenticated UI.
        updateUI(false);*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Setup toolbar
        onFragmentToolbarInteraction.enableNavigationBackIcon();
        onFragmentToolbarInteraction.setToolbarTitle(R.string.toolbar_login);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentToolbarInteraction = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menu.clear();
        menuInflater.inflate(R.menu.menu_fragment_back_only, menu);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Timber.d("OnConnectionFailed has happend:" + connectionResult);
    }
}
