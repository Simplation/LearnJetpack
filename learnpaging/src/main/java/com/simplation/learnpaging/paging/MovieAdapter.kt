package com.simplation.learnpaging.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.simplation.learnpaging.databinding.MovieItemBinding
import com.simplation.learnpaging.model.Movie
import com.squareup.picasso.Picasso

class MovieAdapter :
    PagedListAdapter<Movie, MovieAdapter.MovieViewHolder>(DIFFCALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        // 布局文件为：movie_item，生成的绑定类为 MovieItemBinding
        val itemBinding =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            // 实现绑定操作
            holder.bind(movie)
        }
    }

    object DIFFCALLBACK : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    class MovieViewHolder(private val itemBinding: MovieItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(movie: Movie) {
            itemBinding.tvTitle.text = movie.title
            itemBinding.tvYear.text = movie.year
            Picasso.get().load(movie.images.small)
                .placeholder(android.R.drawable.btn_default_small)
                .error(android.R.drawable.stat_notify_error)
                .into(itemBinding.ivImage)
        }
    }
}