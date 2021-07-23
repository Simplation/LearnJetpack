package com.simplation.mvvm.ui.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.simplation.mvvm.R
import com.simplation.mvvm.app.ext.setAdapterAnimation
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.app.weight.custom.CollectView
import com.simplation.mvvm.data.model.bean.CollectUrlResponse
import com.simplation.mvvmlib.ext.util.toHtml

/**
 * Collect url adapter
 *
 * @constructor
 *
 * @param data
 */
class CollectUrlAdapter(data: ArrayList<CollectUrlResponse>) :
    BaseQuickAdapter<CollectUrlResponse, BaseViewHolder>(R.layout.item_collecturl, data) {

    private var collectionAction: (item: CollectUrlResponse, v: CollectView, position: Int) -> Unit =
        { _: CollectUrlResponse, _: CollectView, _: Int -> }

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun convert(holder: BaseViewHolder, item: CollectUrlResponse) {
        item.run {
            holder.setText(R.id.item_collecturl_name, name.toHtml())
            holder.setText(R.id.item_collecturl_link, link)
            holder.getView<CollectView>(R.id.item_collecturl_collect).isChecked = true
        }
        holder.getView<CollectView>(R.id.item_collecturl_collect)
            .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                override fun onClick(v: CollectView) {
                    collectionAction.invoke(item, v, holder.adapterPosition)
                }
            })

    }

    fun setCollectClick(inputCollectAction: (item: CollectUrlResponse, v: CollectView, position: Int) -> Unit) {
        this.collectionAction = inputCollectAction
    }
}