package com.makaji.aleksej.listopia.data.repository;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
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
import com.makaji.aleksej.listopia.data.entity.UserWithFriends;
import com.makaji.aleksej.listopia.data.vo.Resource;
import com.makaji.aleksej.listopia.util.SingleLiveData;

import java.io.IOException;
import java.util.ArrayList;
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

    public LiveData<Resource<User>> addUserToFriendsByEmail(String userId, String email) {
        return new NetworkBoundResource<User, User>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull User user) {
                userDao.insertUser(user);
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                Timber.d("Should fetch:");
                return true;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                Timber.d("LoadFromDB: findUserByEmail");
                return userDao.findUserWithFriendsById(userId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                Timber.d("Making call findUserByEmail:");
                LiveData<ApiResponse<User>> result = listopiaService.addUserToFriendsByEmail(userId, email);
                Timber.d("Result findUserByEmail is: " + result);
                //Timber.d("Result findUserByEmail is: " + result.getValue().body.getFriends().size());
                return result;
            }
        }.asLiveData();
    }


    private final SingleLiveData<Resource<String>> resultDeleteAllFriends = new SingleLiveData<>();

    public SingleLiveData<Resource<String>> deleteAllFriends(String userId) {
        Call<User> result = listopiaService.deleteAllFriends(userId);
        resultDeleteAllFriends.postValue(Resource.loading(null));
        result.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Timber.d("response is: "+response.code());
                Timber.d("response is: "+response.body());
                if (response.isSuccessful()) {
                    Timber.d("Response successful");
                    appExecutors.diskIO().execute(() -> {
                        userDao.updateUser(response.body());
                        resultDeleteAllFriends.postValue(Resource.success("Successfully deleted all friends"));
                    });
                } else {
                    String errorBodyMessage = null;
                    try {
                        errorBodyMessage = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Timber.d("Error for deleteAllFriends is: " + errorBodyMessage);
                    resultDeleteAllFriends.postValue(Resource.error(errorBodyMessage, errorBodyMessage));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Timber.d("response FAIL: "+ t);
                if (t instanceof IOException) {
                    resultDeleteAllFriends.postValue(Resource.error("Network error, please try again", "Network error, please try again"));
                }
                else {
                    resultDeleteAllFriends.postValue(Resource.error("conversion issue! big problems :(", "conversion issue! big problems :("));
                    // todo log to some central bug tracking service
                }
            }
        });
        return resultDeleteAllFriends;
    }

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

      /*public LiveData<Resource<User>> deleteAllFriends(String userId) {
        return new NetworkBoundResource<User, User>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull User user) {
                //Update usera
                userDao.updateUser(user);

            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                Timber.d("Should Fetch: " + data.getName());
                return true;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                Timber.d("LoadFromDB: deleteAllFriends");
                return userDao.findUserById(userId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                Timber.d("Making call deleteAllFriends:");
                LiveData<ApiResponse<User>> result = listopiaService.deleteAllFriends(userId);
                return result;
            }
        }.asLiveData();
    }*/

     /*private final MutableLiveData<Resource<User>> rez = new MutableLiveData<>();

    public LiveData<Resource<User>> deleteAllFriends(String userId) {
        Timber.d("Making call deleteAllFriends");
        LiveData<ApiResponse<User>> apiResponse = listopiaService.deleteAllFriends(userId);
       // Timber.d("Api response: " + apiResponse.getValue().body);
        //LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            //noinspection ConstantConditions
            if (response.isSuccessful()) {
                appExecutors.diskIO().execute(() -> {
                    //saveCallResult(processResponse(response));
                    Timber.d("Body: " + response.body.getName());
                    userDao.updateUser(response.body);

                    appExecutors.mainThread().execute(() ->
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            result.addSource(loadFromDb(),
                                    newData -> setValue(Resource.success(newData)))
                    );
                });
            } else {
                //onFetchFailed();
                result.addSource(dbSource,
                        newData -> setValue(Resource.error(response.errorMessage, newData)));
                Timber.d("Fetch FAILED");
            }
        });
        Call<ApiResponse<User>> ApiResult = listopiaService.deleteAllFriends(userId);
        result.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Timber.d("response is: "+response.code());
                Timber.d("response is: "+response.body());
                Timber.d("response is " + response.body().getEmail());
                if (response.isSuccessful()) {
                    appExecutors.diskIO().execute(() -> {
                        userDao.updateUser(response.body());
                    });
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //call.
                Timber.d("response FAIL: "+ t);
            }
        });
    }*/


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




}
