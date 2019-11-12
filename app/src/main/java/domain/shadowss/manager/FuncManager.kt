package domain.shadowss.manager

import android.content.Context
import com.scottyab.rootbeer.RootBeer

class FuncManager : Manager {

    override fun init(vararg args: Any?) {}

    fun checkrights() {

    }

    fun checkinternet() {

    }

    fun wssregconnect() {

    }

    fun checkuid() {

    }

    fun checkroot(context: Context) {
        val rootBeer = RootBeer(context)
        if (rootBeer.isRootedWithoutBusyBoxCheck) {

        }
    }

    fun checksim() {

    }

    fun checkmcc() {

    }

    fun checkregion() {

    }

    fun checkversion() {

    }

    fun checkmobile() {

    }

    override fun release(vararg args: Any?) {}
}
