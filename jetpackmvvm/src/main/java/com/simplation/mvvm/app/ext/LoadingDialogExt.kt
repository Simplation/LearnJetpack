package com.simplation.mvvm.app.ext

import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.simplation.mvvm.R
import com.simplation.mvvm.app.util.SettingUtil

/* Dialog */
private var loadingDialog: MaterialDialog? = null

fun AppCompatActivity.showLoadingExt(message: String = "请求网络中...") {
    if (!this.isFinishing) {
        if (loadingDialog == null) {
            loadingDialog = MaterialDialog(this)
                .cancelable(true)
                .cancelOnTouchOutside(false)
                .cornerRadius(12f)
                .customView(R.layout.layout_custom_dialog_view)
                .lifecycleOwner(this)

            loadingDialog?.getCustomView()?.run {
                this.findViewById<ProgressBar>(R.id.progressBar).indeterminateTintList =
                    SettingUtil.getOneColorStateList(this@showLoadingExt)
                this.findViewById<TextView>(R.id.loading_tips).text = message
            }
        }
        loadingDialog?.show()
    }
}

fun Fragment.showLoadingExt(message: String = "请求网络中...") {
    activity?.let {
        if (!it.isFinishing) {
            if (loadingDialog == null) {
                loadingDialog = MaterialDialog(it)
                    .cancelable(true)
                    .cancelOnTouchOutside(false)
                    .cornerRadius(12f)
                    .customView(R.layout.layout_custom_dialog_view)
                    .lifecycleOwner(this)

                loadingDialog?.getCustomView()?.run {
                    this.findViewById<ProgressBar>(R.id.progressBar).indeterminateTintList =
                        SettingUtil.getOneColorStateList(it)
                    this.findViewById<TextView>(R.id.loading_tips).text = message
                }
            }
            loadingDialog?.show()
        }
    }
}

fun AppCompatActivity.dismissLoadingExt() {
    loadingDialog?.dismiss()
    loadingDialog = null
}

fun Fragment.dismissLoadingExt() {
    loadingDialog?.dismiss()
    loadingDialog = null
}
