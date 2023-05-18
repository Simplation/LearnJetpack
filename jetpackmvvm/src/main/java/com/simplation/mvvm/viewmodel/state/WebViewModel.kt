package com.simplation.mvvm.viewmodel.state

import com.simplation.mvvmlib.base.viewmodel.BaseViewModel

/**
 * Web view model
 *
 * @constructor Create empty Web view model
 */
class WebViewModel : BaseViewModel() {
    // 是否收藏
    var collect = false

    // 收藏的 Id
    var ariticleId = 0

    // 标题
    var showTitle: String = ""

    // 文章的网络访问路径
    var url: String = ""

    // 需要收藏的类型 具体参数说明请看 CollectType 枚举类
    var collectType = 0
}
