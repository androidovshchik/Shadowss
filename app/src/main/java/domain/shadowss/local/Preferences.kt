package domain.shadowss.local

import android.content.Context
import com.chibatching.kotpref.KotprefModel
import domain.shadowss.model.Language

class Preferences(context: Context) : KotprefModel(context) {

    override val kotprefName: String = "${context.packageName}_preferences"

    var language by stringPref(Language.EN.id, "0x00")

    var agree by booleanPref(false, "0x01")

    var deviceKey by booleanPref(false, "0x03")
}