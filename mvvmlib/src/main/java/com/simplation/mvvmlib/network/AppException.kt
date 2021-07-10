package com.simplation.mvvmlib.network

import java.lang.Exception

/**
 * @作者: Simplation
 * @日期: 2021/4/21 10:48
 * @描述: 自定义异常信息
 * @更新:
 */

class AppException : Exception {

    var errMsg: String // 错误消息
    var errCode: Int = 0 // 错误码
    var errorLog: String? // 错误日志

    constructor(errCode: Int, error: String?, errorLog: String? = "") : super(error) {
        this.errMsg = error ?: "请求失败，请稍后再试"
        this.errCode = errCode
        this.errorLog = errorLog ?: this.errMsg
    }

    constructor(error: Error, e: Throwable?) {
        errCode = error.getKey()
        errMsg = error.getValue()
        errorLog = e?.message
    }

}
