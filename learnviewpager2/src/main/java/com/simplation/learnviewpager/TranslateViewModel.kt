package com.simplation.learnviewpager

import android.app.Application
import androidx.lifecycle.AndroidViewModel

/**
 * @作者: Simplation
 * @日期: 2021/12/08 10:16
 * @描述:
 * @更新:
 */
class TranslateViewModel(application: Application) : AndroidViewModel(application) {
    var translateValue: Float = 0F
}