package domain.shadowss.data.local

import android.content.Context
import com.chibatching.kotpref.KotprefModel

class Preferences(context: Context) : KotprefModel(context) {

    override val kotprefName: String = "${context.packageName}_preferences"
}