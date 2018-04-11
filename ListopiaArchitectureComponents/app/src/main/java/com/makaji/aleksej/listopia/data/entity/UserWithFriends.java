package com.makaji.aleksej.listopia.data.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by Aleksej on 2/24/2018.
 */

public class UserWithFriends {
    @Embedded
    public User user;

    @Relation(parentColumn = "id", entityColumn = "id", entity = User.class)
    public List<User> friends; // or use simply 'List pets;'


   /* Alternatively you can use projection to fetch a specific column (i.e. only name of the pets) from related Pet table. You can uncomment and try below;

   @Relation(parentColumn = "id", entityColumn = "userId", entity = Pet.class, projection = "name")
   public List<String> pets;
   */
}

