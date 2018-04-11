package com.makaji.aleksej.listopia.data.api;

import android.arch.lifecycle.LiveData;

import com.makaji.aleksej.listopia.data.entity.Product;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.data.entity.User;
import com.makaji.aleksej.listopia.data.entity.UserWithFriends;
import com.makaji.aleksej.listopia.data.vo.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Aleksej on 12/29/2017.
 */

/**
 * REST API access points
 */
public interface ListopiaService {

    @GET("api/shoppingLists")
    LiveData<ApiResponse<List<ShoppingList>>> getShoppingLists();

    @GET("api/products")
    LiveData<ApiResponse<List<Product>>> getProductsByShoppingListId();

   /* @POST("api/login")
    Call<User> validateToken(@Body String idToken);*/

    /*@POST("api/login")
    LiveData<ApiResponse<User>> validateToken(@Body String idToken);*/

    @GET("api/login")
    LiveData<ApiResponse<User>> validateToken();

    /*@GET("api/login")
    Call<Resource<User>> validateToken();*/

   /* @GET("api/login")
    LiveData<ApiResponse<User>> validateToken();*/

  /* @Multipart
   @POST("api/user/addUserToFriendsByEmail")
   LiveData<ApiResponse<User>> addUserToFriendsByEmail(@Part("userId") String userId,
                                                       @Part("email") String email);*/

    @POST("api/user/addUserToFriendsByEmail")
    @FormUrlEncoded
    LiveData<ApiResponse<User>> addUserToFriendsByEmail(@Field("userId") String userId,
                                                        @Field("email") String email);

    /*@POST("api/user/addUserToFriendsByEmail")
    @FormUrlEncoded
    LiveData<ApiResponse<UserWithFriends>> addUserToFriendsByEmail(@Field("userId") String userId,
                                                                   @Field("email") String email);*/

    @DELETE("api/user/{userId}/friends")
    Call<User> deleteAllFriends(@Path("userId") String userId);
}
