package com.simplation.mvvm.ui.fragment.web

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.Window
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.VibrateUtils
import com.just.agentweb.AgentWeb
import com.simplation.mvvm.R
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.eventViewModel
import com.simplation.mvvm.app.ext.hideSoftKeyboard
import com.simplation.mvvm.app.ext.initClose
import com.simplation.mvvm.app.ext.showMessage
import com.simplation.mvvm.app.util.CacheUtil
import com.simplation.mvvm.data.model.bean.*
import com.simplation.mvvm.data.model.enums.CollectType
import com.simplation.mvvm.databinding.FragmentWebBinding
import com.simplation.mvvm.viewmodel.request.RequestCollectViewModel
import com.simplation.mvvm.viewmodel.state.WebViewModel
import com.simplation.mvvmlib.ext.nav

/**
 * Web fragment
 *
 * @constructor Create empty Web fragment
 */
class WebFragment : BaseFragment<WebViewModel, FragmentWebBinding>() {

    private lateinit var toolbar: Toolbar

    private var mAgentWeb: AgentWeb? = null

    private var preWeb: AgentWeb.PreAgentWeb? = null

    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_web

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        arguments?.run {
            // 点击文章进来的
            getParcelable<ArticleResponse>("ariticleData")?.let {
                mViewModel.ariticleId = it.id
                mViewModel.showTitle = it.title
                mViewModel.collect = it.collect
                mViewModel.url = it.link
                mViewModel.collectType = CollectType.Article.type
            }

            // 点击首页轮播图进来的
            getParcelable<BannerResponse>("bannerdata")?.let {
                mViewModel.ariticleId = it.id
                mViewModel.showTitle = it.title
                // 从首页轮播图 没法判断是否已经收藏过，所以直接默认没有收藏
                mViewModel.collect = false
                mViewModel.url = it.url
                mViewModel.collectType = CollectType.Url.type
            }

            // 从收藏文章列表点进来的
            getParcelable<CollectResponse>("collect")?.let {
                mViewModel.ariticleId = it.originId
                mViewModel.showTitle = it.title
                // 从收藏列表过来的，肯定 是 true 了
                mViewModel.collect = true
                mViewModel.url = it.link
                mViewModel.collectType = CollectType.Article.type
            }

            // 点击收藏网址列表进来的
            getParcelable<CollectUrlResponse>("collectUrl")?.let {
                mViewModel.ariticleId = it.id
                mViewModel.showTitle = it.name
                // 从收藏列表过来的，肯定 是 true 了
                mViewModel.collect = true
                mViewModel.url = it.link
                mViewModel.collectType = CollectType.Url.type
            }
        }

        toolbar = mDataBind.root.findViewById(R.id.toolbar)
        toolbar.run {
            // 设置 menu 关键代码
            mActivity.setSupportActionBar(this)
            initClose(mViewModel.showTitle) {
                hideSoftKeyboard(activity)
                mAgentWeb?.let { web ->
                    if (web.webCreator.webView.canGoBack()) {
                        web.webCreator.webView.goBack()
                    } else {
                        nav().navigateUp()
                    }
                }
            }
        }

        preWeb = AgentWeb.with(this)
            .setAgentWebParent(mDataBind.webcontent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
    }

    override fun lazyLoadData() {
        // 加载网页
        mAgentWeb = preWeb?.go(mViewModel.url)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    mAgentWeb?.let { web ->
                        if (web.webCreator.webView.canGoBack()) {
                            web.webCreator.webView.goBack()
                        } else {
                            nav().navigateUp()
                        }
                    }
                }
            })
    }

    override fun createObserver() {
        requestCollectViewModel.collectUiState.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                mViewModel.collect = it.collect
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
                // 刷新一下 menu
                mActivity.window?.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
                mActivity.invalidateOptionsMenu()
            } else {
                showMessage(it.errorMsg)
            }
        })

        requestCollectViewModel.collectUrlUiState.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
                mViewModel.collect = it.collect
                // 刷新一下 menu
                mActivity.window?.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
                mActivity.invalidateOptionsMenu()
            } else {
                showMessage(it.errorMsg)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.web_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        // 如果收藏了，右上角的图标相对应改变
        context?.let {
            if (mViewModel.collect) {
                menu.findItem(R.id.web_collect).icon =
                    ContextCompat.getDrawable(it, R.mipmap.ic_collected)
            } else {
                menu.findItem(R.id.web_collect).icon =
                    ContextCompat.getDrawable(it, R.mipmap.ic_collect)
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.web_share -> {
                // 分享
                startActivity(Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "{${mViewModel.showTitle}}:${mViewModel.url}")
                    type = "text/plain"
                }, "分享到"))
            }
            R.id.web_refresh -> {
                // 刷新网页
                mAgentWeb?.urlLoader?.reload()
            }
            R.id.web_collect -> {
                // 点击收藏 震动一下
                VibrateUtils.vibrate(40)
                // 是否已经登录了，没登录需要跳转到登录页去
                if (CacheUtil.isLogin()) {
                    // 是否已经收藏了
                    if (mViewModel.collect) {
                        if (mViewModel.collectType == CollectType.Url.type) {
                            // 取消收藏网址
                            requestCollectViewModel.uncollectUrl(mViewModel.ariticleId)
                        } else {
                            // 取消收藏文章
                            requestCollectViewModel.uncollect(mViewModel.ariticleId)
                        }
                    } else {
                        if (mViewModel.collectType == CollectType.Url.type) {
                            // 收藏网址
                            requestCollectViewModel.collectUrl(mViewModel.showTitle, mViewModel.url)
                        } else {
                            // 收藏文章
                            requestCollectViewModel.collect(mViewModel.ariticleId)
                        }
                    }
                } else {
                    // 跳转到登录页
                    nav().navigate(R.id.action_to_loginFragment)
                }
            }
            R.id.web_liulanqi -> {
                // 用浏览器打开
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mViewModel.url)))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        mActivity.setSupportActionBar(null)
        super.onDestroy()
    }
}