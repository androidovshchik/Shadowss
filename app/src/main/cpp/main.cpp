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
T main_marsh(JNIEnv *env, V data, jstring className) {
    bool serialize = typeid(V) == typeid(jobject);
    const char *name = env->GetStringUTFChars(className, nullptr);
    jclass cls = env->FindClass(name);
    T result = nullptr;
    jbyte a[] = {1, 2, 3, 4, 5, 6};
    jbyteArray ret = env->NewByteArray(6);
    if (strcmp(name, "defpackage.ASAU") == 0) {
        jfieldID id1 = env->GetFieldID(cls, "token", "Ljava/lang/String");
        jfieldID id2 = env->GetFieldID(cls, "time", "J");
        jfieldID id3 = env->GetFieldID(cls, "timezone", "F");
        if (serialize) {
            main_ASAU main;
            main.token = env->GetObjectField(data, id1);
            main.time = env->GetLongField(data, id2);
            main.timezone = env->GetFloatField(data, id3);
            main_ASAU_marshal(&main, nullptr);
        } else {
            main_ASAU main;
            main_ASAU_unmarshal(&main, nullptr);
            env->SetByteArrayRegion(ret, 0, 6, a);
            char buffer[] = "This is a sample string";
            env->NewStringUTF(env, buffer);
            env->SetIntField(data, id1, 0);
            return result;
        }
    }
    env->ReleaseStringUTFChars(className, name);
    return result;
}

extern "C"

JNIEXPORT jbyteArray JNICALL
Java_defpackage_CBIN_main_marshal(JNIEnv *env, jobject, jobject obj, jstring className) {
    return main_marsh(env, obj, className);
}

extern "C"

JNIEXPORT jobject JNICALL
Java_defpackage_CBIN_main_unmarshal(JNIEnv *env, jobject, jbyteArray bytes, jstring className) {
    return main_marsh(env, bytes, className);
}