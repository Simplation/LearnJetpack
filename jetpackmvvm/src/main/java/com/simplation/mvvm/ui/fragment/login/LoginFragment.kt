package com.simplation.mvvm.ui.fragment.login

import android.os.Build
import android.os.Bundle
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.simplation.mvvm.R
import com.simplation.mvvm.app.appViewModel
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.hideSoftKeyboard
import com.simplation.mvvm.app.ext.initClose
import com.simplation.mvvm.app.ext.showMessage
import com.simplation.mvvm.app.util.CacheUtil
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.databinding.FragmentLoginBinding
import com.simplation.mvvm.viewmodel.request.RequestLoginRegisterViewModel
import com.simplation.mvvm.viewmodel.state.LoginRegisterViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.simplation.mvvmlib.ext.parseState

/**
 * Login fragment
 *
 * @constructor Create empty Login fragment
 */
class LoginFragment : BaseFragment<LoginRegisterViewModel, FragmentLoginBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()
    private lateinit var toolbar: Toolbar

    override fun layoutId(): Int = R.layout.fragment_login

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        mDataBind.viewmodel = mViewModel

        mDataBind.click = ProxyClick()

        toolbar = mDataBind.root.findViewById(R.id.toolbar)
        toolbar.initClose("登录") {
            nav().navigateUp()
        }

        // 设置颜色跟主题颜色一致
        appViewModel.appColor.value?.let {
            SettingUtil.setShapeColor(mDataBind.loginSub, it)
            mDataBind.loginGoregister.setTextColor(it)
            toolbar.setBackgroundColor(it)
        }

    }

    override fun createObserver() {

        requestLoginRegisterViewModel.loginResult.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                // 登录成功 通知账户数据发生改变
                CacheUtil.setUser(it)
                CacheUtil.setIsLogin(true)
                appViewModel.userInfo.value = it
                nav().navigateUp()
            }, {
                // 登录失败
                showMessage(it.errMsg)
            })
        })
    }

    inner class ProxyClick {

        fun clear() {
            mViewModel.username.set("")
        }

        fun login() {
            when {
                mViewModel.username.get().isEmpty() -> showMessage("请填写账号")
                mViewModel.password.get().isEmpty() -> showMessage("请填写密码")
                else -> requestLoginRegisterViewModel.loginReq(
                    mViewModel.username.get(),
                    mViewModel.password.get()
                )
            }
        }

        fun goRegister() {
            hideSoftKeyboard(activity)
            nav().navigateAction(R.id.action_loginFragment_to_registerFrgment)
        }

        var onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { _, isChecked ->
                mViewModel.isShowPwd.set(isChecked)
            }
    }
}