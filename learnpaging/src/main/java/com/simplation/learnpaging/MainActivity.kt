package com.simplation.learnpaging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.simplation.learnpaging.databinding.ActivityMainBinding
import com.simplation.learnpaging.paging.MovieAdapter
import com.simplation.learnpaging.paging.MovieViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val movieAdapter = MovieAdapter()
        ViewModelProvider(this)[MovieViewModel::class.java].apply {
            moviePageList.observe(this@MainActivity) {
                movieAdapter.submitList(it)
            }
        }

        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(
                true
            )
            adapter = movieAdapter
        }
    }
}