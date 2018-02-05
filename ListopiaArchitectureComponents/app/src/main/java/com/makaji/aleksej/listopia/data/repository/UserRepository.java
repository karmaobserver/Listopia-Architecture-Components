package com.makaji.aleksej.listopia.data.repository;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.makaji.aleksej.listopia.AppExecutors;
import com.makaji.aleksej.listopia.data.api.ApiResponse;
import com.makaji.aleksej.listopia.data.api.ListopiaService;
import com.makaji.aleksej.listopia.data.dao.ShoppingListDao;
import com.makaji.aleksej.listopia.data.dao.UserDao;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.data.entity.User;
import com.makaji.aleksej.listopia.data.vo.Resource;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Aleksej on 2/4/2018.
 */

public class UserRepository {

    private final AppExecutors appExecutors;
    private final ListopiaService listopiaService;
    private final UserDao userDao;

    @Inject
    public UserRepository(AppExecutors appExecutors, UserDao userDao, ListopiaService listopiaService) {
        this.appExecutors = appExecutors;
        this.userDao = userDao;
        this.listopiaService = listopiaService;
    }

    public LiveData<Resource<User>> validateToken(String userId) {
        return new NetworkBoundResource<User, User>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull User user) {
                Timber.d("User Inserting");
                userDao.insertUser(user);
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                //Always call endpoint API
                Timber.d("Should fetch");
                return true;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                Timber.d("LoadFromDB: findUserById");
                return userDao.findUserById(userId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                Timber.d("API CALL validateToken");
                LiveData<ApiResponse<User>> result = listopiaService.validateToken();
                Timber.d("Result API: " + result.getValue());
                /*Timber.d("Result API: " + result.getValue().code);
                Timber.d("Result API: " + result.getValue().errorMessage.toString());*/
                return result;
            }
        }.asLiveData();
    }

    public LiveData<Resource<User>> findUserById(String userId) {
        return new NetworkBoundResource<User, User>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull User user) {
                //
                //userDao.insertUser(user);
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                //only from local data (since we fetch when we sign in)
                return false;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                Timber.d("LoadFromDB: findUserById");
                return userDao.findUserById(userId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return null;
            }
        }.asLiveData();
    }

   /* public void validateToken() {
        Call<User> result = listopiaService.validateToken();
        result.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Timber.d("response is: "+response.code());
                Timber.d("response is: "+response.body());
                Timber.d("response is " + response.body().getEmail());
                if (response.isSuccessful())
                    insertUser(response.body());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //call.
                Timber.d("response FAIL: "+ t);
            }
        });

    }*/

   /* public MutableLiveData<User> validateToken() {
        final MutableLiveData<User> getUserResponse = new MutableLiveData<>();
        Call<User> result = listopiaService.validateToken();
        result.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Timber.d("response is: "+response.code());
                Timber.d("response is: "+response.body());
                Timber.d("response is " + response.body().getEmail());
                if (response.isSuccessful())
                    insertUser(response.body());
                getUserResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //call.
                Timber.d("response FAIL: "+ t);
            }
        });
        return getUserResponse;
    }*/

    //Temporary Solution
    @SuppressLint("StaticFieldLeak")
    public void insertUser(User user) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                userDao.insertUser(user);
                return null;
            }
        }.execute();
    }


}
