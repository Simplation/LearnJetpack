package com.simplation.mvvm.viewmodel.state

import com.simplation.mvvm.data.model.enums.TodoType
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.callback.databind.IntObservableField
import com.simplation.mvvmlib.callback.databind.StringObservableField
import com.simplation.mvvmlib.ext.launch

/**
 * Todo view model
 *
 * @constructor Create empty Todo view model
 */
class TodoViewModel : BaseViewModel() {
    // 标题
    var todoTitle = StringObservableField()

    // 内容
    var todoContent =
        StringObservableField()

    // 时间
    var todoTime = StringObservableField()

    // 优先级
    var todoLeve =
        StringObservableField(TodoType.TodoType1.content)

    // 优先级颜色
    var todoColor =
        IntObservableField(TodoType.TodoType1.color)

    fun xx(): Unit {
        launch({

        },{

        })
    }
}
