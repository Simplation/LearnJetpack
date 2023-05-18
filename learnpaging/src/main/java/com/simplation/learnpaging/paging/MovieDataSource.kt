package com.simplation.learnpaging.paging

import androidx.paging.PositionalDataSource
import com.simplation.learnpaging.api.RetrofitClient
import com.simplation.learnpaging.model.Movie
import com.simplation.learnpaging.model.Movies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDataSource : PositionalDataSource<Movie>() {

//    val PER_PAGE = 8

    private val PER_PAGE = 8

    //region 当页面首次加载数据时会调用 loadInitial() 方法。
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Movie>) {
        val startPosition = 0

        RetrofitClient().getInstance().getApi().getMovies(startPosition, PER_PAGE).enqueue(
            object : Callback<Movies> {
                override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                    if (response.body() != null) {
                        callback.onResult(
                            response.body()!!.movieList,
                            response.body()!!.start,
                            response.body()!!.total
                        )
                    }
                }

                override fun onFailure(call: Call<Movies>, t: Throwable) {
                }

            }
        )
    }
    //endregion

    //region loadInitial() 方法的作用是负责第一页数据的加载，当第一页数据顺利加载后，接着加载下一页的工作会在 loadRange() 方法内进行。
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Movie>) {
        RetrofitClient().getInstance().getApi().getMovies(params.startPosition, PER_PAGE).enqueue(
            object : Callback<Movies> {
                override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                    if (response.body() != null) {
                        callback.onResult(response.body()!!.movieList)
                    }
                }

                override fun onFailure(call: Call<Movies>, t: Throwable) {
                }
            }
        )
    }
    //endregion
}

