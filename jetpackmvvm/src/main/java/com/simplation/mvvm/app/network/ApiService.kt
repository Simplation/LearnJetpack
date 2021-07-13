package com.simplation.mvvm.app.network

import com.simplation.mvvm.data.model.bean.ApiPagerResponse
import com.simplation.mvvm.data.model.bean.ApiResponse
import com.simplation.mvvm.data.model.bean.ArticleResponse
import com.simplation.mvvm.data.model.bean.UserInfo
import retrofit2.http.*


interface ApiService {

    companion object {
        const val SERVER_URL = "https://wanandroid.com/"
        const val SERVER_URL1 = "https://wanandroid.com/"
    }

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") pwd: String
    ): ApiResponse<UserInfo>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String, @Field("password") pwd: String, @Field(
            "repassword"
        ) rpwd: String
    ): ApiResponse<Any>

    /**
     * 获取置顶文章集合数据
     */
    @GET("article/top/json")
    suspend fun getTopArticleList(): ApiResponse<ArrayList<ArticleResponse>>

    /**
     * 获取首页文章数据
     */
    @GET("article/list/{page}/json")
    suspend fun getArticleList(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>>

    /**
     * 根据分类id获取项目数据
     */
    @GET("project/list/{page}/json")
    suspend fun getProjectDataByType(
        @Path("page") pageNo: Int,
        @Query("cid") cid: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>>

    /**
     * 获取最新项目数据
     */
    @GET("article/listproject/{page}/json")
    suspend fun getProjecNewData(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>>

}