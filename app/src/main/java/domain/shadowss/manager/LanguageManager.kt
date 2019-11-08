package domain.shadowss.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import de.siegmar.fastcsv.reader.CsvReader
import domain.shadowss.domain.TxtData
import org.jetbrains.anko.doAsync
import timber.log.Timber
import java.lang.ref.WeakReference

class LanguageManager(context: Context) : Manager {

    private val data = arrayListOf<TxtData>()

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

        }
    }

    init {
        init(context)
    }

    override fun init(vararg args: Any?) {
        val contextRef = WeakReference(args[0] as Context)
        doAsync {
            val items = ArrayList<TxtData>()
            try {
                contextRef.get()
                CsvReader().parse(assets.open("data.csv").bufferedReader()).use {
                    it.nextRow()?.let {

                    } ?: if (row == null) {
                        if (items.size > 0) {
                            items[items.size - 1].phrases.addAll(phrases)
                            phrases.clear()
                        }
                        break
                    } else {
                        if (items.size == 0) {
                            items.add(Category(0, row.getField(0), row.getField(1)))
                        } else if (items[items.size - 1].nameRu != row.getField(0)) {
                            items[items.size - 1].phrases.addAll(phrases)
                            items.add(Category(items.size, row.getField(0), row.getField(1)))
                            phrases.clear()
                        }
                        phrases.add(
                            Phrase(
                                phrases.size, items.size - 1, row.getField(2),
                                row.getField(3), row.getField(4)
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        val filter = IntentFilter(Intent.ACTION_LOCALE_CHANGED)
        registerReceiver(mLangReceiver, filter)
    }

    fun getLangPack(): String? {
        return null
    }

    fun getString(var typeId: String, var textId: String): String? {
        return data.firstOrNull { it.typeId == typeId && it.textId == textId }?.text
    }

    override fun release() {}
}
