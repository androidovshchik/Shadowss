package domain.shadowss.manager

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.telephony.SubscriptionManager
import android.text.TextUtils
import domain.shadowss.extension.areGranted
import domain.shadowss.extension.isLollipopMR1Plus
import domain.shadowss.extension.isMarshmallowPlus
import domain.shadowss.extension.isOreoPlus
import domain.shadowss.model.Slot
import org.jetbrains.anko.telephonyManager
import timber.log.Timber
import java.lang.ref.WeakReference
import java.lang.reflect.Modifier
import java.util.*

/**
 * For better performance should be called on background thread
 * https://mvnrepository.com/artifact/com.kirianov.multisim/multisim
 */
@Suppress("MemberVisibilityCanBePrivate", "DEPRECATION")
class MultiSimManager(context: Context) {

    private val reference = WeakReference(context)

    private val slots = arrayListOf<Slot>()

    fun getSlots(): MutableList<Slot> {
        return Collections.synchronizedList(slots)
    }

    @Synchronized
    @SuppressLint("MissingPermission", "HardwareIds")
    fun updateInfo(): String? = reference.get()?.run {
        if (!areGranted(Manifest.permission.READ_PHONE_STATE)) {
            return null
        }
        val telephonyManager = telephonyManager
        val slots = getSlots()
        synchronized(slots) {
            slots.clear()
            val error = try {
                Timber.v("telephonyManager [$telephonyManager] ${telephonyManager.deviceId}")
                val subscriberIdIntValue = arrayListOf<String>()
                val subscriberIdIntIndex = arrayListOf<Int>()
                for (i in 0 until 100) {
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
                val subscriberIdLongValue = arrayListOf<String>()
                val subscriberIdLongIndex = arrayListOf<Long>()
                for (i in 0L until 100L) {
                    runMethodReflect(
                        telephonyManager,
                        "android.telephony.TelephonyManagerSprd",
                        "getSubInfoForSubscriber",
                        arrayOf(i),
                        null
                    ) ?: continue
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
                if (subscriberIdLongIndex.isEmpty()) {
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
                var slotNumber = 0
                for (i in 0 until 100) {
                    val subIdInt = subscriberIdIntIndex.getOrNull(slotNumber)
                    val subIdLong = subscriberIdLongIndex.getOrNull(slotNumber)
                    val slot = touchSlot(slotNumber, subIdInt, subIdLong) ?: break
                    if (slot.indexIn(slots) in 0 until slotNumber) {
                        // protect from Alcatel infinity bug
                        break
                    }
                    slots.add(slot)
                    slotNumber++
                }
                null
            } catch (e: Throwable) {
                Timber.e(e)
                e.toString()
            }
            slots.apply {
                removeAll { it.imsi == null || it.simOperator?.trim()?.isEmpty() != false }
                val imsi = arrayListOf<String?>()
                val iterator = iterator()
                while (iterator.hasNext()) {
                    val slot = iterator.next()
                    if (imsi.contains(slot.imsi)) {
                        iterator.remove()
                    } else {
                        imsi.add(slot.imsi)
                        slot.simStates.addAll(slots.filter { it.imsi == slot.imsi }.map { it.simState })
                    }
                }
            }
            if (isLollipopMR1Plus()) {
                val subscriptionManager =
                    getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
                val list = subscriptionManager.activeSubscriptionInfoList
                list?.forEach { info ->
                    slots.filter { it.simSerialNumber == info.iccId }.forEach {
                        it.mcc = info.mcc.toString().padStart(3, '0')
                    }
                }
            }
            error
        }
    }

    @Suppress("SpellCheckingInspection")
    @SuppressLint("MissingPermission", "HardwareIds")
    private fun touchSlot(slotNumber: Int, subIdI: Int?, subIdL: Long?): Slot? =
        reference.get()?.run {
            val telephonyManager = telephonyManager
            Timber.v("------------------------------------------")
            Timber.v("SLOT [$slotNumber]")
            val subIdInt = subIdI ?: try {
                runMethodReflect(
                    telephonyManager,
                    "android.telephony.TelephonyManager",
                    "getSubId",
                    arrayOf(slotNumber),
                    null
                ).toString().toInt()
            } catch (ignored: Throwable) {
                null
            }
            Timber.v("subIdInt $subIdInt")
            val subIdLong = subIdL ?: runMethodReflect(
                telephonyManager,
                "android.telephony.TelephonyManager",
                "getSubId",
                arrayOf(slotNumber),
                null
            ) as? Long
            Timber.v("subIdLong $subIdLong")
            val objectParamsSubs = arrayListOf<Any>().apply {
                if (subIdInt != null && !contains(subIdInt)) {
                    add(subIdInt)
                }
                if (subIdLong != null && !contains(subIdLong)) {
                    add(subIdLong)
                }
                if (!contains(slotNumber)) {
                    add(slotNumber)
                }
            }.toTypedArray()
            for (i in objectParamsSubs.indices) {
                Timber.v("ITERATE PARAMS_SUBS [$i]=[${objectParamsSubs[i]}]")
            }
            val objectParamsSlot = arrayListOf<Any>().apply {
                if (!contains(slotNumber)) {
                    add(slotNumber)
                }
                if (subIdInt != null && !contains(subIdInt)) {
                    add(subIdInt)
                }
                if (subIdLong != null && !contains(subIdLong)) {
                    add(subIdLong)
                }
            }.toTypedArray()
            for (i in objectParamsSlot.indices) {
                Timber.v("ITERATE PARAMS_SLOT [$i]=[${objectParamsSlot[i]}]")
            }
            return Slot().apply {
                if (isMarshmallowPlus()) {
                    imei = telephonyManager.getDeviceId(slotNumber)
                }
                if (imei == null) {
                    imei = iterateMethods("getDeviceId", objectParamsSlot) as? String
                }
                if (imei == null) {
                    imei = runMethodReflect(
                        null,
                        "com.android.internal.telephony.Phone",
                        null,
                        null,
                        "GEMINI_SIM_${slotNumber + 1}"
                    ) as? String
                }
                if (imei == null) {
                    imei = runMethodReflect(
                        getSystemService("phone${slotNumber + 1}"),
                        null,
                        "getDeviceId",
                        null,
                        null
                    ) as? String
                }
                Timber.v("IMEI [$imei]")
                if (imei == null) {
                    if (slotNumber == 0) {
                        imei = if (isOreoPlus()) {
                            telephonyManager.imei
                        } else {
                            telephonyManager.deviceId
                        }
                        imsi = telephonyManager.subscriberId
                        simState = telephonyManager.simState
                        simOperator = telephonyManager.simOperator
                        simOperatorName = telephonyManager.simOperatorName
                        simSerialNumber = telephonyManager.simSerialNumber
                        simCountryIso = telephonyManager.simCountryIso
                        return this
                    }
                }
                if (imei == null) {
                    return null
                }
                setSimState(iterateMethods("getSimState", objectParamsSlot) as? Int)
                Timber.v("SIMSTATE [$simState]")
                imsi = iterateMethods("getSubscriberId", objectParamsSubs) as? String
                Timber.v("IMSI [$imsi]")
                simSerialNumber = iterateMethods("getSimSerialNumber", objectParamsSubs) as? String
                Timber.v("SIMSERIALNUMBER [$simSerialNumber]")
                simOperator = iterateMethods("getSimOperator", objectParamsSubs) as? String
                Timber.v("SIMOPERATOR [$simOperator]")
                simOperatorName = iterateMethods("getSimOperatorName", objectParamsSubs) as? String
                Timber.v("SIMOPERATORNAME [$simOperatorName]")
                simCountryIso = iterateMethods("getSimCountryIso", objectParamsSubs) as? String
                Timber.v("SIMCOUNTRYISO [$simCountryIso]")
            }
        }

    @SuppressLint("WrongConstant")
    private fun iterateMethods(methodName: String, methodParams: Array<Any>): Any? =
        reference.get()?.run {
            val telephonyManager = telephonyManager
            val instanceMethods = arrayListOf<Any?>()
            val multiSimTelephonyManagerExists = telephonyManager.toString()
                .startsWith("android.telephony.MultiSimTelephonyManager")
            for (methodParam in methodParams) {
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
            for (className in classNames) {
                for (methodSuffix in suffixes) {
                    for (instanceMethod in instanceMethods) {
                        for (methodParam in methodParams) {
                            val result = runMethodReflect(
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
        try {
            val classInvoke = when {
                classInvokeName != null -> Class.forName(classInvokeName)
                instanceInvoke != null -> instanceInvoke.javaClass
                else -> return null
            }
            return if (field != null) {
                val fieldReflect = classInvoke.getField(field)
                val accessible = fieldReflect.isAccessible
                fieldReflect.isAccessible = true
                val result = fieldReflect.get(null).toString()
                fieldReflect.isAccessible = accessible
                result
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
                val result = if (methodParams != null) {
                    method.invoke(instanceInvoke ?: classInvoke, *methodParams)
                } else {
                    method.invoke(instanceInvoke ?: classInvoke)
                }
                method.isAccessible = accessible
                result
            }
        } catch (ignored: Throwable) {
        }
        return null
    }

    @Suppress("unused")
    val allMethodsAndFields: String
        get() = """
            Default: ${reference.get()?.telephonyManager}
            ${printAllMethodsAndFields("android.telephony.TelephonyManager")}
            ${printAllMethodsAndFields("android.telephony.MultiSimTelephonyManager")}
            ${printAllMethodsAndFields("android.telephony.MSimTelephonyManager")}
            ${printAllMethodsAndFields("com.mediatek.telephony.TelephonyManager")}
            ${printAllMethodsAndFields("com.mediatek.telephony.TelephonyManagerEx")}
            ${printAllMethodsAndFields("com.android.internal.telephony.ITelephony")}
        """.trimIndent()

    private fun printAllMethodsAndFields(className: String): String {
        val builder = StringBuilder()
        builder.append("========== $className\n")
        try {
            val cls = Class.forName(className)
            for (method in cls.methods) {
                val params = method.parameterTypes.map { it.name }
                builder.append(
                    "M: ${method.name} [${params.size}](${TextUtils.join(
                        ",",
                        params
                    )}) -> ${method.returnType}${if (Modifier.isStatic(method.modifiers)) " (static)" else ""}\n"
                )
            }
            for (field in cls.fields) {
                builder.append("F: ${field.name} ${field.type}\n")
            }
        } catch (e: Throwable) {
            builder.append("E: $e\n")
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