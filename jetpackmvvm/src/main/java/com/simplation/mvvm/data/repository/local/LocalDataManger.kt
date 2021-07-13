package com.simplation.mvvm.data.repository.local

class LocalDataManger {
    companion object {
        val instance: LocalDataManger by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LocalDataManger()
        }
    }
}
