package com.simplation.mvvm.ui.adapter

import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.simplation.mvvm.R
import com.simplation.mvvm.app.ext.setAdapterAnimation
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.app.weight.custom.CollectView
import com.simplation.mvvm.data.model.bean.ArticleResponse
import com.simplation.mvvmlib.ext.util.toHtml

/**
 * Ariticle adapter
 *
 * @constructor
 *
 * @param data
 */
class AriticleAdapter(data: MutableList<ArticleResponse>?) :
    BaseDelegateMultiAdapter<ArticleResponse, BaseViewHolder>(data) {
    // 文章类型
    private val Ariticle = 1
    // 项目类型 本来打算不区分文章和项目布局用统一布局的，但是布局完以后发现差异化蛮大的，所以还是分开吧
    private val Project = 2
    // 是否展示标签 tag 一般主页才用的到
    private var showTag = false

    private var collectAction: (item: ArticleResponse, v: CollectView, position: Int) -> Unit =
        { _: ArticleResponse, _: CollectView, _: Int -> }

    constructor(data: MutableList<ArticleResponse>?, showTag: Boolean) : this(data) {
        this.showTag = showTag
    }

    init {
        setAdapterAnimation(SettingUtil.getListMode())
        // 第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<ArticleResponse>() {
            override fun getItemType(data: List<ArticleResponse>, position: Int): Int {
                // 根据是否有图片 判断为文章还是项目，好像有点 low 的感觉。。。我看实体类好像没有相关的字段，就用了这个，也有可能是我没发现
                return if (TextUtils.isEmpty(data[position].envelopePic)) Ariticle else Project
            }
        })
        // 第二步，绑定 item 类型
        getMultiTypeDelegate()?.let {
            it.addItemType(Ariticle, R.layout.item_ariticle)
            it.addItemType(Project, R.layout.item_project)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun convert(holder: BaseViewHolder, item: ArticleResponse) {
        when (holder.itemViewType) {
            Ariticle -> {
                // 文章布局的赋值
                item.run {
                    holder.setText(
                        R.id.item_home_author,
                        if (author.isNotEmpty()) author else shareUser
                    )
                    holder.setText(R.id.item_home_content, title.toHtml())
                    holder.setText(R.id.item_home_type2, "$superChapterName·$chapterName".toHtml())
                    holder.setText(R.id.item_home_date, niceDate)
                    holder.getView<CollectView>(R.id.item_home_collect).isChecked = collect
                    if (showTag) {
                        // 展示标签
                        holder.setGone(R.id.item_home_new, !fresh)
                        holder.setGone(R.id.item_home_top, type != 1)
                        if (tags.isNotEmpty()) {
                            holder.setGone(R.id.item_home_type1, false)
                            holder.setText(R.id.item_home_type1, tags[0].name)
                        } else {
                            holder.setGone(R.id.item_home_type1, true)
                        }
                    } else {
                        // 隐藏所有标签
                        holder.setGone(R.id.item_home_top, true)
                        holder.setGone(R.id.item_home_type1, true)
                        holder.setGone(R.id.item_home_new, true)
                    }
                }
                holder.getView<CollectView>(R.id.item_home_collect)
                    .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            collectAction.invoke(item, v, holder.adapterPosition)
                        }
                    })
            }
            Project -> {
                // 项目布局的赋值
                item.run {
                    holder.setText(
                        R.id.item_project_author,
                        if (author.isNotEmpty()) author else shareUser
                    )
                    holder.setText(R.id.item_project_title, title.toHtml())
                    holder.setText(R.id.item_project_content, desc.toHtml())
                    holder.setText(
                        R.id.item_project_type,
                        "$superChapterName·$chapterName".toHtml()
                    )

                    holder.setText(R.id.item_project_date, niceDate)

                    if (showTag) {
                        // 展示标签
                        holder.setGone(R.id.item_project_new, !fresh)
                        holder.setGone(R.id.item_project_top, type != 1)
                        if (tags.isNotEmpty()) {
                            holder.setGone(R.id.item_project_type1, false)
                            holder.setText(R.id.item_project_type1, tags[0].name)
                        } else {
                            holder.setGone(R.id.item_project_type1, true)
                        }
                    } else {
                        // 隐藏所有标签
                        holder.setGone(R.id.item_project_top, true)
                        holder.setGone(R.id.item_project_type1, true)
                        holder.setGone(R.id.item_project_new, true)
                    }

                    holder.getView<CollectView>(R.id.item_project_collect).isChecked = collect
                    Glide.with(context).load(envelopePic)
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(holder.getView(R.id.item_project_imageview))
                }

                holder.getView<CollectView>(R.id.item_project_collect)
                    .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            collectAction.invoke(item, v, holder.adapterPosition)
                        }
                    })
            }
        }
    }

    fun setCollectClick(inputCollectAction: (item: ArticleResponse, v: CollectView, position: Int) -> Unit) {
        this.collectAction = inputCollectAction
    }

}
