package com.simplation.mvvmlib.ext.download

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @作者: Simplation
 * @日期: 2021/4/22 15:58
 * @描述:
 * @更新:
 */


interface DownLoadService {
    @Streaming
    @GET
    suspend fun downloadFile(
        @Header("RANGE") start: String,
        @Url url: String
    ): Response<ResponseBody>
}

