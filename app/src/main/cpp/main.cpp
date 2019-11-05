/**
 * Можно выбрать размер ключа между 128, 192 и 256 бит в файле <aes.h>, раскомментировав там соотв. строку
 * По умолчанию 128
 *     //#define AES192 1
 *     #define AES256 1
 */

#include <jni.h>
#include "Colfer.h"

extern "C" {

    JNIEXPORT jbyteArray JNICALL
    Java_defpackage_CAESCBC_ size_t main_ASPI_marshal_len(const main_ASPI* o);

size_t main_ASPI_marshal(const main_ASPI* o, void* buf);

size_t main_ASPI_unmarshal(main_ASPI* o, const void* data, size_t datalen);

size_t main_SAPO_marshal_len(const main_SAPO* o);

size_t main_SAPO_marshal(const main_SAPO* o, void* buf);

size_t main_SAPO_unmarshal(main_SAPO* o, const void* data, size_t datalen);

size_t main_ASPO_marshal_len(const main_ASPO* o);

size_t main_ASPO_marshal(const main_ASPO* o, void* buf);

size_t main_ASPO_unmarshal(main_ASPO* o, const void* data, size_t datalen);

size_t main_SAPI_marshal_len(const main_SAPI* o);

size_t main_SAPI_marshal(const main_SAPI* o, void* buf);

size_t main_SAPI_unmarshal(main_SAPI* o, const void* data, size_t datalen);

size_t main_ASRR_marshal_len(const main_ASRR* o);

size_t main_ASRR_marshal(const main_ASRR* o, void* buf);

size_t main_ASRR_unmarshal(main_ASRR* o, const void* data, size_t datalen);

size_t main_SARR_marshal_len(const main_SARR* o);

size_t main_SARR_marshal(const main_SARR* o, void* buf);

size_t main_SARR_unmarshal(main_SARR* o, const void* data, size_t datalen);

size_t main_RGI1_data_marshal_len(const main_RGI1_data* o);

size_t main_RGI1_data_marshal(const main_RGI1_data* o, void* buf);

size_t main_RGI1_data_unmarshal(main_RGI1_data* o, const void* data, size_t datalen);

size_t main_ASRV_marshal_len(const main_ASRV* o);

size_t main_ASRV_marshal(const main_ASRV* o, void* buf);

size_t main_ASRV_unmarshal(main_ASRV* o, const void* data, size_t datalen);

size_t main_SARV_marshal_len(const main_SARV* o);

size_t main_SARV_marshal(const main_SARV* o, void* buf);

size_t main_SARV_unmarshal(main_SARV* o, const void* data, size_t datalen);

size_t main_ASRM_marshal_len(const main_ASRM* o);

size_t main_ASRM_marshal(const main_ASRM* o, void* buf);

size_t main_ASRM_unmarshal(main_ASRM* o, const void* data, size_t datalen);

size_t main_SARM_marshal_len(const main_SARM* o);

size_t main_SARM_marshal(const main_SARM* o, void* buf);

size_t main_SARM_unmarshal(main_SARM* o, const void* data, size_t datalen);

size_t main_ASRC_marshal_len(const main_ASRC* o);

size_t main_ASRC_marshal(const main_ASRC* o, void* buf);

size_t main_ASRC_unmarshal(main_ASRC* o, const void* data, size_t datalen);

size_t main_ASAU_marshal_len(const main_ASAU* o);

size_t main_ASAU_marshal(const main_ASAU* o, void* buf);

size_t main_ASAU_unmarshal(main_ASAU* o, const void* data, size_t datalen);

size_t main_CSNG_marshal_len(const main_CSNG* o);

size_t main_CSNG_marshal(const main_CSNG* o, void* buf);

size_t main_CSNG_unmarshal(main_CSNG* o, const void* data, size_t datalen);

size_t main_SCNG_marshal_len(const main_SCNG* o);

size_t main_SCNG_marshal(const main_SCNG* o, void* buf);

size_t main_SCNG_unmarshal(main_SCNG* o, const void* data, size_t datalen);

size_t main_MSNG_marshal_len(const main_MSNG* o);

size_t main_MSNG_marshal(const main_MSNG* o, void* buf);

size_t main_MSNG_unmarshal(main_MSNG* o, const void* data, size_t datalen);

size_t main_SMNG_marshal_len(const main_SMNG* o);

size_t main_SMNG_marshal(const main_SMNG* o, void* buf);

size_t main_SMNG_unmarshal(main_SMNG* o, const void* data, size_t datalen);

size_t main_ASER_marshal_len(const main_ASER* o);

size_t main_ASER_marshal(const main_ASER* o, void* buf);

size_t main_ASER_unmarshal(main_ASER* o, const void* data, size_t datalen);
}


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
