package com.simplation.learndatabinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.simplation.learndatabinding.databinding.ItemLayoutContentBinding

class RecyclerViewAdapter(private val books: MutableList<Book>) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(val itemLayoutContentBinding: ItemLayoutContentBinding) :
        RecyclerView.ViewHolder(itemLayoutContentBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayoutContentBinding: ItemLayoutContentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_layout_content,
            parent,
            false
        )

        return MyViewHolder(itemLayoutContentBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val book = books[position]
        holder.itemLayoutContentBinding.book = book
    }

    override fun getItemCount(): Int {
        return books.size
    }
}