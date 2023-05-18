package com.simplation.mvvm.viewmodel.state

import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.callback.databind.StringObservableField

/**
 * Article view model
 *
 * @constructor Create empty Article view model
 */
class ArticleViewModel : BaseViewModel() {
    // 分享文章标题
    var shareTitle = StringObservableField()

    // 分享文章网址
    var shareUrl = StringObservableField()

    // 分享文章的人
    var shareName = StringObservableField()
}
