package com.makaji.aleksej.listopia.data.database;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.util.StringUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makaji.aleksej.listopia.data.entity.User;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by Aleksej on 2/21/2018.
 */

public class ListopiaTypeConverters {


    //Gson gson = new Gson();

    @TypeConverter
    public static List<User> stringToUser(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<User>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<User> users) {
        Gson gson = new Gson();
        return gson.toJson(users);
    }
}
