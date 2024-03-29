package domain.shadowss.local

import android.content.Context
import com.chibatching.kotpref.KotprefModel

class Preferences(context: Context) : KotprefModel(context) {

    override val kotprefName: String = "${context.packageName}_preferences"

    var language by nullableStringPref(null, "0x00")

    var agree by booleanPref(false, "0x01")

    var user by intPref(0, "0x02")

    var token by nullableStringPref(null, "0x03")
}