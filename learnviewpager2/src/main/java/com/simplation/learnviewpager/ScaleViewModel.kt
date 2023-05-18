package com.simplation.learnviewpager

import android.app.Application
import androidx.lifecycle.AndroidViewModel

/**
 * @作者: Simplation
 * @日期: 2021/12/08 9:20
 * @描述:
 * @更新:
 */
class ScaleViewModel(application: Application) : AndroidViewModel(application) {
    var scaleValue: Float = 1F
}