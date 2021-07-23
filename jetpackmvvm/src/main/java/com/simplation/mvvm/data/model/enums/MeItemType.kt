package com.simplation.mvvm.data.model.enums

/**
 * Me item type
 *
 * @property type
 * @constructor Create empty Me item type
 */
enum class MeItemType(val type: Int) {
    // 头部布局
    TopItem(1),

    // 圆角 Item
    RoundItem(2),

    // 分割线
    BackGroundItem(3),

    // 普通的列表
    ListItem(4),

    // 右边有数据的列表
    ListRightItem(4)
}
