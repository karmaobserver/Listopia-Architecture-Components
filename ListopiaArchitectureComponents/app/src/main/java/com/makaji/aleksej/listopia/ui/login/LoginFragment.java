package com.makaji.aleksej.listopia.ui.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.makaji.aleksej.listopia.R;

import com.makaji.aleksej.listopia.binding.FragmentDataBindingComponent;
import com.makaji.aleksej.listopia.databinding.FragmentLoginBinding;
import com.makaji.aleksej.listopia.di.module.Injectable;
import com.makaji.aleksej.listopia.ui.common.OnFragmentToolbarInteraction;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListNavigationController;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListViewModel;
import com.makaji.aleksej.listopia.util.AutoClearedValue;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 1/21/2018.
 */

public class LoginFragment extends Fragment implements Injectable {

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentLoginBinding> binding;

    private OnFragmentToolbarInteraction onFragmentToolbarInteraction;

    private UserViewModel userViewModel;

    private GoogleSignInClient googleSignInClient;
    private int RC_SIGN_IN = 9001;
    private int RC_GET_TOKEN = 9002;

    @Inject
    GoogleSignInOptions googleSignInOptions;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    ShoppingListNavigationController shoppingListNavigationController;

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
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);

        binding.get().buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_GET_TOKEN);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_TOKEN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Timber.d("HandleSignInResult:" + completedTask.isSuccessful());
        try {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            String userId = account.getId();
            Timber.d("idToken: " + idToken);
            Timber.d("userId: " + userId);
            //Put token into sharedPreferences, which is used for every HTTP header by Interceptor
            sharedPreferences.edit().putString(getString(R.string.key_token), idToken).commit();

            //if token validation pass (TODO make check)
            sharedPreferences.edit().putString(getString(R.string.key_user_id), userId).commit();

       /*     String ressToken = getResources().getString(R.string.key_token);
            String token = sharedPreferences.getString(ressToken, "defualt");
            Timber.d("Token from SP: " + token);*/
            userViewModel.validateToken(userId).observe(this, userResource -> {
                binding.get().setResource(userResource);
                Timber.d("Observing validateToken");
                if (userResource.data != null) {
                    Timber.d("User NOT null");
                    Snackbar.make(getView(), R.string.success_login, Snackbar.LENGTH_LONG).show();
                    shoppingListNavigationController.popBackStackMethod();
                }
            });

            //test only
            binding.get().textGoogleEmail.setText(account.getEmail());
            binding.get().textGoogleName.setText(account.getDisplayName());
            Timber.d("Observing validateToken finished and waitting for observ");
            //shoppingListNavigationController.popBackStackMethod();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //https://developers.google.com/android/reference/com/google/android/gms/common/api/CommonStatusCodes#NETWORK_ERROR
            Timber.d("signInResult:failed code=" + e.getStatusCode());
            switch (e.getStatusCode()) {
                case 7:
                    Timber.d("Login failed. A network error occurred.");
                    Snackbar.make(getView(), R.string.error_network_login, Snackbar.LENGTH_LONG).show();
                    break;
                case 6:
                    Timber.d("Error code 6");
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Attempt to silently refresh the GoogleSignInAccount. If the GoogleSignInAccount
        // already has a valid token this method may complete immediately.
        //
        // If the user has not previously signed in on this device or the sign-in has expired,
        // this asynchronous branch will attempt to sign in the user silently and get a valid
        // ID token. Cross-device single sign on will occur in this branch.
        googleSignInClient.silentSignIn()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        handleSignInResult(task);
                    }
                });
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

}
