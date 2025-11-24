package com.example.test_lab_week_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Calendar

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    init {
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.fetchMovies()
                .catch { throwable ->
                    _error.value = "An exception occurred: ${throwable.message}"
                }
                .collect { movies ->

                    val currentYear = Calendar.getInstance()
                        .get(Calendar.YEAR)
                        .toString()

                    val filteredAndSortedMovies = movies
                        .filter { it.releaseDate?.startsWith(currentYear) == true }
                        .sortedByDescending { it.popularity }

                    _popularMovies.value = filteredAndSortedMovies
                }
        }
    }
}
