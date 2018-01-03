package ru.kalugin19.fridge.android.pub.v2.injection.scope

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Scope

/**
 * Scope : Per Activity
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
annotation class PerActivity
