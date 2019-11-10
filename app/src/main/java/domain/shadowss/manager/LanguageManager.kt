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
    }

    fun getText(data: String): String? {
        return try {
            var start = -2
            var result = data
            do {
                start = result.indexOf("[[", start + 2)
                if (start >= 0) {
                    val end = result.indexOf("]]", start + 2)
                    if (end >= 0) {
                        val id = result.substring(start + 2, end).trim()
                        val (typeId, textId) = id.split(",")
                        val text =
                            pack.firstOrNull { it.typeId == typeId && it.textId == textId }?.text
                        if (text != null) {
                            result = result.replaceRange(start, end + 2, text)
                        }
                    } else {
                        throw IllegalArgumentException()
                    }
                } else {
                    break
                }
            } while (true)
            return result
        } catch (e: Throwable) {
            Timber.e(e)
            null
        }
    }

    @Suppress("DEPRECATION")
    fun updatePack(context: Context) {
        val preferences = Preferences(context)
        val id = try {
            // todo mcc
            preferences.language
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
