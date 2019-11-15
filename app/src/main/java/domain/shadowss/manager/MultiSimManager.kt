package domain.shadowss.manager

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.SubscriptionManager
import domain.shadowss.extension.isLollipopMR1Plus
import domain.shadowss.model.Slot
import org.jetbrains.anko.telephonyManager
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*

/**
 * https://mvnrepository.com/artifact/com.kirianov.multisim/multisim
 */
@Suppress("DEPRECATION", "MemberVisibilityCanBePrivate")
class MultiSimManager(context: Context) {

    private val reference = WeakReference(context)

    val slots = arrayListOf<Slot>()
        @Synchronized get

    @Synchronized
    @SuppressLint("MissingPermission")
    fun updateData() = reference.get()?.run {
        try {
            var slotNumber = 0
            while (true) {
                val slot = touchSlot(slotNumber)
                if (slot == null) {
                    for (i in slotNumber until slots.size) {
                        slots.removeAt(i)
                    }
                    break
                }
                if (slot.containsIn(slots) && slot.indexIn(slots) < slotNumber) {
                    // protect from Alcatel infinity bug
                    break
                }
                slots.apply {
                    when {
                        size > slotNumber -> {
                            removeAt(slotNumber)
                            add(slotNumber, slot)
                        }
                        size == slotNumber -> add(slot)
                    }
                }
                slotNumber++
            }
        } catch (e: Throwable) {
            Timber.e(e)
        }
        if (isLollipopMR1Plus()) {
            val subscriptionManager =
                context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
            val list = subscriptionManager.activeSubscriptionInfoList.orEmpty()
            mcc1 = list.getOrNull(0)?.mcc ?: -1
            mcc2 = list.getOrNull(1)?.mcc ?: -1
        }
        slots.filter { it.imsi != null && it.simOperator.isNotEmpty() }
    }

    @Suppress("SpellCheckingInspection")
    @SuppressLint("MissingPermission", "HardwareIds")
    private fun touchSlot(slotNumber: Int): Slot? = reference.get()?.run {
        val slot = Slot()
        val telephonyManager = telephonyManager
        Timber.v("telephonyManager [$telephonyManager] ${telephonyManager.deviceId}")
        val subscriberIdIntValue = ArrayList<String>()
        val subscriberIdIntIndex = ArrayList<Int>()
        for (i in 0..99) {
            val subscriber = runMethodReflect(
                telephonyManager,
                "android.telephony.TelephonyManager",
                "getSubscriberId",
                arrayOf(i),
                null
            ) as? String
            if (subscriber != null && !subscriberIdIntValue.contains(subscriber)) {
                subscriberIdIntValue.add(subscriber)
                subscriberIdIntIndex.add(i)
            }
        }
        var subIdInt =
            if (subscriberIdIntIndex.size > slotNumber) subscriberIdIntIndex[slotNumber] else null
        if (subIdInt == null) {
            try {
                subIdInt = runMethodReflect(
                    telephonyManager,
                    "android.telephony.TelephonyManager",
                    "getSubId",
                    arrayOf(slotNumber),
                    null
                ).toString().toInt()
            } catch (ignored: Throwable) {
            }
        }
        Timber.v("subIdInt $subIdInt")
        val subscriberIdLongValue = ArrayList<String>()
        val subscriberIdLongIndex = ArrayList<Long>()
        for (i in 0L until 100L) {
            val subscriber = runMethodReflect(
                telephonyManager,
                "android.telephony.TelephonyManager",
                "getSubscriberId",
                arrayOf(i),
                null
            ) as? String
            runMethodReflect(
                telephonyManager,
                "android.telephony.TelephonyManagerSprd",
                "getSubInfoForSubscriber",
                arrayOf(i),
                null
            ) ?: continue
            if (subscriber != null && !subscriberIdLongValue.contains(subscriber)) {
                subscriberIdLongValue.add(subscriber)
                subscriberIdLongIndex.add(i)
            }
        }
        if (subscriberIdLongIndex.size <= 0) {
            for (i in 0L until 100L) {
                val subscriber = runMethodReflect(
                    telephonyManager,
                    "android.telephony.TelephonyManager",
                    "getSubscriberId",
                    arrayOf(i),
                    null
                ) as? String
                if (subscriber != null && !subscriberIdLongValue.contains(subscriber)) {
                    subscriberIdLongValue.add(subscriber)
                    subscriberIdLongIndex.add(i)
                }
            }
        }
        var subIdLong =
            if (subscriberIdLongIndex.size > slotNumber) subscriberIdLongIndex[slotNumber] else null
        if (subIdLong == null) {
            subIdLong = runMethodReflect(
                telephonyManager,
                "android.telephony.TelephonyManager",
                "getSubId",
                arrayOf(slotNumber),
                null
            ) as? Long
        }
        Timber.v("subIdLong $subIdLong")
        val listParamsSubs = ArrayList<Any?>()
        if (subIdInt != null && !listParamsSubs.contains(subIdInt)) {
            listParamsSubs.add(subIdInt)
        }
        if (subIdLong != null && !listParamsSubs.contains(subIdLong)) {
            listParamsSubs.add(subIdLong)
        }
        if (!listParamsSubs.contains(slotNumber)) {
            listParamsSubs.add(slotNumber)
        }
        val objectParamsSubs = listParamsSubs.toTypedArray()
        for (i in objectParamsSubs.indices) {
            Timber.v("SPAM PARAMS_SUBS [$i]=[${objectParamsSubs[i]}]")
        }
        val listParamsSlot = ArrayList<Any?>()
        if (!listParamsSlot.contains(slotNumber)) {
            listParamsSlot.add(slotNumber)
        }
        if (subIdInt != null && !listParamsSlot.contains(subIdInt)) {
            listParamsSlot.add(subIdInt)
        }
        if (subIdLong != null && !listParamsSlot.contains(subIdLong)) {
            listParamsSlot.add(subIdLong)
        }
        val objectParamsSlot = listParamsSlot.toTypedArray()
        for (i in objectParamsSlot.indices) {
            Timber.v("SPAM PARAMS_SLOT [$i]=[${objectParamsSlot[i]}]")
        }
        // firstly all Int params, then all Long params
        Timber.v("------------------------------------------")
        Timber.v("SLOT [$slotNumber]")
        slot.imei = iterateMethods("getDeviceId", objectParamsSlot) as? String
        if (slot.imei == null) {
            slot.imei = runMethodReflect(
                null,
                "com.android.internal.telephony.Phone",
                null,
                null,
                "GEMINI_SIM_" + (slotNumber + 1)
            ) as? String
        }
        if (slot.imei == null) {
            slot.imei = runMethodReflect(
                getSystemService("phone" + (slotNumber + 1)),
                null,
                "getDeviceId",
                null,
                null
            ) as? String
        }
        Timber.v("IMEI [${slot.imei}]")
        if (slot.imei == null) {
            when (slotNumber) {
                0 -> {
                    slot.imei = telephonyManager.deviceId
                    slot.imsi = telephonyManager.subscriberId
                    slot.simState = telephonyManager.simState
                    slot.simOperator = telephonyManager.simOperator
                    slot.simSerialNumber = telephonyManager.simSerialNumber
                    slot.simCountryIso = telephonyManager.simCountryIso
                    return slot
                }
            }
        }
        if (slot.imei == null) {
            return null
        }
        slot.setSimState(iterateMethods("getSimState", objectParamsSlot) as? Int)
        Timber.v("SIMSTATE [${slot.simState}]")
        slot.imsi = iterateMethods("getSubscriberId", objectParamsSubs) as? String
        Timber.v("IMSI [${slot.imsi}]")
        slot.simSerialNumber = iterateMethods("getSimSerialNumber", objectParamsSubs) as? String
        Timber.v("SIMSERIALNUMBER [${slot.simSerialNumber}]")
        slot.simOperator = iterateMethods("getSimOperator", objectParamsSubs) as? String
        Timber.v("SIMOPERATOR [${slot.simOperator}]")
        slot.simCountryIso = iterateMethods("getSimCountryIso", objectParamsSubs) as? String
        Timber.v("SIMCOUNTRYISO [${slot.simCountryIso}]")
        Timber.v("------------------------------------------")
        return slot
    }

    @SuppressLint("WrongConstant")
    private fun iterateMethods(methodName: String?, methodParams: Array<Any?>): Any? =
        reference.get()?.run {
            if (methodName == null || methodName.isEmpty()) {
                return null
            }
            val telephonyManager = telephonyManager
            val instanceMethods = ArrayList<Any?>()
            val multiSimTelephonyManagerExists = telephonyManager.toString()
                .startsWith("android.telephony.MultiSimTelephonyManager")
            for (methodParam in methodParams) {
                if (methodParam == null) {
                    continue
                }
                val objectMulti = if (multiSimTelephonyManagerExists) {
                    runMethodReflect(
                        null,
                        "android.telephony.MultiSimTelephonyManager",
                        "getDefault",
                        arrayOf(methodParam),
                        null
                    )
                } else {
                    telephonyManager
                }
                if (!instanceMethods.contains(objectMulti)) {
                    instanceMethods.add(objectMulti)
                }
            }
            if (!instanceMethods.contains(telephonyManager)) {
                instanceMethods.add(telephonyManager)
            }
            val telephonyManagerEx = runMethodReflect(
                null,
                "com.mediatek.telephony.TelephonyManagerEx",
                "getDefault",
                null,
                null
            )
            if (!instanceMethods.contains(telephonyManagerEx)) {
                instanceMethods.add(telephonyManagerEx)
            }
            val phoneMsim = getSystemService("phone_msim")
            if (!instanceMethods.contains(phoneMsim)) {
                instanceMethods.add(phoneMsim)
            }
            if (!instanceMethods.contains(null)) {
                instanceMethods.add(null)
            }
            var result: Any?
            for (methodSuffix in suffixes) {
                for (className in classNames) {
                    for (instanceMethod in instanceMethods) {
                        for (methodParam in methodParams) {
                            if (methodParam == null) {
                                continue
                            }
                            result = runMethodReflect(
                                instanceMethod,
                                className,
                                methodName + methodSuffix,
                                if (multiSimTelephonyManagerExists) null else arrayOf(methodParam),
                                null
                            )
                            if (result != null) {
                                return result
                            }
                        }
                    }
                }
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
        try {
            val classInvoke = when {
                classInvokeName != null -> Class.forName(classInvokeName)
                instanceInvoke != null -> instanceInvoke.javaClass
                else -> return null
            }
            if (field != null) {
                val fieldReflect = classInvoke.getField(field)
                val accessible = fieldReflect.isAccessible
                fieldReflect.isAccessible = true
                result = fieldReflect.get(null).toString()
                fieldReflect.isAccessible = accessible
            } else {
                var classesParams: Array<Class<*>?>? = null
                if (methodParams != null) {
                    classesParams = arrayOfNulls(methodParams.size)
                    for (i in methodParams.indices) {
                        classesParams[i] = when {
                            methodParams[i] is Int -> Int::class.javaPrimitiveType
                            methodParams[i] is Long -> Long::class.javaPrimitiveType
                            methodParams[i] is Boolean -> Boolean::class.javaPrimitiveType
                            else -> methodParams[i].javaClass
                        }
                    }
                }
                val method = if (classesParams != null) {
                    classInvoke.getDeclaredMethod(methodName.toString(), *classesParams)
                } else {
                    classInvoke.getDeclaredMethod(methodName.toString())
                }
                val accessible = method.isAccessible
                method.isAccessible = true
                result = if (methodParams != null) {
                    method.invoke(instanceInvoke ?: classInvoke, *methodParams)
                } else {
                    method.invoke(instanceInvoke ?: classInvoke)
                }
                method.isAccessible = accessible
            }
        } catch (ignored: Throwable) {
        }
        return result
    }

    fun sdsdf() = """
        default telephony [" + context.getSystemService(Context.TELEPHONY_SERVICE) + "]"
        ${printAllMethodsAndFields("android.telephony.TelephonyManager")}
        ${printAllMethodsAndFields("android.telephony.MultiSimTelephonyManager")}
        ${printAllMethodsAndFields("android.telephony.MSimTelephonyManager")}
        ${printAllMethodsAndFields("com.mediatek.telephony.TelephonyManager")}
        ${printAllMethodsAndFields("com.mediatek.telephony.TelephonyManagerEx")}
        ${printAllMethodsAndFields("com.android.internal.telephony.ITelephony")}
    """.trimIndent()

    @SuppressWarnings("ResourceType")
    private fun printAllMethodsAndFields(className: String?): String {
        val builder = StringBuilder()
        builder.append("=========================")
        if (className != null) {
            try {
                val MultiSimClass = Class.forName(className);

                Log.i(LOG, "Methods of [" + className + "] (" + paramsCount + "):");
                for (Method method : MultiSimClass.getMethods()) {
                    if ((method.getParameterTypes().length == paramsCount) || (paramsCount == -1)) {
                        String params = "";
                        for (int i = 0; i < method.getParameterTypes().length; i++)
                        params += method.getParameterTypes()[i].getName() + ", ";
                        Log.i(
                            LOG,
                            " m " + method.getName() + "[" + method.getParameterTypes().length + "](" + (params.length() > 0 ? params . substring (0, params.length()-2) : params)+") -> "+method.getReturnType()+""+(Modifier.isStatic(method.getModifiers()) ? " (static)" : ""));
                    }
                }

                Log.i(LOG, "Fields of [" + className + "]:");
                for (Field field : MultiSimClass.getFields()) {
                    Log.i(LOG, " f " + field.getName() + " " + field.getType());
                }

            } catch (Exception e) {
                Log.i(LOG, "EXC " + e.getMessage());
            }
        }
        return builder.toString()
    }

    companion object {

        private val classNames = arrayOf(
            null,
            "android.telephony.TelephonyManager",
            "android.telephony.MSimTelephonyManager",
            "android.telephony.MultiSimTelephonyManager",
            "com.mediatek.telephony.TelephonyManagerEx",
            "com.android.internal.telephony.Phone",
            "com.android.internal.telephony.PhoneFactory"
        )

        private val suffixes = arrayOf(
            "",
            "Gemini",
            "Ext",
            "Ds",
            "ForSubscription",
            "ForPhone"
        )
    }
}