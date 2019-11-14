package com.kirianov.multisim;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

@SuppressWarnings("all")
public class MultiSimTelephonyManager {

    private static String[] PERMISSIONS = {
        Manifest.permission.READ_PHONE_STATE
    };
    private final List<Slot> slots;
    private Context context;
    private BroadcastReceiverSimState broadcastReceiverSimState;
    private PhoneStateListenerSim phoneStateListenerSim;

    public MultiSimTelephonyManager(Context context) {
        try {
            System.loadLibrary("multisimlib");
        } catch (Exception e) {
            Timber.d("MSIM-LIB exception [" + e.getMessage() + "]");
        }

        MultiSimTelephonyManager.this.context = context.getApplicationContext();

        slots = new ArrayList<>();

        broadcastReceiverSimState = new BroadcastReceiverSimState();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        context.registerReceiver(broadcastReceiverSimState, intentFilter);

        phoneStateListenerSim = new PhoneStateListenerSim();
        ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
            .listen(phoneStateListenerSim, PhoneStateListener.LISTEN_SERVICE_STATE | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

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

    private native static String[] generateClassNames();

    private native static String[] generateMethodSuffix();

    private static Object runMethodReflect(Object instanceInvoke, String classInvokeName, String methodName, Object[] methodParams, String field) {
        Object result = null;
        Class<?> classInvoke = null;
        Class[] classesParams = null;
        // String logString = "";
        try {
            if (classInvokeName != null)
                classInvoke = Class.forName(classInvokeName);
            else if (instanceInvoke != null)
                classInvoke = Class.forName(instanceInvoke.getClass().getName());
            // logString += "" + (classInvoke != null ? classInvoke.getName() : null) + ".";
            if (classInvoke != null) {
                if (field != null) {
                    // logString += field;
                    Field fieldReflect = classInvoke.getField(field);
                    boolean accessible = fieldReflect.isAccessible();
                    fieldReflect.setAccessible(true);
                    result = fieldReflect.get(null).toString();
                    fieldReflect.setAccessible(accessible);
                } else {
                    // logString += methodName + "(";
                    if (methodParams != null) {
                        classesParams = new Class[methodParams.length];
                        for (int i = 0; i < methodParams.length; i++) {
                            if (methodParams[i] instanceof String) {
                                classesParams[i] = String.class;
                                // logString += "\"" + methodParams[i] + "\",";
                            } else if (methodParams[i] instanceof Integer) {
                                classesParams[i] = int.class;
                                // logString += methodParams[i] + ",";
                            } else if (methodParams[i] instanceof Long) {
                                classesParams[i] = long.class;
                                // logString += methodParams[i] + ",";
                            } else if (methodParams[i] instanceof Boolean) {
                                classesParams[i] = boolean.class;
                                // logString += methodParams[i] + ",";
                            } else {
                                classesParams[i] = methodParams[i].getClass();
                                // logString += "["+methodParams[i]+"]" + ",";
                            }
                        }
                    }
                    // if (logString.endsWith(","))
                    //     logString = logString.substring(0, logString.length() - 1);
                    // logString += ")";
                    try {
                        Method method = classInvoke.getDeclaredMethod(methodName, classesParams);
                        boolean accessible = method.isAccessible();
                        method.setAccessible(true);
                        result = method.invoke(instanceInvoke == null ? classInvoke : instanceInvoke, methodParams);
                        method.setAccessible(accessible);
                    } catch (Exception ignored) {
                    }
                }

            }

        } catch (Exception ignored) {
            // Log.i(LOG, "EXC " + ignored.getMessage());
        }
        // Log("" + logString + " = [" + result + "]");
        return result;
    }

    private boolean setSlot(int location, Slot slot) {
        boolean updated = false;
        try {
            if (slot == null) {
                if (slots.size() > location) {
                    slots.remove(location);
                    updated = true;
                }
            } else {
                if (slots.size() > location) {
                    if (!slot.compare(slots.get(location))) {
                        updated = true;
                    }
                    slots.set(location, slot);
                } else {
                    slots.add(location, slot);
                    updated = true;
                }
            }
        } catch (Exception ignored) {
        }
        return updated;
    }

    public Slot getSlot(int location) {
        if (slots.size() > location) return slots.get(location);
        return null;
    }

    public List<Slot> getSlots() {
        if ((slots == null) || (slots.size() <= 0)) return null;
        return slots;
    }

    @SuppressWarnings("ResourceType")
    private Slot touchSlot(int slotNumber) {
        Slot slot = new Slot();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Timber.d("telephonyManager [" + telephonyManager + "] " + telephonyManager.getDeviceId());

        ArrayList<String> subscriberIdIntValue = new ArrayList<String>();
        ArrayList<Integer> subscriberIdIntIndex = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            String subscriber;
            subscriber = (String) runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), "android.telephony.TelephonyManager", "getSubscriberId", new Object[]{i}, null);
            if ((subscriber != null) && (!subscriberIdIntValue.contains(subscriber))) {
                subscriberIdIntValue.add(subscriber);
                subscriberIdIntIndex.add(i);
            }
        }
        Integer subIdInt = subscriberIdIntIndex.size() > slotNumber ? subscriberIdIntIndex.get(slotNumber) : null;
        try {
            if (subIdInt == null)
                subIdInt = Integer.parseInt("" + runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), "android.telephony.TelephonyManager", "getSubId", new Object[]{slotNumber}, null));
        } catch (Exception ignored) {
        }
        Timber.d("subIdInt " + subIdInt);


        ArrayList<String> subscriberIdLongValue = new ArrayList<String>();
        ArrayList<Long> subscriberIdLongIndex = new ArrayList<Long>();
        for (Long i = 0L; i < 100L; i++) {
            String subscriber = (String) runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), "android.telephony.TelephonyManager", "getSubscriberId", new Object[]{i}, null);
            if (runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), "android.telephony.TelephonyManagerSprd", "getSubInfoForSubscriber", new Object[]{i}, null) == null)
                continue;
            if ((subscriber != null) && (!subscriberIdLongValue.contains(subscriber))) {
                subscriberIdLongValue.add(subscriber);
                subscriberIdLongIndex.add(i);
            }
        }
        if (subscriberIdLongIndex.size() <= 0)
            for (Long i = 0L; i < 100L; i++) {
                String subscriber = (String) runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), "android.telephony.TelephonyManager", "getSubscriberId", new Object[]{i}, null);
                if ((subscriber != null) && (!subscriberIdLongValue.contains(subscriber))) {
                    subscriberIdLongValue.add(subscriber);
                    subscriberIdLongIndex.add(i);
                }
            }
        Long subIdLong = subscriberIdLongIndex.size() > slotNumber ? subscriberIdLongIndex.get(slotNumber) : null;
        if (subIdLong == null)
            subIdLong = (Long) runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), "android.telephony.TelephonyManager", "getSubId", new Object[]{slotNumber}, null);
        Timber.d("subIdLong " + subIdLong);

        List<Object> listParamsSubs = new ArrayList<Object>();
        if ((subIdInt != null) && (!listParamsSubs.contains(subIdInt)))
            listParamsSubs.add(subIdInt);
        if ((subIdLong != null) && (!listParamsSubs.contains(subIdLong)))
            listParamsSubs.add(subIdLong);
        if (!listParamsSubs.contains(slotNumber))
            listParamsSubs.add(slotNumber);
        Object[] objectParamsSubs = listParamsSubs.toArray();
        for (int i = 0; i < objectParamsSubs.length; i++)
            Timber.d("SPAM PARAMS_SUBS [" + i + "]=[" + objectParamsSubs[i] + "]");

        List<Object> listParamsSlot = new ArrayList<Object>();
        if (!listParamsSlot.contains(slotNumber))
            listParamsSlot.add(slotNumber);
        if ((subIdInt != null) && (!listParamsSlot.contains(subIdInt)))
            listParamsSlot.add(subIdInt);
        if ((subIdLong != null) && (!listParamsSlot.contains(subIdLong)))
            listParamsSlot.add(subIdLong);
        Object[] objectParamsSlot = listParamsSlot.toArray();
        for (int i = 0; i < objectParamsSlot.length; i++)
            Timber.d("SPAM PARAMS_SLOT [" + i + "]=[" + objectParamsSlot[i] + "]");

        // for 6.0+ android sdk - uncomment after upgrade builder to SDK 23+ (now SDK 18 with apache.http*)
        //if(Build.VERSION.SDK_INT >= 23) {
        //    slot.setImei(telephonyManager.getDeviceId(slotNumber));
        //}

        // firstly all Int params, then all Long params
        Timber.d("------------------------------------------");
        // imei
        Timber.d("SLOT [" + slotNumber + "]");
        //if( slot.getImei() == null)
        slot.setImei((String) spamMethods("getDeviceId", objectParamsSlot));
        if (slot.getImei() == null)
            slot.setImei((String) runMethodReflect(null, "com.android.internal.telephony.Phone", null, null, "GEMINI_SIM_" + (slotNumber + 1)));
        if (slot.getImei() == null)
            slot.setImei((String) runMethodReflect(context.getSystemService("phone" + (slotNumber + 1)), null, "getDeviceId", null, null));
        // if( slot.getImei() == null)
        //     slot.setImei((String) runMethodReflect(null, "com.android.internal.telephony.PhoneFactory", "getServiceName", new Object[]{Context.TELEPHONY_SERVICE, slotNumber}, null));
        Timber.d("IMEI [" + slot.getImei() + "]");
        // default one sim phone
        if (slot.getImei() == null)
            switch (slotNumber) {
                case 0:
                    slot.setImei(telephonyManager.getDeviceId());
                    slot.setImsi(telephonyManager.getSubscriberId());
                    slot.setSimState(telephonyManager.getSimState());
                    slot.setSimOperator(telephonyManager.getSimOperator());
                    slot.setSimOperatorName(telephonyManager.getSimOperatorName());
                    slot.setSimSerialNumber(telephonyManager.getSimSerialNumber());
                    slot.setSimCountryIso(telephonyManager.getSimCountryIso());
                    slot.setNetworkOperator(telephonyManager.getNetworkOperator());
                    slot.setNetworkOperatorName(telephonyManager.getNetworkOperatorName());
                    slot.setNetworkCountryIso(telephonyManager.getNetworkCountryIso());
                    slot.setNetworkType(telephonyManager.getNetworkType());
                    slot.setNetworkRoaming(telephonyManager.isNetworkRoaming());
                    return slot;
            }
        if (slot.getImei() == null) return null;
        // simState
        slot.setSimState((Integer) spamMethods("getSimState", objectParamsSlot));
        Timber.d("SIMSTATE [" + slot.getSimState() + "]");
        // if( (slot.getSimState() == TelephonyManager.SIM_STATE_UNKNOWN) || (slot.getSimState() == -1))
        //    return slot;
        // imsi
        slot.setImsi((String) spamMethods("getSubscriberId", objectParamsSubs));
        Timber.d("IMSI [" + slot.getImsi() + "]");
        // simSerialNumber
        slot.setSimSerialNumber((String) spamMethods("getSimSerialNumber", objectParamsSubs));
        Timber.d("SIMSERIALNUMBER [" + slot.getSimSerialNumber() + "]");
        // simOperator
        slot.setSimOperator((String) spamMethods("getSimOperator", objectParamsSubs));
        Timber.d("SIMOPERATOR [" + slot.getSimOperator() + "]");
        // simOperatorName
        slot.setSimOperatorName((String) spamMethods("getSimOperatorName", objectParamsSubs));
        Timber.d("SIMOPERATORNAME [" + slot.getSimOperatorName() + "]");
        // simCountryIso
        slot.setSimCountryIso((String) spamMethods("getSimCountryIso", objectParamsSubs));
        Timber.d("SIMCOUNTRYISO [" + slot.getSimCountryIso() + "]");
        // networkOperator
        slot.setNetworkOperator((String) spamMethods("getNetworkOperator", objectParamsSubs));
        Timber.d("NETWORKOPERATOR [" + slot.getNetworkOperator() + "]");
        // networkOperatorName
        slot.setNetworkOperatorName((String) spamMethods("getNetworkOperatorName", objectParamsSubs));
        Timber.d("NETWORKOPERATORNAME [" + slot.getNetworkOperatorName() + "]");
        // networkCountryIso
        slot.setNetworkCountryIso((String) spamMethods("getNetworkCountryIso", objectParamsSubs));
        Timber.d("NETWORKCOUNTRYISO [" + slot.getNetworkCountryIso() + "]");
        // networkType
        slot.setNetworkType((Integer) spamMethods("getNetworkType", objectParamsSubs));
        Timber.d("NETWORKTYPE [" + slot.getNetworkType() + "]");
        // networkRoaming
        slot.setNetworkRoaming((Boolean) spamMethods("isNetworkRoaming", objectParamsSubs));
        Timber.d("NETWORKROAMING [" + slot.isNetworkRoaming() + "]");
        Timber.d("------------------------------------------");
        return slot;
    }

    public void updateSlots() {
        int slotNumber = 0;
        Slot slot;
        boolean changed = false;
        while (true) {
            slot = touchSlot(slotNumber);
            if ((slot == null) && (slots != null)) {
                for (int i = slotNumber; i < slots.size(); i++) {
                    changed = true;
                    slots.remove(i);
                }
                break;
            }
            // Log( "slot.containsIn(slots) " + slot.getImei() + " " + slot.containsIn( slots));
            if ((slot != null) && slot.containsIn(slots) && (slot.indexIn(slots) < slotNumber)) // protect from Alcatel infinity bug
                break;
            changed = setSlot(slotNumber, slot) | changed;
            slotNumber++;
        }
    }

    @SuppressWarnings("ResourceType")
    private Object spamMethods(String methodName, Object[] methodParams) {
        if ((methodName == null) || (methodName.length() <= 0)) return null;
        List<Object> instanceMethods = new ArrayList<Object>();
        boolean multiSimTelephonyManagerExists = false;
        try {
            multiSimTelephonyManagerExists = context.getSystemService(Context.TELEPHONY_SERVICE).toString().startsWith("android.telephony.MultiSimTelephonyManager");
            for (int i = 0; i < methodParams.length; i++) {
                if (methodParams[i] == null) continue;
                Object objectMulti = multiSimTelephonyManagerExists ?
                        (methodParams.length >= 1 ?
                                runMethodReflect(null, "android.telephony.MultiSimTelephonyManager", "getDefault", new Object[]{methodParams[i]}, null) :
                                runMethodReflect(null, "android.telephony.MultiSimTelephonyManager", "getDefault", null, null))
                        : context.getSystemService(Context.TELEPHONY_SERVICE);
                if (!instanceMethods.contains(objectMulti))
                    instanceMethods.add(objectMulti);
            }
        } catch (Exception ignored) {
        }
        if (!instanceMethods.contains(context.getSystemService(Context.TELEPHONY_SERVICE)))
            instanceMethods.add(context.getSystemService(Context.TELEPHONY_SERVICE));
        if (!instanceMethods.contains(runMethodReflect(null, "com.mediatek.telephony.TelephonyManagerEx", "getDefault", null, null)))
            instanceMethods.add(runMethodReflect(null, "com.mediatek.telephony.TelephonyManagerEx", "getDefault", null, null));
        if (!instanceMethods.contains(context.getSystemService("phone_msim")))
            instanceMethods.add(context.getSystemService("phone_msim"));
        if (!instanceMethods.contains(null))
            instanceMethods.add(null);
//        String[] classNames = {
//                null,
//                "android.telephony.TelephonyManager",
//                "android.telephony.MSimTelephonyManager",
//                "android.telephony.MultiSimTelephonyManager",
//                "com.mediatek.telephony.TelephonyManagerEx",
//                "com.android.internal.telephony.Phone",
//                "com.android.internal.telephony.PhoneFactory"
//        };
        String[] classNames = generateClassNames();


//        String[] methodSuffixes = {
//                "",
//                "Gemini",
//                "Ext",
//                "Ds",
//                "ForSubscription",
//                "ForPhone"
//        };
        String[] methodSuffixes = generateMethodSuffix();
        Object result;
        for (String methodSuffix : methodSuffixes)
            for (String className : classNames)
                for (Object instanceMethod : instanceMethods)
                    for (int i = 0; i < methodParams.length; i++) {
                        if (methodParams[i] == null) continue;
                        result = runMethodReflect(instanceMethod, className, methodName + methodSuffix, multiSimTelephonyManagerExists ? null : new Object[]{methodParams[i]}, null);
                        if (result != null)
                            return result;
                    }
        return null;
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

/*
    @SuppressWarnings("ResourceType")
    private void printAllMethodsAndFields(String className, int paramsCount) {

        Log("default telephony [" + context.getSystemService(Context.TELEPHONY_SERVICE) + "]");


        Log.i(LOG, "=========================");
        if( className != null)
            try {
                Class<?> MultiSimClass = Class.forName(className);

                Log.i(LOG, "Methods of [" + className + "] (" + paramsCount + "):");
                for (Method method : MultiSimClass.getMethods()) {
                    if ((method.getParameterTypes().length == paramsCount) || (paramsCount == -1)) {
                        String params = "";
                        for (int i = 0; i < method.getParameterTypes().length; i++)
                            params += method.getParameterTypes()[i].getName() + ", ";
                        Log.i(LOG, " m " + method.getName() + "[" + method.getParameterTypes().length + "](" + (params.length() > 0 ? params.substring(0, params.length() - 2) : params) + ") -> " + method.getReturnType() + "" + (Modifier.isStatic(method.getModifiers()) ? " (static)" : ""));
                    }
                }

                Log.i(LOG, "Fields of [" + className + "]:");
                for( Field field : MultiSimClass.getFields()) {
                    Log.i(LOG, " f " + field.getName() + " " + field.getType());
                }

            } catch( Exception e) {
                Log.i( LOG, "EXC " + e.getMessage());
            }
        Log.i( LOG, "=========================");


        try {
            Class<?> MultiSimClass = Class.forName(className);


//            // get fields values
//            Object inst = null;
//            for(int i = 0; i < 10; i++) {
//                inst = runMethodReflect(null, className, "getDefault", new Object[]{i}, null);
//                Log.i(LOG, "Fields2 of [" + className + "]:");
//                for( Field field : MultiSimClass.getFields()) {
//                    field.setAccessible(true);
//                    Log.i(LOG, " f2 " + field.getName() + " " + field.getType() + " " + field.get(inst));
//                }
//            }



//            for(int i = 0; i < 10; i++) {
//                Object inst = runMethodReflect(null, "android.telephony.MultiSimTelephonyManager", "getDefault", new Object[]{i}, null);
//                Log.i(LOG, "METHOD3s [" + "inst0" + "] = [" + inst + "]");
//                inst = runMethodReflect(inst, "android.telephony.MultiSimTelephonyManager", "getITelephony", null, null);
//                Log.i(LOG, "METHOD3s [" + "inst1" + "] = [" + inst + "]");
//                if (runMethodReflect(inst, "com.android.internal.telephony.ITelephony$Stub$Proxy", "getImei", null, null) != null)
//                    Log.i(LOG, "METHOD3s [" + "getImei" + "] = [" + runMethodReflect(inst, "com.android.internal.telephony.ITelephony$Stub$Proxy", "getImei", null, null) + "]");
//            }


            Object inst = runMethodReflect(null, "android.telephony.TelephonyManager", "getDefault", null, null);
            Log.i(LOG, "METHOD5s [" + "==inst" + "] = [" + inst + "]");


            for (Method method : MultiSimClass.getMethods()) {

//                // 0 param
//                if( method.getParameterTypes().length == 0) {
//                    Log.i(LOG, "m " + method.getName() + "(" + method.getParameterTypes().length + ") " + method.getReturnType());
//                    //if (runMethodReflect(null, className, method.getName(), null, null) != null)
//                      //  Log.i(LOG, "METHODs [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(null, className, method.getName(), null, null) + "]");
//                    //if (runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), className, method.getName(), null, null) != null)
//                      //  Log.i(LOG, "METHODi [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), className, method.getName(), null, null) + "]");
//                    if (runMethodReflect(inst1, null, method.getName(), null, null) != null)
//                        Log.i(LOG, "METHODx [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(inst1, null, method.getName(), null, null) + "]");
//                    if (runMethodReflect(inst2, null, method.getName(), null, null) != null)
//                        Log.i(LOG, "METHODx [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(inst2, null, method.getName(), null, null) + "]");
//                }



                // 1 param
                if( method.getParameterTypes().length == 1) {

                    Log.i( LOG, "m " + method.getName() + "(" + method.getParameterTypes().length + ") " + method.getReturnType());
//                    // if (!method.getName().startsWith("get")) continue;
//
//                    //if( runMethodReflect(null, className, method.getName(), new Object[]{0}, null) != null)
//                    //    Log.i(LOG, "METHODs [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(null, className, method.getName(), new Object[]{0}, null) + "]");
//                    //if( runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), className, method.getName(), new Object[]{0}, null) != null)
//                    //    Log.i(LOG, "METHODi [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), className, method.getName(), new Object[]{0}, null) + "]");
//                    //if( runMethodReflect(null, className, method.getName(), new Object[]{1}, null) != null)
//                    //    Log.i(LOG, "METHODs [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(null, className, method.getName(), new Object[]{1}, null) + "]");
//                    //if( runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), className, method.getName(), new Object[]{1}, null) != null)
//                    //    Log.i(LOG, "METHODi [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), className, method.getName(), new Object[]{1}, null) + "]");
//
//                    //if( runMethodReflect(null, className, method.getName(), new Object[]{0L}, null) != null)
//                    //    Log.i(LOG, "METHODs [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(null, className, method.getName(), new Object[]{0L}, null) + "]");
//                    //if( runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), className, method.getName(), new Object[]{0L}, null) != null)
//                    //    Log.i(LOG, "METHODi [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), className, method.getName(), new Object[]{0L}, null) + "]");
//                    //if( runMethodReflect(null, className, method.getName(), new Object[]{1L}, null) != null)
//                    //    Log.i(LOG, "METHODs [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(null, className, method.getName(), new Object[]{1L}, null) + "]");
//                    //if( runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), className, method.getName(), new Object[]{1L}, null) != null)
//                    //    Log.i(LOG, "METHODi [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), className, method.getName(), new Object[]{1L}, null) + "]");
//
//                    //Log.i(LOG, "METHODs [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(null, "android.telephony.TelephonyManager", method.getName(), new Object[]{0}, null) + "]");
//                    //Log.i(LOG, "METHODi [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), "android.telephony.TelephonyManager", method.getName(), new Object[]{0}, null) + "]");
//                    //Log.i(LOG, "METHODs [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(null, "android.telephony.TelephonyManager", method.getName(), new Object[]{1}, null) + "]");
//                    //Log.i(LOG, "METHODi [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), "android.telephony.TelephonyManager", method.getName(), new Object[]{1}, null) + "]");
//
//
//                    //for(int i = 0; i < 10; i++) {
//                      //  if (runMethodReflect(null, className, method.getName(), new Object[]{i}, null) != null)
//                        //    Log.i(LOG, "METHODs [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(null, className, method.getName(), new Object[]{i}, null) + "]");
//                    //}
//
//                    //for(int i = 0; i < 10; i++) {
//                      //  if (runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), className, method.getName(), new Object[]{i}, null) != null)
//                        //    Log.i(LOG, "METHODi [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(context.getSystemService(Context.TELEPHONY_SERVICE), className, method.getName(), new Object[]{i}, null) + "]");
//                    //}
//
                }

            }

        } catch(Exception ignored) {
        }

//// Samsung Galaxy S III (android 4.4.4 + Samsung SDK)
//        try {
//            Object object = runMethodReflect(null, "android.telephony.MultiSimTelephonyManager", "getDefault", new Object[]{0}, null);
//            for (Method method : object.getClass().getMethods()) {
//                if (method.getParameterTypes().length == 0) {
//                    Log.i(LOG, "METHOD-ww0 [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(object, "android.telephony.TelephonyManager", method.getName(), null, null) + "]");
//                }
//            }
//            object = runMethodReflect(null, "android.telephony.MultiSimTelephonyManager", "getDefault", new Object[]{1}, null);
//            for (Method method : object.getClass().getMethods()) {
//                if (method.getParameterTypes().length == 0) {
//                    Log.i(LOG, "METHOD-ww1 [" + method.getName() + "] [" + method.getReturnType() + "] = [" + runMethodReflect(object, "android.telephony.TelephonyManager", method.getName(), null, null) + "]");
//                }
//            }
//        } catch (Exception ignored) {
//        }

//        Log.i( LOG, "=========================");
//        Log.i( LOG, "=========================");

    }
*/

    public void destroy() {
        ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
            .listen(null, PhoneStateListener.LISTEN_NONE);
        phoneStateListenerSim = null;
        try {
            if (broadcastReceiverSimState != null)
                context.unregisterReceiver(broadcastReceiverSimState);
        } catch (Exception ignored) {
        }
        context = null;
    }

    private class BroadcastReceiverSimState extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            Timber.d("BRSS context [" + context + "] intent [" + intent + "]");
            if ((intent != null) && (intent.getAction() != null)) {
                if (MultiSimTelephonyManager.this.context == null)
                    MultiSimTelephonyManager.this.context = context.getApplicationContext();
                if (hasPermissions(context, PERMISSIONS)) {
                    updateSlots();
                }
//// try patch below when onSignalStrengthsChanged is not work
//                // patch for "late" update of network operator info, because after "android.intent.action.SIM_STATE_CHANGED" I read FIRST time before OS write new network operator info :
//                // this will work on switch off/on airplane mode (long time operation with both sims one-by-one) also
//                if( intent.getAction().compareTo(context.getPackageName() + "BRSS") != 0) {
//                    new CountDownTimer(30000, 1000) {
//                        public void onTick(long m) {
//                            updateSlots(context, intent);
//                        }
//                        public void onFinish() {
//                        }
//                    }.start();
//                }
            }
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private class PhoneStateListenerSim extends PhoneStateListener {

        public void onServiceStateChanged(ServiceState serviceState) {
            Timber.d("PhoneStateListenerSim.onServiceStateChanged " + serviceState.getState());
            updateSlots();
        }
//        public void onSignalStrengthsChanged(SignalStrength signalStrength) { // removed temporary because more times start (every sec)
//            Log("PhoneStateListenerSim.onSignalStrengthsChanged " + signalStrength);
//            if( context != null) context.sendBroadcast(new Intent(context.getPackageName() + "BRSS"));
//        }
// onCellInfoChanged and onCellLocationChanged REQUIRED  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" /> in AndroidManifest
//        public void onCellInfoChanged(List<CellInfo> cellInfo) {
//            Log("PhoneStateListenerSim.onCellInfoChanged " + cellInfo);
//            if( context != null) context.sendBroadcast(new Intent(context.getPackageName() + "BRSS"));
//        }
//        public void onCellLocationChanged(CellLocation location) {
//            Log("PhoneStateListenerSim.onCellLocationChanged " + location);
//            if( context != null) context.sendBroadcast(new Intent(context.getPackageName() + "BRSS"));
//        }
    }
}