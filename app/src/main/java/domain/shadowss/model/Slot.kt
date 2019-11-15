package domain.shadowss.model

class Slot {

    var imei: String? = null

    var imsi: String? = null

    var simState = -1

    val simStates = hashSetOf<Int>()

    var simSerialNumber: String? = null

    var simOperator: String? = null

    var simCountryIso: String? = null

    fun setSimState(state: Int?) {
        if (state == null) {
            simState = -1
            return
        }
        simState = state
    }

    private fun compare(slot: Slot?): Boolean {
        return if (slot != null) {
            imei == slot.imei && imsi == slot.imsi && simSerialNumber == slot.simSerialNumber
        } else false
    }

    fun indexIn(slots: List<Slot>?): Int {
        if (slots == null) {
            return -1
        }
        for (i in slots.indices) {
            if (compare(slots[i])) {
                return i
            }
        }
        return -1
    }

    fun containsIn(slots: List<Slot>?): Boolean {
        if (slots == null) {
            return false
        }
        for (slot in slots) {
            if (compare(slot)) {
                return true
            }
        }
        return false
    }

    override fun toString(): String {
        return "Slot(" +
            "imei=$imei, " +
            "imsi=$imsi, " +
            "simState=$simState, " +
            "simStates=$simStates, " +
            "simSerialNumber=$simSerialNumber, " +
            "simOperator=$simOperator, " +
            "simCountryIso=$simCountryIso" +
            ")"
    }
}
