package com.makaji.aleksej.listopia.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import com.makaji.aleksej.listopia.data.database.ListopiaTypeConverters;

import java.util.List;

/**
 * Created by Aleksej on 12/16/2017.
 */

@Entity(tableName = "shopping_list")
public class ShoppingList {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("friends_who_share")
    @TypeConverters(ListopiaTypeConverters.class)
    private List<User> friendsWhoShare;

    private String userId;

    public ShoppingList(int id, String name, List<User> friendsWhoShare, String userId) {
        this.id = id;
        this.name = name;
        this.friendsWhoShare = friendsWhoShare;
        this.userId = userId;
    }

    @Ignore
    public ShoppingList() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<User> getFriendsWhoShare() {
        return friendsWhoShare;
    }

    public void setFriendsWhoShare(List<User> friendsWhoShare) {
        this.friendsWhoShare = friendsWhoShare;
    }
}
