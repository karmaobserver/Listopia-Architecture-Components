package com.makaji.aleksej.listopia.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Aleksej on 1/25/2018.
 */

@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScoped {
}
