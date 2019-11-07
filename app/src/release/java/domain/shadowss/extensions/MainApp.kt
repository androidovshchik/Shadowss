@file:Suppress("unused")

package domain.shadowss.extensions

import android.content.Context
import okhttp3.OkHttpClient

fun Context.noopInit() {}

fun OkHttpClient.Builder.noopInit() {}