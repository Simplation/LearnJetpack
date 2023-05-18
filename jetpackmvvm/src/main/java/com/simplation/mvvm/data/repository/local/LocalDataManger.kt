package com.simplation.mvvm.data.repository.local

/**
 * Local data manger
 *
 * @constructor Create empty Local data manger
 */
class LocalDataManger {
    companion object {
        val instance: LocalDataManger by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LocalDataManger()
        }
    }
}
