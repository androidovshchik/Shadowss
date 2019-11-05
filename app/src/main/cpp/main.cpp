/**
 * Можно выбрать размер ключа между 128, 192 и 256 бит в файле <aes.h>, раскомментировав там соотв. строку
 * По умолчанию 128
 *     //#define AES192 1
 *     #define AES256 1
 */

#include <jni.h>
#include "Colfer.h"

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_defpackage_CAESCBC_encrypt(JNIEnv *env, jobject, jbyteArray data, jbyteArray key, jbyteArray iv) {

    return data;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_defpackage_CAESCBC_decrypt(JNIEnv *env, jobject, jbyteArray data, jbyteArray key, jbyteArray iv) {

    return data;
}
