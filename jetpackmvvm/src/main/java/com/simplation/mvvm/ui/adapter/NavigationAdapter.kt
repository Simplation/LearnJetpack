package com.simplation.mvvm.ui.adapter

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.simplation.mvvm.R
import com.simplation.mvvm.app.ext.setAdapterAnimation
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.data.model.bean.ArticleResponse
import com.simplation.mvvm.data.model.bean.NavigationResponse
import com.simplation.mvvmlib.ext.util.toHtml


/**
 * Navigation adapter
 *
 * @constructor
 *
 * @param data
 */
class NavigationAdapter(data: ArrayList<NavigationResponse>) :
    BaseQuickAdapter<NavigationResponse, BaseViewHolder>(
        R.layout.item_system, data
    ) {

    private var navigationAction: (item: ArticleResponse, view: View) -> Unit =
        { _: ArticleResponse, _: View -> }

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun convert(holder: BaseViewHolder, item: NavigationResponse) {
        holder.setText(R.id.item_system_title, item.name.toHtml())
        holder.getView<RecyclerView>(R.id.item_system_rv).run {
            val foxLayoutManager: FlexboxLayoutManager by lazy {
                FlexboxLayoutManager(context).apply {
                    // 方向 主轴为水平方向，起点在左端
                    flexDirection = FlexDirection.ROW
                    // 左对齐
                    justifyContent = JustifyContent.FLEX_START
                }
            }
            layoutManager = foxLayoutManager
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = NavigationChildAdapter(item.articles).apply {
                setOnItemClickListener { _, view, position ->
                    navigationAction.invoke(item.articles[position], view)
                }
            }
        }
    }

    fun setNavigationAction(inputNavigationAction: (item: ArticleResponse, view: View) -> Unit) {
        this.navigationAction = inputNavigationAction
    }
}