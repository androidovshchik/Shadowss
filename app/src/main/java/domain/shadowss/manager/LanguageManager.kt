package domain.shadowss.manager

import android.content.Context
import de.siegmar.fastcsv.reader.CsvReader
import domain.shadowss.R
import domain.shadowss.local.Preferences
import domain.shadowss.model.Language
import domain.shadowss.model.TxtData
import timber.log.Timber
import java.util.concurrent.CopyOnWriteArrayList

@Suppress("MemberVisibilityCanBePrivate")
class LanguageManager(context: Context) : Manager {

    @Volatile
    var language = Language.EN.id

    val pack = CopyOnWriteArrayList<TxtData>()

    private val data = arrayListOf<TxtData>()

    /*private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            updatePack(context)
        }
    }*/

    init {
        init(context)
    }

    override fun init(vararg args: Any?) {
        val context = args[0] as Context
        context.resources
            .openRawResource(R.raw.txtdata)
            .bufferedReader().use { reader ->
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
                        } catch (e: Throwable) {
                            Timber.e(e)
                        }
                    } while (true)
                }
            }
        updatePack(context)
        //context.registerReceiver(receiver, IntentFilter(Intent.ACTION_LOCALE_CHANGED))
    }

    fun getText(data: String?): String? {
        return data?.let {
            try {
                var start = 0
                do {
                    start = it.indexOf("[[", start)
                    if (start >= 0) {
                        val end = it.indexOf("]]")
                        if (end >= 0) {

                        } else {
                            throw IllegalArgumentException()
                        }
                    } else {
                        break
                    }
                } while (true)
                return data?.let {
                    val (type, text) = it.split(",")
                    getText(type.trim(), text.trim())
                }
            } catch (e: Throwable) {
                Timber.e(e)
                return pack.firstOrNull { it.typeId == typeId && it.textId == textId }?.text
                null
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun updatePack(context: Context) {
        val preferences = Preferences(context)
        val id = try {

        } catch (e: Throwable) {
            Timber.e(e)
            null
        }
        language = Language.fromId(id).id
        pack.apply {
            clear()
            addAll(data.filter { it.langId == language })
        }
    }

    override fun release(vararg args: Any?) {}
}
