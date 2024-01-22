package com.suhaib.swiftpos.common.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

inline fun <reified T> typeToken(): Type = object : TypeToken<T>() {}.type

inline fun <reified T> String.toDataClassObject(): T = Gson().fromJson(this, typeToken<T>())

inline fun <reified T> T.toDataClassString(): String = Gson().toJson(this, typeToken<T>())