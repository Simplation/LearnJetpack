package com.simplation.learnviewpager

import android.app.Application
import androidx.lifecycle.AndroidViewModel

/**
 * @作者: Simplation
 * @日期: 2021/12/08 10:09
 * @描述:
 * @更新:
 */
class RotateViewModel(application: Application) : AndroidViewModel(application) {
    var rotateValue: Float = 0F
}
