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
void process(JNIEnv *env, jobject, jobject obj) {
    jclass cls = env->GetObjectClass(env, objarg);
    switch ("") {
        case "defpackage.ASAU":
            "Ljava/lang/String"
            "J"
            "F"
            break;
        case 2:
            break;
        default:
            break;
    }
    /* Get int field
       Take a look here, we are passing char* with
       field descriptor - e.g. "I" => int
     */
    jfieldID fidInt = env->GetFieldID(cls, "iVal", "I");
    jint iVal = env->GetIntField(env, objarg, fidInt);
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_defpackage_CBIN_main_marshal(JNIEnv *env, jobject, jobject obj, jstring className) {

}

extern "C"
JNIEXPORT jobject JNICALL
Java_defpackage_CBIN_main_unmarshal(JNIEnv *env, jobject, jbyteArray bytes, jstring className) {

}