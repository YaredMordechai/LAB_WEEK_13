package com.example.test_lab_week_13

import com.example.test_lab_week_13.database.MovieDao
import com.example.test_lab_week_13.database.MovieDatabase
import com.example.test_lab_week_13.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(
    private val movieService: MovieService,
    private val movieDatabase: MovieDatabase
) {

    private val apiKey = "4ebb92bb9df6dd74c5c162ab29810a81"

    // fetch movies using Flow, tapi sekarang lewat Room dulu
    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            val movieDao: MovieDao = movieDatabase.movieDao()
            val savedMovies = movieDao.getMovies()

            if (savedMovies.isEmpty()) {
                // kalau DB kosong → ambil dari API
                val moviesFromApi = movieService.getPopularMovies(apiKey).results
                // simpan ke Room
                movieDao.addMovies(moviesFromApi)
                // kirim hasil dari API
                emit(moviesFromApi)
            } else {
                // kalau sudah ada di DB → pakai data lokal
                emit(savedMovies)
            }
        }.flowOn(Dispatchers.IO)
    }
}
