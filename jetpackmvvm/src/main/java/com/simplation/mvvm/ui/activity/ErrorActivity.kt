package com.simplation.mvvm.ui.activity

import android.content.ClipData
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.blankj.utilcode.util.ToastUtils
import com.simplation.mvvm.R
import com.simplation.mvvm.app.base.BaseActivity
import com.simplation.mvvm.app.ext.init
import com.simplation.mvvm.app.ext.showMessage
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.app.util.StatusBarUtil
import com.simplation.mvvm.databinding.ActivityErrorBinding
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.ext.util.clipboardManager
import com.simplation.mvvmlib.ext.view.clickNoRepeat

class ErrorActivity : BaseActivity<BaseViewModel, ActivityErrorBinding>() {

    override fun layoutId(): Int = R.layout.activity_error

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.root.findViewById<Toolbar>(R.id.toolbar).init("出现问题!")

        supportActionBar?.setBackgroundDrawable(ColorDrawable(SettingUtil.getColor(this)))
        StatusBarUtil.setColor(this, SettingUtil.getColor(this), 0)
        val config = CustomActivityOnCrash.getConfigFromIntent(intent)
        mDatabind.errorRestart.clickNoRepeat {
            config?.run {
                CustomActivityOnCrash.restartApplication(this@ErrorActivity, this)
            }
        }

        mDatabind.errorSendError.clickNoRepeat {
            CustomActivityOnCrash.getStackTraceFromIntent(intent)?.let {
                showMessage(it, "发现有 Bug 不去打作者脸？", "必须打", {
                    val mClipData = ClipData.newPlainText("errorLog", it)
                    // 将 ClipData 内容放到系统剪贴板里。
                    clipboardManager?.setPrimaryClip(mClipData)
                    ToastUtils.showShort("已复制错误日志")
                    try {
                        val url = "mqqwpa://im/chat?chat_type=wpa&uin=824868922"
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    } catch (e: Exception) {
                        ToastUtils.showShort("请先安装 QQ")
                    }
                }, "我不敢")
            }
        }
    }
}
