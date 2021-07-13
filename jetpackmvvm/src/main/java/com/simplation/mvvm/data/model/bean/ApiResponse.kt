package com.simplation.mvvm.data.model.bean

import com.simplation.mvvmlib.network.BaseResponse

/**
 * Api response
 * 服务器返回数据的基类
 * 如果你的项目中有基类，那美滋滋，可以继承 BaseResponse，请求时框架可以帮你自动脱壳，自动判断是否请求成功，怎么做：
 * 1.继承 BaseResponse
 * 2.重写 isSucces 方法，编写你的业务需求，根据自己的条件判断数据是否请求成功
 * 3.重写 getResponseCode、getResponseData、getResponseMsg 方法，传入你的 code data msg
 *
 * @param T
 * @property errorCode
 * @property errorMessage
 * @property data
 * @constructor Create empty Api response
 */
data class ApiResponse<T>(val errorCode: Int, val errorMessage: String, val data: T) :
    BaseResponse<T>() {
    override fun isSuccess(): Boolean {
        return errorCode == 0
    }

    override fun getResponseData(): T {
        return data
    }

    override fun getResponseCode(): Int {
        return errorCode
    }

    override fun getResponseMsg(): String {
        return errorMessage
    }
}

