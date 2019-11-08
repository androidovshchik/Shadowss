package domain.shadowss.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import de.siegmar.fastcsv.reader.CsvReader
import domain.shadowss.domain.TxtData
import timber.log.Timber
import java.util.concurrent.CopyOnWriteArrayList

class LanguageManager(context: Context) : Manager {

    val pack = CopyOnWriteArrayList<TxtData>()

    private val data = arrayListOf<TxtData>()

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

        }
    }

    init {
        init(context)
    }

    override fun init(vararg args: Any?) {
        val context = args[0] as Context
        try {
            CsvReader().parse(context.assets.open("data.csv").bufferedReader()).use {
                do {
                    it.nextRow()?.let { row ->
                        row.getField(0)
                        row.getField(1)
                        row.getField(2)
                        row.getField(3)
                    } ?: break
                } while (true)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_LOCALE_CHANGED))
    }

    fun getString(typeId: String, textId: String): String? {
        return pack.firstOrNull { it.typeId == typeId && it.textId == textId }?.text
    }

    override fun release(vararg args: Any?) {}
}
