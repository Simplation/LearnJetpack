package com.simplation.mvvm.ui.fragment.setting

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.color.colorChooser
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.blankj.utilcode.util.AppUtils
import com.simplation.mvvm.R
import com.simplation.mvvm.app.event.AppViewModel
import com.simplation.mvvm.app.ext.initClose
import com.simplation.mvvm.app.ext.showMessage
import com.simplation.mvvm.app.network.NetworkApi
import com.simplation.mvvm.app.util.CacheDataManager
import com.simplation.mvvm.app.util.CacheUtil
import com.simplation.mvvm.app.util.ColorUtil
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.app.weight.preference.CheckBoxPreference
import com.simplation.mvvm.app.weight.preference.IconPreference
import com.simplation.mvvm.app.weight.preference.PreferenceCategory
import com.simplation.mvvm.data.model.bean.BannerResponse
import com.simplation.mvvmlib.ext.getAppViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.tencent.bugly.beta.Beta

/**
 * Setting fragment
 *
 * @constructor Create empty Setting fragment
 */
class SettingFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    // 这里不能继承 BaseFragment了，所以手动获取一下 AppViewModel
    val shareViewModel: AppViewModel by lazy { getAppViewModel() }

    private var colorPreview: IconPreference? = null

    var toolbarView: View? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 这里重写根据 PreferenceFragmentCompat 的布局 ，往他的根布局插入了一个 toolbar
        val containerView = view.findViewById<FrameLayout>(android.R.id.list_container)
        containerView.let {
            // 转为线性布局
            val linearLayout = it.parent as? LinearLayout
            linearLayout?.run {
                toolbarView = LayoutInflater.from(activity).inflate(R.layout.include_toolbar, null)
                toolbarView?.let { view ->
                    view.findViewById<Toolbar>(R.id.toolbar)?.initClose("设置") {
                        nav().navigateUp()
                    }
                    // 添加到第一个
                    addView(toolbarView, 0)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.root_preferences)

        colorPreview = findPreference("color")
        setText()
        findPreference<Preference>("exit")?.isVisible = CacheUtil.isLogin()// 未登录时，退出登录需要隐藏

        findPreference<Preference>("exit")?.setOnPreferenceClickListener {
            showMessage(
                "确定退出登录吗",
                positiveButtonText = "退出",
                negativeButtonText = "取消",
                positiveAction = {
                    // 清空 cookie
                    NetworkApi.INSTANCE.cookieJar.clear()
                    CacheUtil.setUser(null)
                    shareViewModel.userInfo.value = null
                    nav().navigateUp()
                })
            false
        }

        findPreference<Preference>("clearCache")?.setOnPreferenceClickListener {
            showMessage(
                "确定清理缓存吗",
                positiveButtonText = "清理",
                negativeButtonText = "取消",
                positiveAction = {
                    activity?.let { CacheDataManager.clearAllCache(it as? AppCompatActivity) }
                    setText()
                })
            false
        }
        findPreference<Preference>("mode")?.setOnPreferenceClickListener {
            activity?.let { activity ->
                MaterialDialog(activity).show {
                    cancelable(false)
                    lifecycleOwner(viewLifecycleOwner)
                    listItemsSingleChoice(
                        R.array.setting_modes,
                        initialSelection = SettingUtil.getListMode()
                    ) { _, index, text ->
                        SettingUtil.setListMode(index)
                        it.summary = text
                        // 通知其他界面立马修改配置
                        shareViewModel.appAnimation.value = index
                    }
                    title(text = "设置列表动画")
                    positiveButton(R.string.confirm)
                    negativeButton(R.string.cancel)
                    getActionButton(WhichButton.POSITIVE).updateTextColor(
                        SettingUtil.getColor(
                            activity
                        )
                    )
                    getActionButton(WhichButton.NEGATIVE).updateTextColor(
                        SettingUtil.getColor(
                            activity
                        )
                    )
                }
            }

            false
        }
        findPreference<IconPreference>("color")?.setOnPreferenceClickListener {
            activity?.let { activity ->
                MaterialDialog(activity).show {
                    title(R.string.choose_theme_color)
                    colorChooser(
                        ColorUtil.ACCENT_COLORS,
                        initialSelection = SettingUtil.getColor(activity),
                        subColors = ColorUtil.PRIMARY_COLORS_SUB
                    ) { _, color ->
                        // 修改颜色
                        SettingUtil.setColor(activity, color)
                        findPreference<PreferenceCategory>("base")?.setTitleColor(color)
                        findPreference<PreferenceCategory>("other")?.setTitleColor(color)
                        findPreference<PreferenceCategory>("about")?.setTitleColor(color)
                        findPreference<CheckBoxPreference>("top")?.setBottonColor()
                        toolbarView?.setBackgroundColor(color)
                        // 通知其他界面立马修改配置
                        shareViewModel.appColor.value = color
                    }
                    getActionButton(WhichButton.POSITIVE).updateTextColor(
                        SettingUtil.getColor(
                            activity
                        )
                    )
                    getActionButton(WhichButton.NEGATIVE).updateTextColor(
                        SettingUtil.getColor(
                            activity
                        )
                    )
                    positiveButton(R.string.done)
                    negativeButton(R.string.cancel)
                }
            }
            false
        }

        findPreference<Preference>("version")?.setOnPreferenceClickListener {
            Beta.checkUpgrade(true, false)
            false
        }
        findPreference<Preference>("copyRight")?.setOnPreferenceClickListener {
            activity?.let {
                showMessage(it.getString(R.string.copyright_tip))
            }
            false
        }
        findPreference<Preference>("author")?.setOnPreferenceClickListener {
            showMessage(
                title = "联系作者",
                message = "Q　Q：824868922\n\n微　信：hgj840\n\n邮　箱：824868922@qq.com"
            )
            false
        }
        findPreference<Preference>("project")?.setOnPreferenceClickListener {
            val data = BannerResponse(
                title = "一位练习时长两年半的菜虚鲲制作的玩安卓App",
                url = findPreference<Preference>("project")?.summary.toString()
            )
            view?.let {
                nav().navigateAction(R.id.action_to_webFragment, Bundle()
                    .apply { putParcelable("bannerdata", data) })
            }
            false
        }
    }

    /**
     * 初始化设值
     */
    private fun setText() {
        activity?.let {
            findPreference<CheckBoxPreference>("top")?.isChecked = CacheUtil.isNeedTop()

            findPreference<Preference>("clearCache")?.summary =
                CacheDataManager.getTotalCacheSize(it)

            findPreference<Preference>("version")?.summary = "当前版本 " + AppUtils.getAppVersionName()

            val modes = it.resources.getStringArray(R.array.setting_modes)
            findPreference<Preference>("mode")?.summary =
                modes[SettingUtil.getListMode()]
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == "color") {
            colorPreview?.setView()
        }
        if (key == "top") {
            CacheUtil.setIsNeedTop(sharedPreferences.getBoolean("top", true))
        }
    }

}
