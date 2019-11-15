package com.kirianov.multisim

import android.Manifest
import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import timber.log.Timber
import java.util.*

@Suppress("RedundantEmptyInitializerBlock")
class MultiSimTelephonyManager(context: Context) {

    private val context = context.applicationContext

    val slots = ArrayList<Slot>()
        @Synchronized get

    init {
        // printAllMethodsAndFields("android.telephony.TelephonyManager", -1); // all methods
        // printAllMethodsAndFields("android.telephony.MultiSimTelephonyManager", -1); // all methods
        // printAllMethodsAndFields("android.telephony.MultiSimTelephonyManager", 0); // methods with 0 params
        // printAllMethodsAndFields("android.telephony.MultiSimTelephonyManager", 1); // methods with 1 params
        // printAllMethodsAndFields("android.telephony.MultiSimTelephonyManager", 2); // methods with 2 params
        // printAllMethodsAndFields("android.telephony.MSimTelephonyManager", -1); // all methods
        // printAllMethodsAndFields("com.mediatek.telephony.TelephonyManager", -1); // all methods
        // printAllMethodsAndFields("com.mediatek.telephony.TelephonyManagerEx", -1); // all methods
        // printAllMethodsAndFields("com.android.internal.telephony.ITelephony", -1); // all methods
        // printAllMethodsAndFields("com.android.internal.telephony.ITelephony$Stub$Proxy", -1); // all methods
    }

    @Synchronized
    fun getSlot(location: Int): Slot? {
        return slots.getOrNull(location)
    }

    @Synchronized
    fun updateSlots() {
        var slotNumber = 0
        while (true) {
            val slot = touchSlot(slotNumber)
            if (slot == null) {
                for (i in slotNumber until slots.size) {
                    slots.removeAt(i)
                }
                break
            }
            // Log( "slot.containsIn(slots) " + slot.getImei() + " " + slot.containsIn( slots));
            if (slot.containsIn(slots) && slot.indexIn(slots) < slotNumber) {
                // protect from Alcatel infinity bug
                break
            }
            setSlot(slotNumber, slot)
            slotNumber++
        }
    }

    private fun touchSlot(slotNumber: Int): Slot? {
        val slot = Slot()
        val telephonyManager =
            context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        Timber.d("telephonyManager [" + telephonyManager + "] " + telephonyManager.deviceId)

        val subscriberIdIntValue = ArrayList<String>()
        val subscriberIdIntIndex = ArrayList<Int>()
        for (i in 0..99) {
            val subscriber: String?
            subscriber = runMethodReflect(
                context!!.getSystemService(Context.TELEPHONY_SERVICE),
                "android.telephony.TelephonyManager",
                "getSubscriberId",
                arrayOf(i),
                null
            ) as String?
            if (subscriber != null && !subscriberIdIntValue.contains(subscriber)) {
                subscriberIdIntValue.add(subscriber)
                subscriberIdIntIndex.add(i)
            }
        }
        var subIdInt: Int? =
            if (subscriberIdIntIndex.size > slotNumber) subscriberIdIntIndex[slotNumber] else null
        try {
            if (subIdInt == null)
                subIdInt = Integer.parseInt(
                    "" + runMethodReflect(
                        context!!.getSystemService(Context.TELEPHONY_SERVICE),
                        "android.telephony.TelephonyManager",
                        "getSubId",
                        arrayOf(slotNumber),
                        null
                    )!!
                )
        } catch (ignored: Exception) {
        }

        Timber.d("subIdInt " + subIdInt!!)

        val subscriberIdLongValue = ArrayList<String>()
        val subscriberIdLongIndex = ArrayList<Long>()
        for (i in 0L..100L - 1) {
            val subscriber = runMethodReflect(
                context!!.getSystemService(Context.TELEPHONY_SERVICE),
                "android.telephony.TelephonyManager",
                "getSubscriberId",
                arrayOf(i),
                null
            ) as String?
            if (runMethodReflect(
                    context!!.getSystemService(Context.TELEPHONY_SERVICE),
                    "android.telephony.TelephonyManagerSprd",
                    "getSubInfoForSubscriber",
                    arrayOf(i),
                    null
                ) == null
            )
                continue
            if (subscriber != null && !subscriberIdLongValue.contains(subscriber)) {
                subscriberIdLongValue.add(subscriber)
                subscriberIdLongIndex.add(i)
            }
        }
        if (subscriberIdLongIndex.size <= 0)
            for (i in 0L..100L - 1) {
                val subscriber = runMethodReflect(
                    context!!.getSystemService(Context.TELEPHONY_SERVICE),
                    "android.telephony.TelephonyManager",
                    "getSubscriberId",
                    arrayOf(i),
                    null
                ) as String?
                if (subscriber != null && !subscriberIdLongValue.contains(subscriber)) {
                    subscriberIdLongValue.add(subscriber)
                    subscriberIdLongIndex.add(i)
                }
            }
        var subIdLong: Long? =
            if (subscriberIdLongIndex.size > slotNumber) subscriberIdLongIndex[slotNumber] else null
        if (subIdLong == null)
            subIdLong = runMethodReflect(
                context!!.getSystemService(Context.TELEPHONY_SERVICE),
                "android.telephony.TelephonyManager",
                "getSubId",
                arrayOf(slotNumber),
                null
            ) as Long?
        Timber.d("subIdLong " + subIdLong!!)

        val listParamsSubs = ArrayList<Any>()
        if (subIdInt != null && !listParamsSubs.contains(subIdInt))
            listParamsSubs.add(subIdInt)
        if (subIdLong != null && !listParamsSubs.contains(subIdLong))
            listParamsSubs.add(subIdLong)
        if (!listParamsSubs.contains(slotNumber))
            listParamsSubs.add(slotNumber)
        val objectParamsSubs = listParamsSubs.toTypedArray()
        for (i in objectParamsSubs.indices)
            Timber.d("SPAM PARAMS_SUBS [" + i + "]=[" + objectParamsSubs[i] + "]")

        val listParamsSlot = ArrayList<Any>()
        if (!listParamsSlot.contains(slotNumber))
            listParamsSlot.add(slotNumber)
        if (subIdInt != null && !listParamsSlot.contains(subIdInt))
            listParamsSlot.add(subIdInt)
        if (subIdLong != null && !listParamsSlot.contains(subIdLong))
            listParamsSlot.add(subIdLong)
        val objectParamsSlot = listParamsSlot.toTypedArray()
        for (i in objectParamsSlot.indices)
            Timber.d("SPAM PARAMS_SLOT [" + i + "]=[" + objectParamsSlot[i] + "]")

        // for 6.0+ android sdk - uncomment after upgrade builder to SDK 23+ (now SDK 18 with apache.http*)
        //if(Build.VERSION.SDK_INT >= 23) {
        //    slot.setImei(telephonyManager.getDeviceId(slotNumber));
        //}

        // firstly all Int params, then all Long params
        Timber.d("------------------------------------------")
        // imei
        Timber.d("SLOT [$slotNumber]")
        //if( slot.getImei() == null)
        slot.imei = spamMethods("getDeviceId", objectParamsSlot) as String?
        if (slot.imei == null)
            slot.imei = runMethodReflect(
                null,
                "com.android.internal.telephony.Phone",
                null,
                null,
                "GEMINI_SIM_" + (slotNumber + 1)
            ) as String?
        if (slot.imei == null)
            slot.imei = runMethodReflect(
                context!!.getSystemService("phone" + (slotNumber + 1)),
                null,
                "getDeviceId",
                null,
                null
            ) as String?
        // if( slot.getImei() == null)
        //     slot.setImei((String) runMethodReflect(null, "com.android.internal.telephony.PhoneFactory", "getServiceName", new Object[]{Context.TELEPHONY_SERVICE, slotNumber}, null));
        Timber.d("IMEI [" + slot.imei + "]")
        // default one sim phone
        if (slot.imei == null)
            when (slotNumber) {
                0 -> {
                    slot.imei = telephonyManager.deviceId
                    slot.imsi = telephonyManager.subscriberId
                    slot.simState = telephonyManager.simState
                    slot.simOperator = telephonyManager.simOperator
                    slot.simOperatorName = telephonyManager.simOperatorName
                    slot.simSerialNumber = telephonyManager.simSerialNumber
                    slot.simCountryIso = telephonyManager.simCountryIso
                    slot.networkOperator = telephonyManager.networkOperator
                    slot.networkOperatorName = telephonyManager.networkOperatorName
                    slot.networkCountryIso = telephonyManager.networkCountryIso
                    slot.networkType = telephonyManager.networkType
                    slot.isNetworkRoaming = telephonyManager.isNetworkRoaming
                    return slot
                }
            }
        if (slot.imei == null) return null
        // simState
        slot.setSimState(spamMethods("getSimState", objectParamsSlot) as Int?)
        Timber.d("SIMSTATE [" + slot.simState + "]")
        // if( (slot.getSimState() == TelephonyManager.SIM_STATE_UNKNOWN) || (slot.getSimState() == -1))
        //    return slot;
        // imsi
        slot.imsi = spamMethods("getSubscriberId", objectParamsSubs) as String?
        Timber.d("IMSI [" + slot.imsi + "]")
        // simSerialNumber
        slot.simSerialNumber = spamMethods("getSimSerialNumber", objectParamsSubs) as String?
        Timber.d("SIMSERIALNUMBER [" + slot.simSerialNumber + "]")
        // simOperator
        slot.simOperator = spamMethods("getSimOperator", objectParamsSubs) as String?
        Timber.d("SIMOPERATOR [" + slot.simOperator + "]")
        // simOperatorName
        slot.simOperatorName = spamMethods("getSimOperatorName", objectParamsSubs) as String?
        Timber.d("SIMOPERATORNAME [" + slot.simOperatorName + "]")
        // simCountryIso
        slot.simCountryIso = spamMethods("getSimCountryIso", objectParamsSubs) as String?
        Timber.d("SIMCOUNTRYISO [" + slot.simCountryIso + "]")
        // networkOperator
        slot.networkOperator = spamMethods("getNetworkOperator", objectParamsSubs) as String?
        Timber.d("NETWORKOPERATOR [" + slot.networkOperator + "]")
        // networkOperatorName
        slot.networkOperatorName =
            spamMethods("getNetworkOperatorName", objectParamsSubs) as String?
        Timber.d("NETWORKOPERATORNAME [" + slot.networkOperatorName + "]")
        // networkCountryIso
        slot.networkCountryIso = spamMethods("getNetworkCountryIso", objectParamsSubs) as String?
        Timber.d("NETWORKCOUNTRYISO [" + slot.networkCountryIso + "]")
        // networkType
        slot.setNetworkType(spamMethods("getNetworkType", objectParamsSubs) as Int?)
        Timber.d("NETWORKTYPE [" + slot.networkType + "]")
        // networkRoaming
        slot.setNetworkRoaming(spamMethods("isNetworkRoaming", objectParamsSubs) as Boolean?)
        Timber.d("NETWORKROAMING [" + slot.isNetworkRoaming + "]")
        Timber.d("------------------------------------------")
        return slot
    }

    private fun setSlot(location: Int, slot: Slot?): Boolean {
        var updated = false
        try {
            if (slot == null) {
                if (slots.size > location) {
                    slots.removeAt(location)
                    updated = true
                }
            } else {
                if (slots.size > location) {
                    if (!slot.compare(slots[location])) {
                        updated = true
                    }
                    slots[location] = slot
                } else {
                    slots.add(location, slot)
                    updated = true
                }
            }
        } catch (ignored: Exception) {
        }
        return updated
    }

    private fun spamMethods(methodName: String?, methodParams: Array<Any>): Any? {
        if (methodName == null || methodName.length <= 0) return null
        val instanceMethods = ArrayList<Any>()
        var multiSimTelephonyManagerExists = false
        try {
            multiSimTelephonyManagerExists =
                context!!.getSystemService(Context.TELEPHONY_SERVICE).toString()
                    .startsWith("android.telephony.MultiSimTelephonyManager")
            for (i in methodParams.indices) {
                if (methodParams[i] == null) continue
                val objectMulti = if (multiSimTelephonyManagerExists)
                    if (methodParams.size >= 1)
                        runMethodReflect(
                            null,
                            "android.telephony.MultiSimTelephonyManager",
                            "getDefault",
                            arrayOf(methodParams[i]),
                            null
                        )
                    else
                        runMethodReflect(
                            null,
                            "android.telephony.MultiSimTelephonyManager",
                            "getDefault",
                            null,
                            null
                        )
                else
                    context!!.getSystemService(Context.TELEPHONY_SERVICE)
                if (!instanceMethods.contains(objectMulti))
                    instanceMethods.add(objectMulti)
            }
        } catch (ignored: Exception) {
        }

        if (!instanceMethods.contains(context!!.getSystemService(Context.TELEPHONY_SERVICE)))
            instanceMethods.add(context!!.getSystemService(Context.TELEPHONY_SERVICE))
        if (!instanceMethods.contains(
                runMethodReflect(
                    null,
                    "com.mediatek.telephony.TelephonyManagerEx",
                    "getDefault",
                    null,
                    null
                )
            )
        )
            instanceMethods.add(
                runMethodReflect(
                    null,
                    "com.mediatek.telephony.TelephonyManagerEx",
                    "getDefault",
                    null,
                    null
                )
            )
        if (!instanceMethods.contains(context!!.getSystemService("phone_msim")))
            instanceMethods.add(context!!.getSystemService("phone_msim"))
        if (!instanceMethods.contains(null))
            instanceMethods.add(null)
        //        String[] classNames = {
        //                null,
        //                "android.telephony.TelephonyManager",
        //                "android.telephony.MSimTelephonyManager",
        //                "android.telephony.MultiSimTelephonyManager",
        //                "com.mediatek.telephony.TelephonyManagerEx",
        //                "com.android.internal.telephony.Phone",
        //                "com.android.internal.telephony.PhoneFactory"
        //        };
        val classNames = generateClassNames()


        //        String[] methodSuffixes = {
        //                "",
        //                "Gemini",
        //                "Ext",
        //                "Ds",
        //                "ForSubscription",
        //                "ForPhone"
        //        };
        val methodSuffixes = generateMethodSuffix()
        var result: Any?
        for (methodSuffix in methodSuffixes)
            for (className in classNames)
                for (instanceMethod in instanceMethods)
                    for (i in methodParams.indices) {
                        if (methodParams[i] == null) continue
                        result = runMethodReflect(
                            instanceMethod,
                            className,
                            methodName + methodSuffix,
                            if (multiSimTelephonyManagerExists) null else arrayOf(methodParams[i]),
                            null
                        )
                        if (result != null)
                            return result
                    }
        return null
    }

    private fun runMethodReflect(
        instanceInvoke: Any?,
        classInvokeName: String?,
        methodName: String?,
        methodParams: Array<Any>?,
        field: String?
    ): Any? {
        var result: Any? = null
        var classInvoke: Class<*>? = null
        var classesParams: Array<Class<*>>? = null
        // String logString = "";
        try {
            if (classInvokeName != null)
                classInvoke = Class.forName(classInvokeName)
            else if (instanceInvoke != null)
                classInvoke = Class.forName(instanceInvoke.javaClass.name)
            // logString += "" + (classInvoke != null ? classInvoke.getName() : null) + ".";
            if (classInvoke != null) {
                if (field != null) {
                    // logString += field;
                    val fieldReflect = classInvoke.getField(field)
                    val accessible = fieldReflect.isAccessible
                    fieldReflect.isAccessible = true
                    result = fieldReflect.get(null).toString()
                    fieldReflect.isAccessible = accessible
                } else {
                    // logString += methodName + "(";
                    if (methodParams != null) {
                        classesParams = arrayOfNulls(methodParams.size)
                        for (i in methodParams.indices) {
                            if (methodParams[i] is String) {
                                classesParams[i] = String::class.java
                                // logString += "\"" + methodParams[i] + "\",";
                            } else if (methodParams[i] is Int) {
                                classesParams[i] = Int::class.javaPrimitiveType
                                // logString += methodParams[i] + ",";
                            } else if (methodParams[i] is Long) {
                                classesParams[i] = Long::class.javaPrimitiveType
                                // logString += methodParams[i] + ",";
                            } else if (methodParams[i] is Boolean) {
                                classesParams[i] = Boolean::class.javaPrimitiveType
                                // logString += methodParams[i] + ",";
                            } else {
                                classesParams[i] = methodParams[i].javaClass
                                // logString += "["+methodParams[i]+"]" + ",";
                            }
                        }
                    }
                    // if (logString.endsWith(","))
                    //     logString = logString.substring(0, logString.length() - 1);
                    // logString += ")";
                    try {
                        val method =
                            classInvoke.getDeclaredMethod(methodName!!, *classesParams!!)
                        val accessible = method.isAccessible
                        method.isAccessible = true
                        result = method.invoke(instanceInvoke ?: classInvoke, *methodParams)
                        method.isAccessible = accessible
                    } catch (ignored: Exception) {
                    }

                }
            }
        } catch (ignored: Exception) {
            // Log.i(LOG, "EXC " + ignored.getMessage());
        }

        // Log("" + logString + " = [" + result + "]");
        return result
    }

    private external fun generateClassNames(): Array<String>

    private external fun generateMethodSuffix(): Array<String>

    fun destroy() {
        (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
            .listen(null, PhoneStateListener.LISTEN_NONE)
    }

    companion object {

        private val PERMISSIONS = arrayOf(Manifest.permission.READ_PHONE_STATE)

        init {
            try {
                System.loadLibrary("multisimlib")
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}