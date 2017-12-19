package com.makaji.aleksej.listopia.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import static com.makaji.aleksej.listopia.data.entity.ShoppingList.TABLE_NAME;

/**
 * Created by Aleksej on 12/16/2017.
 */

@Entity(tableName = TABLE_NAME)
public class ShoppingList {

    public static final String TABLE_NAME = "shopping_list";

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    public ShoppingList(int id, String name) {
        this.id = id;
        this.name = name;
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
}
