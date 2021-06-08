package com.core.compass.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by CuongCK on 2/13/17.
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainInfo {
}
