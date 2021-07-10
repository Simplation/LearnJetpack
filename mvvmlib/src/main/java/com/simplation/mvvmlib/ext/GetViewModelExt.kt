package com.simplation.mvvmlib.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.simplation.mvvmlib.base.BaseApp
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType


/**
 * 获取当前类绑定的泛型 ViewModel-clazz
 */
@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}

/**
 * 在 Activity 中得到 Application 上下文的 ViewModel
 */
inline fun <reified VM : BaseViewModel> AppCompatActivity.getAppViewModel(): VM {
    (this.application as? BaseApp).let {
        if (it == null) {
            throw NullPointerException("你的 Application 没有继承框架自带的 BaseApp 类，暂时无法使用 getAppViewModel 该方法")
        } else {
            return it.getAppViewModelProvider().get(VM::class.java)
        }
    }
}

/**
 * 在 Fragment 中得到 Application 上下文的 ViewModel
 * 提示，在 fragment 中调用该方法时，请在该 Fragment onCreate 以后调用或者请用 by lazy 方式懒加载初始化调用，不然会提示 requireActivity 没有导致错误
 */
inline fun <reified VM : BaseViewModel> Fragment.getAppViewModel(): VM {
    (this.requireActivity().application as? BaseApp).let {
        if (it == null) {
            throw NullPointerException("你的 Application 没有继承框架自带的 BaseApp 类，暂时无法使用 getAppViewModel 该方法")
        } else {
            return it.getAppViewModelProvider().get(VM::class.java)
        }
    }
}

/**
 * 得到当前 Activity 上下文的 ViewModel
 */
@Deprecated("已过时的方法，现在可以直接使用 Ktx 函数 viewmodels() 获取")
inline fun <reified VM : BaseViewModel> AppCompatActivity.getViewModel(): VM {
    return ViewModelProvider(
        this,
        ViewModelProvider.AndroidViewModelFactory(application)
    ).get(VM::class.java)
}

/**
 * 得到当前 Fragment 上下文的 ViewModel
 * 提示，在 fragment 中调用该方法时，请在该 Fragment onCreate 以后调用或者请用 by lazy 方式懒加载初始化调用，不然会提示 requireActivity 没有导致错误
 */
@Deprecated("已过时的方法，现在可以直接使用 Ktx 函数 viewmodels() 获取")
inline fun <reified VM : BaseViewModel> Fragment.getViewModel(): VM {
    return ViewModelProvider(
        this,
        ViewModelProvider.AndroidViewModelFactory(this.requireActivity().application)
    ).get(VM::class.java)
}

/**
 * 在 Fragment 中得到父类Activity的共享ViewModel
 * 提示，在 fragment 中调用该方法时，请在该 Fragment onCreate 以后调用或者请用 by lazy 方式懒加载初始化调用，不然会提示 requireActivity 没有导致错误
 */
@Deprecated("已过时的方法，现在可以直接使用 Ktx 函数 activityViewModels() 获取")
inline fun <reified VM : BaseViewModel> Fragment.getActivityViewModel(): VM {
    return ViewModelProvider(requireActivity(),
        ViewModelProvider.AndroidViewModelFactory(this.requireActivity().application)
    ).get(VM::class.java)
}
