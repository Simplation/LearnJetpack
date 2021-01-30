package com.simplation.learndatabinding

import android.graphics.Color
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

class RecyclerViewBindingAdapter {

    companion object {
        @JvmStatic  // 解决 Required DataBindingComponent is null in class ItemLayoutContentBindingImpl. 错误
        @BindingAdapter("itemImage")
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
    }



}