package com.example.test_lab_week_13

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.test_lab_week_13.databinding.ActivityMainBinding
import com.example.test_lab_week_13.model.Movie

class MainActivity : AppCompatActivity(), MovieAdapter.MovieClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        movieAdapter = MovieAdapter(this)
        binding.movieList.adapter = movieAdapter  // pastikan movie_list di XML → @+id/movie_list

        val movieRepository = (application as MovieApplication).movieRepository

        val movieViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MovieViewModel(movieRepository) as T
                }
            }
        )[MovieViewModel::class.java]

        binding.viewModel = movieViewModel
        binding.lifecycleOwner = this
    }

    override fun onMovieClick(movie: Movie) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_TITLE, movie.title ?: "")
            putExtra(DetailsActivity.EXTRA_RELEASE, movie.releaseDate ?: "")
            putExtra(DetailsActivity.EXTRA_OVERVIEW, movie.overview ?: "")
            putExtra(DetailsActivity.EXTRA_POSTER, movie.posterPath ?: "")
        }
        startActivity(intent)
    }
}
