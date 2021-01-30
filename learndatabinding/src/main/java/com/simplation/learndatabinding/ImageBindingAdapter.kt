package com.simplation.learndatabinding

import android.graphics.Color
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

class ImageBindingAdapter {
    @BindingAdapter("image")
    fun setImage(imageView: ImageView, imageUrl: String) {
        if (TextUtils.isEmpty(imageUrl)) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(android.R.drawable.stat_notify_error)
                .into(imageView)
        } else {
            imageView.setBackgroundColor(Color.DKGRAY)
        }
    }


    //region 使用方法重载的方式支持项目中的图片
    @BindingAdapter("image")
    fun setImage(imageView: ImageView, imageResource: Int) {
        imageView.setImageResource(imageResource)
    }
    //endregion

    //region 多参数重载
    // requireAll 表示是否全部赋值
    @BindingAdapter("image", "defaultImageResource", requireAll = false)
    fun setImage(imageView: ImageView, imageUrl: String, imageResource: Int) {
        if (TextUtils.isEmpty(imageUrl)) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(android.R.drawable.stat_notify_error)
                .into(imageView)
        } else {
            imageView.setImageResource(imageResource)
        }
    }
    //endregion

}