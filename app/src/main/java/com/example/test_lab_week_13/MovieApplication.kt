package com.example.test_lab_week_13

import android.app.Application
import com.example.test_lab_week_13.database.MovieDatabase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MovieApplication : Application() {

    lateinit var movieRepository: MovieRepository
        private set

    override fun onCreate() {
        super.onCreate()

        // Retrofit untuk akses API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val movieService = retrofit.create(MovieService::class.java)

        // Buat instance Room Database (Singleton)
        val movieDatabase = MovieDatabase.getInstance(applicationContext)

        // Repository sekarang punya akses ke API + Database
        movieRepository = MovieRepository(movieService, movieDatabase)
    }
}
