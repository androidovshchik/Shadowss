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
jint main_marsh(JNIEnv *env, jobject obj, jbyteArray bytes, bool serialize) {
    jint result = 0;
    auto *array = (uint8_t *) env->GetByteArrayElements(bytes, nullptr);
    const char *name = env->GetStringUTFChars(className, nullptr);
    jclass cls = env->FindClass(name);
    if (strcmp(name, "defpackage.ASAU") == 0) {
        jfieldID id1 = env->GetFieldID(cls, "token", "Ljava/lang/String");
        jfieldID id2 = env->GetFieldID(cls, "time", "J");
        jfieldID id3 = env->GetFieldID(cls, "timezone", "F");
        if (serialize) {
            main_ASAU main;
            main.token = env->GetObjectField(obj, id1);
            main.time = env->GetLongField(obj, id2);
            main.timezone = env->GetFloatField(obj, id3);
            main_ASAU_marshal(&main, array);
        } else {
            main_ASAU main;
            main_ASAU_unmarshal(&main, array, 0);
            char buffer[] = "This is a sample string";
            env->NewStringUTF(env, buffer);
            env->SetIntField(data, id1, 0);
            return result;
        }
    }
    env->ReleaseByteArrayElements(bytes, (jbyte *) array, 0);
    return result;
}

extern "C"

JNIEXPORT jint JNICALL
Java_defpackage_CBIN_main_marshal(JNIEnv *env, jobject, jobject obj, jbyteArray bytes) {
    return main_marsh(env, obj, bytes, true);
}

extern "C"

JNIEXPORT jint JNICALL
Java_defpackage_CBIN_main_unmarshal(JNIEnv *env, jobject, jobject obj, jbyteArray bytes) {
    return main_marsh(env, obj, bytes, false);
}