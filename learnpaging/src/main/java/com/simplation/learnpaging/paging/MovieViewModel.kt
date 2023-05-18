package com.simplation.learnpaging.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.simplation.learnpaging.model.Movie

class MovieViewModel : ViewModel() {
    lateinit var moviePageList: LiveData<PagedList<Movie>>

    fun MovieViewModel() {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)    // 用于设置控件占位
            .setPageSize(8)    // MovieDataSource.PER_PAGE  设置每页的大小
            .setPrefetchDistance(3)     // 设置当距离底部还有多少条数据时开始加载下一页数据。
            .setInitialLoadSizeHint(8 * 4)  // MovieDataSource.PER_PAGE * 4   设置首次加载数据的数量。
            .setMaxSize(65536 * 8)  // 65536 * MovieDataSource.PER_PAGE   设置PagedList所能承受的最大数量
            .build()

        moviePageList = LivePagedListBuilder(MovieDataSourceFactory(), config).build()

    }
}