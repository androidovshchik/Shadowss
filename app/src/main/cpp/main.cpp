#include <jni.h>
#include <cerrno>
#include "Colfer.h"

colfer_text main_text(JNIEnv *env, jobject obj, jfieldID id1) {
    colfer_text text;
    auto string = (jstring) env->GetObjectField(obj, id1);
    const char *chars = env->GetStringUTFChars(string, nullptr);
    text.utf8 = chars;
    text.len = strlen(chars);
    env->ReleaseStringUTFChars(string, chars);
    env->DeleteLocalRef(string);
    return text;
}

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
jboolean main_marsh(JNIEnv *env, jobject obj, jstring className, jbyteArray bytes, bool serialize) {
    const char *name = env->GetStringUTFChars(className, nullptr);
    const auto size = (size_t) env->GetArrayLength(bytes);
    auto *array = env->GetByteArrayElements(bytes, nullptr);
    jclass cls = env->GetObjectClass(obj);
    if (strcmp(name, "defpackage.ASAU") == 0) {
        main_ASAU main;
        jfieldID id1 = env->GetFieldID(cls, "token", "Ljava/lang/String");
        jfieldID id2 = env->GetFieldID(cls, "time", "J");
        jfieldID id3 = env->GetFieldID(cls, "timezone", "F");
        if (serialize) {
            main.token = main_text(env, obj, id1);
            main.time = (uint64_t) env->GetLongField(obj, id2);
            main.timezone = env->GetFloatField(obj, id3);
            main_ASAU_marshal(&main, array);
        } else {
            main_ASAU_unmarshal(&main, array, size);
            env->SetObjectField(obj, id1, env->NewStringUTF(main.token.utf8));
            env->SetLongField(obj, id2, main.time);
            env->SetFloatField(obj, id3, main.timezone);
        }
    }
    env->ReleaseByteArrayElements(bytes, array, 0);
    env->ReleaseStringUTFChars(className, name);
    env->DeleteLocalRef(cls);
    if (errno == EWOULDBLOCK || errno == EFBIG || errno == EILSEQ) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

extern "C"

JNIEXPORT jboolean JNICALL
Java_defpackage_CBIN_main_marshal(JNIEnv *env, jobject, jobject obj, jstring className,
                                  jbyteArray bytes) {
    return main_marsh(env, obj, className, bytes, true);
}

extern "C"

JNIEXPORT jboolean JNICALL
Java_defpackage_CBIN_main_unmarshal(JNIEnv *env, jobject, jobject obj, jstring className,
                                    jbyteArray bytes) {
    return main_marsh(env, obj, className, bytes, false);
}