@file:Suppress("unused", "DEPRECATION")

package domain.shadowss.extension

import android.net.ConnectivityManager

val ConnectivityManager.isConnected: Boolean
    get() = activeNetworkInfo?.isConnected == true