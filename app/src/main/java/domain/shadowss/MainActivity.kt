package domain.shadowss

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import defpackage.ASAU
import defpackage.CBIN
import org.jetbrains.anko.doAsync
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        doAsync {
            val instance = CBIN.marshal(ASAU())?.let {
                CBIN.unmarshal<ASAU>(it)
            }
            Timber.d("IS ${instance is ASAU}")
        }
    }
}
