package domain.shadowss

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import defpackage.CBIN
import defpackage.SARR
import org.jetbrains.anko.doAsync
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        doAsync {
            val instance = CBIN.main_marshal(SARR())?.let {
                CBIN.main_unmarshal(it, SARR::class.java.name)
            }
            Timber.d("IS ${instance is SARR}")
        }
    }
}
