package com.makaji.aleksej.listopia.ui.user;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Patterns;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.data.entity.User;
import com.makaji.aleksej.listopia.data.entity.UserWithFriends;
import com.makaji.aleksej.listopia.data.repository.UserRepository;
import com.makaji.aleksej.listopia.data.vo.Resource;
import com.makaji.aleksej.listopia.util.AbsentLiveData;
import com.makaji.aleksej.listopia.util.SingleLiveData;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 2/4/2018.
 */

public class UserViewModel extends ViewModel {

    private final MutableLiveData<String> userId = new MutableLiveData<>();
    private LiveData<Resource<User>> user;
    private LiveData<Resource<User>> userWithFriends;
    private final SingleLiveData<Void> findFriendClick = new SingleLiveData<>();
    private final SingleLiveData<Void> addFriendClick = new SingleLiveData<>();
    public final MutableLiveData<String> textEmail = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> errorTextEmail = new MutableLiveData<>();

    @Inject
    UserRepository userRepository;

    @Inject
    Resources resources;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    public UserViewModel(UserRepository userRepository) {
        user = Transformations.switchMap(userId, id -> {
            Timber.d("User Transformation " + id);
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return userRepository.findUserById(id);
            }
        });

        userWithFriends = Transformations.switchMap(email, textEmail -> {
            Timber.d("Friends Transformation " + textEmail);
            if (textEmail == null) {
                return AbsentLiveData.create();
            } else {
                String resUserId = resources.getString(R.string.key_user_id);
                String userId = sharedPreferences.getString(resUserId, "defualtValue");
                Timber.d("Email button: " + textEmail + " UserId button: " + userId);
                return userRepository.addUserToFriendsByEmail(userId, textEmail);
            }
        });
    }

    //Set id to get user (call findUserById)
    public void setId(String id) {
        /*Timber.d("Passed id: " + id + " livedata id: " +userId.getValue());
        if (Objects.equals(userId.getValue(), id)) {
            Timber.d("User ID-s are the SAME!");
            return;
        }*/
        Timber.d("Setting User Id");
        userId.setValue(id);
    }

    public LiveData<Resource<User>> getUser() {
        return user;
    }

   /* public void validateToken() {
        userRepository.validateToken();
    }*/
   /* public MutableLiveData<Resource<User>> validateToken() {
        return userRepository.validateToken();
    }*/

   //TODO change it
   public LiveData<Resource<User>> validateToken(String userId) {
       return userRepository.validateToken(userId);
   }

    //Find friend button click event
    public void onFindFriendClick() {
        findFriendClick.call();
    }

    //Get Find friend button click event
    public SingleLiveData<Void> getFindFriendClick() {
        return findFriendClick;
    }

    //Add friend button click event
    public void onAddFriendClick() {
        if (!validateEmail()) {
            return;
        }
        /*String resUserId = resources.getString(R.string.key_user_id);
        String userId = sharedPreferences.getString(resUserId, "defualtValue");
        Timber.d("Email button: " + textEmail.getValue() + " UserId button: " + userId);
        friends = userRepository.addUserToFriendsByEmail(userId, textEmail.getValue());*/
        email.setValue(textEmail.getValue());
        addFriendClick.call();
    }

    //Get Add friend button click event
    public SingleLiveData<Void> getAddFriendClick() {
        return addFriendClick;
    }

    public LiveData<Resource<User>> getUserWithFriends() {
        return userWithFriends;
    }



    //Get error
    public LiveData<String> getErrorTextEmail() {
        return errorTextEmail;
    }

    /**
     * Validate email address
     * @return
     */
    private boolean validateEmail() {
        boolean isValid = true;

        if (textEmail.getValue() == null) {
            errorTextEmail.setValue("Enter email address");
            isValid = false;
        } else {
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            if (!pattern.matcher(textEmail.getValue()).matches()) {
                errorTextEmail.setValue("Email address is not valid");
                isValid = false;
            }
        }
        return isValid;
    }

    /**
     * Delete all friends for user
     * @return SingleLiveData - to observe error in case happen and notify UI
     */
    public SingleLiveData<Resource<String>> deleteAllFriends() {
        String resUserId = resources.getString(R.string.key_user_id);
        String userId = sharedPreferences.getString(resUserId, "defualtValue");
        Timber.d("User Id for delete: " + userId);
        return userRepository.deleteAllFriends(userId);
    }

   /* public void deleteAllFriends() {
        String resUserId = resources.getString(R.string.key_user_id);
        String userId = sharedPreferences.getString(resUserId, "defualtValue");
        Timber.d("User Id for delete: " + userId);
        errorDeleteAllFriends = userRepository.deleteAllFriends(userId);
        errorDeleteAllFriends.call();
    }

    public SingleLiveData<String> getErrorDeleteAllFriends() {
        return errorDeleteAllFriends;
    }*/




}
