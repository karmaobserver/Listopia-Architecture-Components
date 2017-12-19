package com.makaji.aleksej.listopia.data.vo;

/**
 * Created by Aleksej on 12/18/2017.
 */

/**
 * Status of a resource that is provided to the UI.
 * <p>
 * These are usually created by the Repository classes where they return
 * {@code LiveData<Resource<T>>} to pass back the latest data to the UI with its fetch status.
 */
public enum Status {
    SUCCESS,
    ERROR,
    LOADING
}
