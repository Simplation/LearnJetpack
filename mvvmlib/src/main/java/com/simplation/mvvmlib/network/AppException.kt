package com.simplation.mvvmlib.network

import java.lang.Exception

/**
 * App exception
 *      自定义异常信息
 *
 * @constructor Create empty App exception
 */
class AppException : Exception {

    var errMsg: String // 错误消息
    var errCode: Int = 0 // 错误码
    var errorLog: String? // 错误日志
    var throwable: Throwable? = null

    constructor(
        errCode: Int,
        error: String?,
        errorLog: String? = "",
        throwable: Throwable? = null
    ) : super(error) {
        this.errMsg = error ?: "请求失败，请稍后再试"
        this.errCode = errCode
        this.errorLog = errorLog ?: this.errMsg
        this.throwable = throwable
    }

    constructor(error: Error, e: Throwable?) {
        errCode = error.getKey()
        errMsg = error.getValue()
        errorLog = e?.message
        throwable = e
    }

}
