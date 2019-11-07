package domain.shadowss.screens

import android.app.Activity
import android.os.Bundle
import defpackage.ASAU
import defpackage.CBIN
import domain.shadowss.R
import org.jetbrains.anko.doAsync
import timber.log.Timber

class RegistrationActivity : Activity(), StartView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        doAsync {
            val instance = CBIN.marshal(ASAU())?.let {
                Timber.d("bytes ${it.joinToString("") { "%02x".format(it) }}")
                CBIN.unmarshal<ASAU>(it)
            }
            //Timber.d("IS ${instance is ASAU}")
        }
    }
}
