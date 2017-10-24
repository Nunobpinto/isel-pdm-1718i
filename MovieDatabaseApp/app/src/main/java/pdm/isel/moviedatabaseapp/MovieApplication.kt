package pdm.isel.moviedatabaseapp

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import pdm.isel.moviedatabaseapp.service.MovieProvider
import pdm.isel.moviedatabaseapp.service.MovieTMDBService

class MovieApplication : Application() {
    @Volatile lateinit var requestQueue: RequestQueue
    @Volatile  var service : MovieProvider = MovieTMDBService()
    @Volatile lateinit var imageLoader : ImageLoader
    override fun onCreate() {
        super.onCreate()
        service = MovieTMDBService()
        requestQueue = Volley.newRequestQueue(this)
        imageLoader = ImageLoader(requestQueue,DefaultCache())
    }
}
