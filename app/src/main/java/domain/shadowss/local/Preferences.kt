package domain.shadowss.local

import android.content.Context
import com.chibatching.kotpref.KotprefModel
import domain.shadowss.domain.Language

class Preferences(context: Context) : KotprefModel(context) {

    override val kotprefName: String = "${context.packageName}_preferences"

    var language by stringPref(Language.EN.id, "0x00")
}