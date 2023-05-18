package com.simplation.learnpaging.paging

import androidx.lifecycle.MutableLiveData
import com.simplation.learnpaging.model.Movie

class MovieDataSourceFactory : androidx.paging.DataSource.Factory<Int, Movie>() {

    val liveDataSource = MutableLiveData<Movie>()

    override fun create(): androidx.paging.DataSource<Int, Movie> {
        val dataSource = MovieDataSource()
//        liveDataSource.postValue(dataSource)   // 提示添加扩展方法
        return dataSource
    }
}