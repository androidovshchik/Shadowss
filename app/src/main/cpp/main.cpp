#include <jni.h>
#include "Colfer.h"

/*
   For accessing primitive types from class use following field descriptors
   +---+---------+
   | Z | boolean |
   | B | byte    |
   | C | char    |
   | S | short   |
   | I | int     |
   | J | long    |
   | F | float   |
   | D | double  |
   +-------------+
*/
template<typename T, typename V>
T process(JNIEnv *env, V data, jstring className) {
    const char *name = env->GetStringUTFChars(className, nullptr);
    jclass cls = env->FindClass(name);
    switch (name) {
        case "defpackage.ASAU":
            jfieldID fidInt = env->GetFieldID(cls, "token", "Ljava/lang/String");
            jfieldID fidInt = env->GetFieldID(cls, "time", "J");
            jfieldID fidInt = env->GetFieldID(cls, "timezone", "F");
            jint iVal = env->GetIntField(env, objarg, fidInt);
            env->SetIntField(env, objarg, fidInt);
            break;
        default:
            break;
    }
    env->ReleaseStringUTFChars(className, name);
}

extern "C"

JNIEXPORT jbyteArray JNICALL
Java_defpackage_CBIN_main_marshal(JNIEnv *env, jobject, jobject obj, jstring className) {
    return process(env, obj, className);
}

extern "C"

JNIEXPORT jobject JNICALL
Java_defpackage_CBIN_main_unmarshal(JNIEnv *env, jobject, jbyteArray bytes, jstring className) {
    return process(env, bytes, className);
}