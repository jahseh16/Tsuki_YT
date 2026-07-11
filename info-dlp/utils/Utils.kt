package com.zionhuang.music.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import com.zionhuang.music.db.entities.LyricsEntity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

// DataStore configuration
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tsuki_settings")

// Generic get helper for Datastore
fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>, defaultValue: T): T = runBlocking {
    this@get.data.first()[key] ?: defaultValue
}

operator fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>): T? = runBlocking {
    this@get.data.first()[key]
}

// Delegate property for Enum preferences
fun <T : Enum<T>> enumPreference(
    context: Context,
    key: Preferences.Key<String>,
    defaultValue: T
): ReadOnlyProperty<Any?, T> {
    return object : ReadOnlyProperty<Any?, T> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            val valueString = runBlocking { context.dataStore.data.first()[key] }
            if (valueString == null) return defaultValue
            return try {
                java.lang.Enum.valueOf(defaultValue.declaringClass, valueString)
            } catch (e: Exception) {
                defaultValue
            }
        }
    }
}

// Network state checker
fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

// Error reporting stub
fun reportException(throwable: Throwable) {
    Log.e("TsukiYT", "Exception reported", throwable)
}

// Translation Helper stub
object TranslationHelper {
    suspend fun translate(lyrics: LyricsEntity): LyricsEntity {
        // Under construction/stub: simply return original lyrics
        return lyrics
    }
}

@Composable
fun <T> rememberPreference(key: Preferences.Key<T>, defaultValue: T): MutableState<T> {
    return remember { mutableStateOf(defaultValue) }
}

@Composable
fun <T : Enum<T>> rememberEnumPreference(key: Preferences.Key<String>, defaultValue: T): MutableState<T> {
    return remember { mutableStateOf(defaultValue) }
}

fun joinByBullet(vararg strings: String?): String {
    return strings.filter { !it.isNullOrBlank() }.joinToString(" • ")
}

fun makeTimeString(millis: Long): String {
    val totalSeconds = millis / 1000
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

fun urlEncode(value: String): String {
    return java.net.URLEncoder.encode(value, "UTF-8")
}

object Updater {
    var lastCheckTime: Long = 0L
    suspend fun getLatestVersionName(): Result<String> = Result.success(com.zionhuang.music.BuildConfig.VERSION_NAME)
}
