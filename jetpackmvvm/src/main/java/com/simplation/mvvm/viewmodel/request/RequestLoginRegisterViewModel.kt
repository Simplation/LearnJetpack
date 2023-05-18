package com.simplation.mvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.simplation.mvvm.app.network.apiService
import com.simplation.mvvm.data.model.bean.UserInfo
import com.simplation.mvvm.data.repository.request.HttpRequestCoroutine
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.ext.request
import com.simplation.mvvmlib.state.ResultState

/**
 * Request login register view model
 *
 * @constructor Create empty Request login register view model
 */
class RequestLoginRegisterViewModel : BaseViewModel() {
    // 方式 1  自动脱壳过滤处理请求结果，判断结果是否成功
    var loginResult = MutableLiveData<ResultState<UserInfo>>()

    // 方式 2  不用框架帮脱壳，判断结果是否成功
    // var loginResult2 = MutableLiveData<ResultState<ApiResponse<UserInfo>>>()

    fun loginReq(username: String, password: String) {
        // 1.这种是在 Activity/Fragment 的监听回调中拿到已脱壳的数据（项目有基类的可以用）
        request(
            { apiService.login(username, password) }// 请求体
            , loginResult,// 请求的返回结果，请求成功与否都会改变该值，在 Activity 或 fragment 中监听回调结果，具体可看 loginActivity 中的回调
            true,// 是否显示等待框，，默认false不显示 可以默认不传
            "正在登录中..."// 等待框内容，可以默认不填请求网络中...
        )
        // 2.这种是在 Activity/Fragment 中的监听拿到未脱壳的数据，你可以自己根据 code 做业务需求操作（项目没有基类的可以用）
        /*requestNoCheck({HttpRequestCoroutine.login(username,password)},loginResult2,true)*/

        // 3. 这种是直接在当前 ViewModel 中就拿到了脱壳数据数据，做一层封装再给 Activity/Fragment，如果 （项目有基类的可以用）
        /* request( {HttpRequestCoroutine.login(username,password) }, {
             // 请求成功 已自动处理了 请求结果是否正常
         },{
             // 请求失败 网络异常，或者请求结果码错误都会回调在这里
         })*/

        // 4.这种是直接在当前 ViewModel 中就拿到了未脱壳数据数据，（项目没有基类的可以用）
        /*requestNoCheck({ HttpRequestCoroutine.login(username, password )}, {
            // 请求成功 自己拿到数据做业务需求操作
            if (it.errorCode == 0) {
                // 结果正确
            } else {
                // 结果错误
            }
        },{
            // 请求失败 网络异常回调在这里
        })*/
    }

    fun registerAndLogin(username: String, password: String) {
        request(
            { HttpRequestCoroutine.register(username, password) }
            , loginResult,
            true,
            "正在注册中..."
        )
    }
}