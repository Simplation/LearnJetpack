package com.simplation.mvvm.viewmodel.state

import android.view.View
import androidx.databinding.ObservableInt
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.callback.databind.BooleanObservableField
import com.simplation.mvvmlib.callback.databind.StringObservableField

/**
 * @作者: Simplation
 * @日期: 2021/4/26 11:12
 * @描述:
 * @更新:
 */

class LoginRegisterViewModel : BaseViewModel() {
    // 用户名
    val username = StringObservableField()

    // 密码（登录注册页面）
    val password = StringObservableField()
    val password2 = StringObservableField()

    // 是否显示明文密码（登录注册界面）
    val isShowPwd = BooleanObservableField()
    val isShowPwd2 = BooleanObservableField()

    // 用户名清除按钮是否显示   不要在 xml 中写逻辑 所以逻辑判断放在这里
    var clearVisible = object : ObservableInt(username) {
        override fun get(): Int {
            return if (username.get().isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    // 密码显示按钮是否显示   不要在 xml 中写逻辑 所以逻辑判断放在这里
    var passwordVisible = object : ObservableInt(password) {
        override fun get(): Int {
            return if (password.get().isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    // 密码显示按钮是否显示   不要在 xml 中写逻辑 所以逻辑判断放在这里
    var passwordVisible2 = object : ObservableInt(password2) {
        override fun get(): Int {
            return if (password2.get().isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

}
