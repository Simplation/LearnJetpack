package com.simplation.mvvm.app

import android.os.Build
import androidx.annotation.RequiresApi
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.simplation.mvvm.app.event.AppViewModel
import com.simplation.mvvm.app.event.EventViewModel
import com.simplation.mvvm.app.ext.getProcessName
import com.simplation.mvvm.app.weight.loadCallback.EmptyCallback
import com.simplation.mvvm.app.weight.loadCallback.ErrorCallback
import com.simplation.mvvm.app.weight.loadCallback.LoadingCallback
import com.simplation.mvvm.ui.activity.ErrorActivity
import com.simplation.mvvm.ui.activity.WelcomeActivity
import com.simplation.mvvmlib.base.BaseApp
import com.simplation.mvvmlib.ext.util.MvvmLog
import com.simplation.mvvmlib.ext.util.logd
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.BuildConfig
import com.tencent.mmkv.MMKV

/**
 * App
 *
 * @constructor Create empty App
 */

// Application 全局的 ViewModel，里面存放了一些账户信息，基本配置信息等
val appViewModel: AppViewModel by lazy { App.appViewModelInstance }

// Application 全局的 ViewModel，用于发送全局通知操作
val eventViewModel: EventViewModel by lazy { App.eventViewModelInstance }

class App : BaseApp() {
    companion object {
        lateinit var instance: App
        lateinit var eventViewModelInstance: EventViewModel
        lateinit var appViewModelInstance: AppViewModel
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this,this.filesDir.absolutePath + "/mmkv")
        instance = this
        eventViewModelInstance = getAppViewModelProvider().get(EventViewModel::class.java)
        appViewModelInstance = getAppViewModelProvider().get(AppViewModel::class.java)

        // MultiDex.install(this)
        // 界面加载管理 初始化
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())                     // 加载
            .addCallback(ErrorCallback())                       // 错误
            .addCallback(EmptyCallback())                       // 空
            .setDefaultCallback(SuccessCallback::class.java)    // 设置默认加载状态页
            .commit()
        // 初始化 Bugly
        val context = applicationContext
        // 获取当前包名
        val packageName = context.packageName
        // 获取当前进程名
        val processName = getProcessName(android.os.Process.myPid())
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(context)
        strategy.isUploadProcess = processName == null || processName == packageName
        // 初始化 Bugly
        Bugly.init(context, if (BuildConfig.DEBUG) "xxx" else "a52f2b5ebb", BuildConfig.DEBUG)
        "".logd()
        MvvmLog = BuildConfig.DEBUG


        // 防止项目崩溃，崩溃后打开错误界面
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) // default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            // 是否启用 CustomActivityOnCrash 崩溃拦截机制 必须启用！不然集成这个库干啥？？？
            .enabled(true)
            // 是否必须显示包含错误详细信息的按钮 default: true
            .showErrorDetails(false)
            // 是否必须显示“重新启动应用程序”按钮或“关闭应用程序”按钮 default: true
            .showRestartButton(false)
            // 是否必须重新堆栈堆栈跟踪 default: true
            .logErrorOnRestart(false)
            // 是否必须跟踪用户访问的活动及其生命周期调用 default: false
            .trackActivities(true)
            // 应用程序崩溃之间必须经过的时间 default: 3000
            .minTimeBetweenCrashesMs(2000)
            // 重启的 Activity
            .restartActivity(WelcomeActivity::class.java)
            // 发生错误跳转的 Activity
            .errorActivity(ErrorActivity::class.java)
            .apply()
    }
}