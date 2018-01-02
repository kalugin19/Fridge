package ru.kalugin19.fridge.android.pub.v1.injection.scope

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Scope

/**
 * Scope : Per Fragment
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
annotation class PerFragment
