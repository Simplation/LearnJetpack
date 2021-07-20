package com.simplation.mvvm.ui.fragment.share

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.simplation.mvvm.R
import com.simplation.mvvm.app.appViewModel
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.eventViewModel
import com.simplation.mvvm.app.ext.initClose
import com.simplation.mvvm.app.ext.showMessage
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.databinding.FragmentShareAriticleBinding
import com.simplation.mvvm.viewmodel.request.RequestAriticleViewModel
import com.simplation.mvvm.viewmodel.state.ArticleViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.parseState
import com.simplation.mvvmlib.ext.view.clickNoRepeat

/**
 * Add ariticle fragment
 *
 * @constructor Create empty Add ariticle fragment
 */
class AddAriticleFragment : BaseFragment<ArticleViewModel, FragmentShareAriticleBinding>() {

    private lateinit var toolBar: Toolbar

    private val requestViewModel: RequestAriticleViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_share_ariticle

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        mDataBind.vm = mViewModel
        appViewModel.userInfo.value?.let {
            if (it.nickname.isEmpty()) mViewModel.shareName.set(it.username) else mViewModel.shareName.set(
                it.nickname
            )
        }

        appViewModel.appColor.value?.let { SettingUtil.setShapeColor(mDataBind.shareSubmit, it) }

        toolBar = mDataBind.root.findViewById(R.id.toolbar)
        toolBar.run {
            initClose("分享文章") {
                nav().navigateUp()
            }
            inflateMenu(R.menu.share_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.share_guize -> {
                        activity?.let { activity ->
                            MaterialDialog(activity, BottomSheet())
                                .lifecycleOwner(viewLifecycleOwner)
                                .show {
                                    title(text = "温馨提示")
                                    customView(
                                        R.layout.customview,
                                        scrollable = true,
                                        horizontalPadding = true
                                    )
                                    positiveButton(text = "知道了")
                                    cornerRadius(16f)
                                    getActionButton(WhichButton.POSITIVE).updateTextColor(
                                        SettingUtil.getColor(activity)
                                    )
                                    getActionButton(WhichButton.NEGATIVE).updateTextColor(
                                        SettingUtil.getColor(activity)
                                    )
                                }
                        }
                    }
                }
                true
            }
        }

        mDataBind.shareSubmit.clickNoRepeat {
            when {
                mViewModel.shareTitle.get().isEmpty() -> {
                    showMessage("请填写文章标题")
                }
                mViewModel.shareUrl.get().isEmpty() -> {
                    showMessage("请填写文章链接")
                }
                else -> {
                    showMessage("确定分享吗？", positiveButtonText = "分享", positiveAction = {
                        requestViewModel.addAriticle(
                            mViewModel.shareTitle.get(),
                            mViewModel.shareUrl.get()
                        )
                    }, negativeButtonText = "取消")
                }
            }
        }
    }

    override fun createObserver() {
        requestViewModel.addData.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                eventViewModel.shareArticleEvent.value = true
                nav().navigateUp()
            }, {
                showMessage(it.errMsg)
            })
        })
    }
}