package pdm.isel.moviedatabaseapp

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import pdm.isel.moviedatabaseapp.service.MovieProvider
import pdm.isel.moviedatabaseapp.service.MovieTMDBService

class MovieApplication : Application() {
    lateinit var requestQueue: RequestQueue
    lateinit var service : MovieProvider
    override fun onCreate() {
        super.onCreate()
        service = MovieTMDBService()
        requestQueue = Volley.newRequestQueue(this)

    }
}
