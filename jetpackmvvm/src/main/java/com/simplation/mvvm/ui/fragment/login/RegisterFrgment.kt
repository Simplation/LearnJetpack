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
import com.simplation.mvvm.app.ext.initClose
import com.simplation.mvvm.app.ext.showMessage
import com.simplation.mvvm.app.util.CacheUtil
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.databinding.FragmentRegisterBinding
import com.simplation.mvvm.viewmodel.request.RequestLoginRegisterViewModel
import com.simplation.mvvm.viewmodel.state.LoginRegisterViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.simplation.mvvmlib.ext.parseState

/**
 * Register frgment
 *
 * @constructor Create empty Register frgment
 */
class RegisterFrgment : BaseFragment<LoginRegisterViewModel, FragmentRegisterBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()

    private lateinit var toolbar: Toolbar

    override fun layoutId() = R.layout.fragment_register

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        mDataBind.viewmodel = mViewModel
        mDataBind.click = ProxyClick()

        toolbar = mDataBind.root.findViewById(R.id.toolbar)
        toolbar.initClose("注册") {
            nav().navigateUp()
        }

        // 设置颜色跟主题颜色一致
        appViewModel.appColor.value?.let {
            SettingUtil.setShapeColor(mDataBind.registerSub, it)
            toolbar.setBackgroundColor(it)
        }
    }

    override fun createObserver() {
        requestLoginRegisterViewModel.loginResult.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                CacheUtil.setIsLogin(true)
                CacheUtil.setUser(it)
                appViewModel.userInfo.value = it
                nav().navigateAction(R.id.action_registerFrgment_to_mainFragment)
            }, {
                showMessage(it.errMsg)
            })
        })
    }


    inner class ProxyClick {
        // 清空
        fun clear() {
            mViewModel.username.set("")
        }

        // 注册
        fun register() {
            when {
                mViewModel.username.get().isEmpty() -> showMessage("请填写账号")
                mViewModel.password.get().isEmpty() -> showMessage("请填写密码")
                mViewModel.password2.get().isEmpty() -> showMessage("请填写确认密码")
                mViewModel.password.get().length < 6 -> showMessage("密码最少 6 位")
                mViewModel.password.get() != mViewModel.password2.get() -> showMessage("密码不一致")
                else -> requestLoginRegisterViewModel.registerAndLogin(
                    mViewModel.username.get(),
                    mViewModel.password.get()
                )
            }
        }

        var onCheckedChangeListener1 = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            mViewModel.isShowPwd.set(isChecked)
        }

        var onCheckedChangeListener2 = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            mViewModel.isShowPwd2.set(isChecked)
        }
    }
}
