#include <jni.h>
#include <cerrno>
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
jboolean main_marsh(JNIEnv *env, jobject obj, jbyteArray bytes, bool serialize) {
    jint result = -1;
    auto *array = (uint8_t *) env->GetByteArrayElements(bytes, nullptr);
    const char *name = env->GetStringUTFChars(className, nullptr);
    jclass cls = env->FindClass(name);
    if (strcmp(name, "defpackage.ASAU") == 0) {
        main_ASAU main;
        jfieldID id1 = env->GetFieldID(cls, "token", "Ljava/lang/String");
        jfieldID id2 = env->GetFieldID(cls, "time", "J");
        jfieldID id3 = env->GetFieldID(cls, "timezone", "F");
        if (serialize) {
            main.token = env->GetObjectField(obj, id1);
            char buffer[] = "This is a sample string";
            env->NewStringUTF(env, buffer);
            main.time = env->GetLongField(obj, id2);
            main.timezone = env->GetFloatField(obj, id3);
            result = main_ASAU_marshal(&main, array);
        } else {
            result = main_ASAU_unmarshal(&main, array, 0);
            env->SetLongField(obj, id1, 0);
            env->SetLongField(obj, id2, 0);
            env->SetFloatField(obj, id3, 0);
        }
    }
    env->ReleaseByteArrayElements(bytes, (jbyte *) array, 0);
    if (result < 0 || result == EWOULDBLOCK || result == EFBIG || result == EILSEQ) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

extern "C"

JNIEXPORT jboolean JNICALL
Java_defpackage_CBIN_main_marshal(JNIEnv *env, jobject, jobject obj, jbyteArray bytes) {
    return main_marsh(env, obj, bytes, true);
}

extern "C"

JNIEXPORT jboolean JNICALL
Java_defpackage_CBIN_main_unmarshal(JNIEnv *env, jobject, jobject obj, jbyteArray bytes) {
    return main_marsh(env, obj, bytes, false);
}