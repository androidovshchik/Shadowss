package com.kirianov.multisim

class Slot {

    var imei: String? = null

    var imsi: String? = null

    var simState = -1

    var simSerialNumber: String? = null

    var simOperator: String? = null

    var simOperatorName: String? = null

    var simCountryIso: String? = null

    var networkOperator: String? = null

    var networkOperatorName: String? = null

    var networkCountryIso: String? = null

    var networkType = 0

    // careful find networkTypeName because it will be different with networkType on same devices
    var isNetworkRoaming = false

    // private String msisdn; // not posible now

    fun setSimState(state: Int?) {
        if (state == null) {
            simState = -1
            return
        }
        simState = state
    }

    fun setNetworkType(type: Int?) {
        if (type == null) {
            networkType = 0
            return
        }
        networkType = type
    }

    fun setNetworkRoaming(networkRoaming: Boolean?) {
        if (networkRoaming == null) {
            isNetworkRoaming = false
            return
        }
        isNetworkRoaming = networkRoaming
    }

    override fun toString(): String {
        return "imei=[" + imei +
            "] imsi=[" + imsi +
            "] simState=[" + simState +
            "] simSerialNumber=[" + simSerialNumber +
            "] simOperator=[" + simOperator +
            "] simOperatorName=[" + simOperatorName +
            "] simCountryIso=[" + simCountryIso +
            "] networkOperator=[" + networkOperator +
            "] networkOperatorName=[" + networkOperatorName +
            "] networkCountryIso=[" + networkCountryIso +
            "] networkType=[" + networkType +
            "] networkRoaming=[" + isNetworkRoaming + "]"
    }

    fun compare(slot: Slot?): Boolean {
        return if (slot == null) {
            false
        } else {
            imei == slot.imei && imsi == slot.imsi && simSerialNumber == slot.simSerialNumber
        }// &&
        // (("" + getSimOperator()).compareTo("" + slot.getSimOperator()) == 0) &&
        // (("" + getSimOperatorName()).compareTo("" + slot.getSimOperatorName()) == 0) &&
        // (("" + getSimCountryIso()).compareTo("" + slot.getSimCountryIso()) == 0) &&
        // (getSimState() == slot.getSimState()) &&
        // (("" + getSimSerialNumber()).compareTo("" + slot.getSimSerialNumber()) == 0) &&
        // (("" + getNetworkOperator()).compareTo("" + slot.getNetworkOperator()) == 0) &&
        // (("" + getNetworkOperatorName()).compareTo("" + slot.getNetworkOperatorName()) == 0) &&
        // (("" + getNetworkCountryIso()).compareTo("" + slot.getNetworkCountryIso()) == 0) &&
        // (getNetworkType() == slot.getNetworkType()) &&
        // (isNetworkRoaming() == slot.isNetworkRoaming())
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
}
