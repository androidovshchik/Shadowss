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

// on incomplete data
private
const val EWOULDBLOCK = 11

// on a breach of either colfer_size_max or colfer_list_max
private
const val EFBIG = 27

// on schema mismatch.
private
const val EILSEQ = 84

jboolean main_marsh(JNIEnv *env, jobject obj, jbyteArray bytes, bool serialize) {
    jint result = 0;
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
            env->SetIntField(obj, id1, 0);
        }
    }
    env->ReleaseByteArrayElements(bytes, (jbyte *) array, 0);
    return result;
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