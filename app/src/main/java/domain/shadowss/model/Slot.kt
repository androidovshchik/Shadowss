package domain.shadowss.model

import android.annotation.TargetApi
import android.os.Build
import android.text.TextUtils
import domain.shadowss.extension.isLollipopMR1Plus

class Slot {

    var imei: String? = null

    var imsi: String? = null

    var simState = -1

    // for duplicates
    val simStates = hashSetOf<Int>()

    var simSerialNumber: String? = null

    var simOperator: String? = null

    var simCountryIso: String? = null

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    var mcc: String? = null

    val isActive: Boolean
        get() = simStates.contains(5)

    fun getMCC(): String? {
        if (isLollipopMR1Plus() && !TextUtils.isEmpty(mcc)) {
            return mcc
        }
        return try {
            simOperator?.substring(0, 3)
        } catch (e: Throwable) {
            null
        }
    }

    fun setSimState(state: Int?) {
        if (state == null) {
            simState = -1
            return
        }
        simState = state
    }

    fun indexIn(slots: List<Slot>): Int {
        for (i in slots.indices) {
            if (imei == slots[i].imei && imsi == slots[i].imsi && simSerialNumber == slots[i].simSerialNumber) {
                return i
            }
        }
        return -1
    }

    override fun toString(): String {
        return "Slot(" +
            "imei=$imei, " +
            "imsi=$imsi, " +
            "simState=$simState, " +
            "simStates=$simStates, " +
            "simSerialNumber=$simSerialNumber, " +
            "simOperator=$simOperator, " +
            "simCountryIso=$simCountryIso, " +
            "mcc=$mcc" +
            ")"
    }
}
