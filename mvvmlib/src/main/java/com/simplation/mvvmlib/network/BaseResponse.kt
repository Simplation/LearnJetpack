package com.simplation.mvvmlib.network

/**
 * @作者: Simplation
 * @日期: 2021/4/21 11:02
 * @描述: 后台返回数据的基类
 * 如果需要框架帮你做脱壳处理请继承它！！请注意：
 * 2.必须实现抽象方法，根据自己的业务判断返回请求结果是否成功
 * @更新:
 */

abstract class BaseResponse<T> {

    // 抽象方法，用户的基类继承该类时，需要重写该方法
    abstract fun isSucces(): Boolean

    abstract fun getResponseData(): T

    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String

}