package com.simplation.learnpaging

import androidx.recyclerview.widget.DiffUtil
import com.simplation.learnpaging.model.Movie

class MyDiffUtilCallback(
    private val mOldList: MutableList<Movie>,
    private val mNewList: MutableList<Movie>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldList.size
    }

    override fun getNewListSize(): Int {
        return mNewList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].id == mNewList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mOldList[oldItemPosition].title == mNewList[newItemPosition].title
    }
}