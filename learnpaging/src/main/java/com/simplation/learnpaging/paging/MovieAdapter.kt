package com.simplation.learnpaging.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.simplation.learnpaging.R
import com.simplation.learnpaging.model.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(diffCallback: DiffUtil.ItemCallback<Movie>) :
    PagedListAdapter<Movie, MovieAdapter.MovieViewHolder>(diffCallback) {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvYear: TextView = itemView.findViewById(R.id.tvYear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            Picasso.get().load(movie.images.small)
                .placeholder(android.R.drawable.btn_default_small)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.ivImage)

            holder.tvTitle.text = movie.title
            holder.tvYear.text = movie.year
        } else {
            holder.ivImage.setImageResource(android.R.drawable.btn_default_small)
            holder.tvTitle.text = ""
            holder.tvYear.text = ""
        }
    }
}