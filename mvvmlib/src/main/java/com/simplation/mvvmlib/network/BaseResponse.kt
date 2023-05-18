package com.simplation.mvvmlib.network

/**
 * Base response
 *      后台返回数据的基类
 *          如果需要框架帮你做脱壳处理请继承它！！请注意：
 *              必须实现抽象方法，根据自己的业务判断返回请求结果是否成功
 *
 * @param T
 * @constructor Create empty Base response
 */
abstract class BaseResponse<T> {

    // 抽象方法，用户的基类继承该类时，需要重写该方法
    abstract fun isSuccess(): Boolean

    abstract fun getResponseData(): T

    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String

}