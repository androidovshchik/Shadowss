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
T process(JNIEnv *env, V data, jclass cls) {
    switch (className) {
        case "defpackage.ASAU":
            "Ljava/lang/String"
            "J"
            "F"
            jfieldID fidInt = env->GetFieldID(cls, "iVal", "I");
            jint iVal = env->GetIntField(env, objarg, fidInt);
            env->SetIntField(env, objarg, fidInt);
            break;
        default:
            break;
    }
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_defpackage_CBIN_main_marshal(JNIEnv *env, jobject, jobject obj) {
    return process(env, obj, env->GetObjectClass(obj));
}

extern "C"
JNIEXPORT jobject JNICALL
Java_defpackage_CBIN_main_unmarshal(JNIEnv *env, jobject, jbyteArray bytes, jstring className) {
    return process(env, bytes, cls);
}