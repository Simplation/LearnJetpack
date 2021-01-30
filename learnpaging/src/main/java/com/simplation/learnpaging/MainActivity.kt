package com.simplation.learnpaging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.simplation.learnpaging.databinding.ActivityMainBinding
import com.simplation.learnpaging.model.Movie
import com.simplation.learnpaging.paging.MovieAdapter
import com.simplation.learnpaging.paging.MovieViewModel

class MainActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerView.setHasFixedSize(true)

        val movieAdapter = MovieAdapter(DiffUtil.ItemCallback<Movie>())   // DiffUtil.ItemCallback
        val movieViewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        movieViewModel.moviePageList.observe(this, {
            fun onChanged(movies: PagedList<Movie>) {
                movieAdapter.submitList(it)
            }
        })
        viewBinding.recyclerView.adapter = movieAdapter
    }
}