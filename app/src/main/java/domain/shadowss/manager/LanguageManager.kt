package domain.shadowss.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import de.siegmar.fastcsv.reader.CsvReader
import domain.shadowss.R
import domain.shadowss.domain.TxtData
import domain.shadowss.extension.isNougatPlus
import timber.log.Timber
import java.util.concurrent.CopyOnWriteArrayList

class LanguageManager(context: Context) : Manager {

    val pack = CopyOnWriteArrayList<TxtData>()

    private val data = arrayListOf<TxtData>()

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            updatePack(context)
        }
    }

    init {
        init(context)
    }

    override fun init(vararg args: Any?) {
        val context = args[0] as Context
        val reader = context.resources
            .openRawResource(R.raw.txtdata)
            .bufferedReader()
        CsvReader().parse(reader).use { parser ->
            do {
                try {
                    parser.nextRow()?.let {
                        data.add(TxtData().apply {
                            langId = it.getField(0)
                            typeId = it.getField(1)
                            textId = it.getField(2)
                            text = it.getField(3)
                        })
                    } ?: break
                } catch (e: Exception) {
                    Timber.e(e)
                }
            } while (true)
        }
        updatePack(context)
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_LOCALE_CHANGED))
    }

    fun getString(typeId: String, textId: String): String? {
        return pack.firstOrNull { it.typeId == typeId && it.textId == textId }?.text
    }

    @Suppress("DEPRECATION")
    private fun updatePack(context: Context) {
        if (isNougatPlus()) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }
        pack.apply {
            clear()
            addAll(data.filter { it.langId == "" })
        }
    }

    override fun release(vararg args: Any?) {}
}
