#include <jni.h>
#include "Colfer.h"

template<typename T>
T min(T a, T b) {
    a.
            jclass
    javaDataClass = env->FindClass("JavaData");

// get the field id
    jfieldID countField = env->GetFieldID(javaDataClass, "count", "I");

// get the data from the field
    cData.count = env->GetIntField(jData, countField);
    return a < b ? a : b;
}

extern "C" {

JNIEXPORT jint JNICALL
Java_defpackage_CBIN_decrypt(JNIEnv *env, jobject, jobject data) {
    jclass javaDataClass = env->FindClass("JavaData");

// get the field id
    jfieldID countField = env->GetFieldID(javaDataClass, "count", "I");

// get the data from the field
    cData.count = env->GetIntField(jData, countField);
    return main_ASPI_marshal(NULL, NULL);
}

JNIEXPORT jint JNICALL
Java_defpackage_CBIN_decrypt(JNIEnv *env, jobject, jobject data) {
    jclass javaDataClass = env->FindClass("JavaData");

// get the field id
    jfieldID countField = env->GetFieldID(javaDataClass, "count", "I");

// get the data from the field
    cData.count = env->GetIntField(jData, countField);
    return main_ASPI_marshal(NULL, NULL);
}
}