package com.makaji.aleksej.listopia.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.makaji.aleksej.listopia.data.entity.Product;
import com.makaji.aleksej.listopia.data.entity.User;
import com.makaji.aleksej.listopia.data.entity.UserWithFriends;

/**
 * Created by Aleksej on 2/4/2018.
 */

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("SELECT * FROM user WHERE id = :id")
    LiveData<User> findUserById(String id);

    @Query("SELECT * FROM user WHERE id = :id")
    LiveData<User> findUserWithFriendsById(String id);

    /*@Query("SELECT * FROM user WHERE email = :email")
    LiveData<User> findUserByEmail(String email);*/

    @Update
    void updateUser(User user);

}
