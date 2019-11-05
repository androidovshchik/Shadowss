package defpackage.shadowss

import defpackage.shadowss.jbit.ASPI
import defpackage.shadowss.jbit.SAPO

/**
 * Важны имена пакета и класса
 */
@Suppress("FunctionName")
object CBIN {

    init {
        System.loadLibrary("bin")
    }

    private external fun encrypt(data: ByteArray, key: ByteArray, iv: ByteArray): ByteArray

    external fun main_ASPI_marshal_len(o: ASPI): Int

    external fun main_ASPI_marshal(o: ASPI, void* buf): Int

    external fun main_ASPI_unmarshal(main_ASPI* o, const void* data ,  datalen): Int

    external fun main_SAPO_marshal_len(o: SAPO): Int

    external fun main_SAPO_marshal(SAPO* o, void* buf): Int

    external fun main_SAPO_unmarshal(main_SAPO* o, const void* data ,  datalen): Int

    external fun main_ASPO_marshal_len(ASPO* o): Int

    external fun main_ASPO_marshal(ASPO* o, void* buf): Int

    external fun main_ASPO_unmarshal(main_ASPO* o, const void* data ,  datalen): Int

    external fun main_SAPI_marshal_len(SAPI* o): Int

    external fun main_SAPI_marshal(SAPI* o, void* buf): Int

    external fun main_SAPI_unmarshal(main_SAPI* o, const void* data ,  datalen): Int

    external fun main_ASRR_marshal_len(ASRR* o): Int

    external fun main_ASRR_marshal(ASRR* o, void* buf): Int

    external fun main_ASRR_unmarshal(main_ASRR* o, const void* data ,  datalen): Int

    external fun main_SARR_marshal_len(SARR* o): Int

    external fun main_SARR_marshal(SARR* o, void* buf): Int

    external fun main_SARR_unmarshal(main_SARR* o, const void* data ,  datalen): Int

    external fun main_RGI1_data_marshal_len(RGI1_data* o): Int

    external fun main_RGI1_data_marshal(RGI1_data* o, void* buf): Int

    external fun main_RGI1_data_unmarshal(main_RGI1_data* o, const void* data ,  datalen): Int

    external fun main_ASRV_marshal_len(ASRV* o): Int

    external fun main_ASRV_marshal(ASRV* o, void* buf): Int

    external fun main_ASRV_unmarshal(main_ASRV* o, const void* data ,  datalen): Int

    external fun main_SARV_marshal_len(SARV* o): Int

    external fun main_SARV_marshal(SARV* o, void* buf): Int

    external fun main_SARV_unmarshal(main_SARV* o, const void* data ,  datalen): Int

    external fun main_ASRM_marshal_len(ASRM* o): Int

    external fun main_ASRM_marshal(ASRM* o, void* buf): Int

    external fun main_ASRM_unmarshal(main_ASRM* o, const void* data ,  datalen): Int

    external fun main_SARM_marshal_len(SARM* o): Int

    external fun main_SARM_marshal(SARM* o, void* buf): Int

    external fun main_SARM_unmarshal(main_SARM* o, const void* data ,  datalen): Int

    external fun main_ASRC_marshal_len(ASRC* o): Int

    external fun main_ASRC_marshal(ASRC* o, void* buf): Int

    external fun main_ASRC_unmarshal(main_ASRC* o, const void* data ,  datalen): Int

    external fun main_ASAU_marshal_len(ASAU* o): Int

    external fun main_ASAU_marshal(ASAU* o, void* buf): Int

    external fun main_ASAU_unmarshal(main_ASAU* o, const void* data ,  datalen): Int

    external fun main_CSNG_marshal_len(CSNG* o): Int

    external fun main_CSNG_marshal(CSNG* o, void* buf): Int

    external fun main_CSNG_unmarshal(main_CSNG* o, const void* data ,  datalen): Int

    external fun main_SCNG_marshal_len(SCNG* o): Int

    external fun main_SCNG_marshal(SCNG* o, void* buf): Int

    external fun main_SCNG_unmarshal(main_SCNG* o, const void* data ,  datalen): Int

    external fun main_MSNG_marshal_len(MSNG* o): Int

    external fun main_MSNG_marshal(MSNG* o, void* buf): Int

    external fun main_MSNG_unmarshal(main_MSNG* o, const void* data ,  datalen): Int

    external fun main_SMNG_marshal_len(SMNG* o): Int

    external fun main_SMNG_marshal(SMNG* o, void* buf): Int

    external fun main_SMNG_unmarshal(main_SMNG* o, const void* data ,  datalen): Int

    external fun main_ASER_marshal_len(ASER* o): Int

    external fun main_ASER_marshal(ASER* o, void* buf): Int

    external fun main_ASER_unmarshal(main_ASER* o, const void* data ,  datalen): Int
}