package com.core.compass.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by CuongCK on 2/13/17.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerService {
}
