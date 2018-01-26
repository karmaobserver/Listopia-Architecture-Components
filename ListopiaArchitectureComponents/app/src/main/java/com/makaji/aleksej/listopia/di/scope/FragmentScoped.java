package com.makaji.aleksej.listopia.di.scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Scope;

/**
 * Created by Aleksej on 1/25/2018.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface FragmentScoped {}
