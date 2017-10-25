package pdm.isel.moviedatabaseapp

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import pdm.isel.moviedatabaseapp.service.MovieProvider
import pdm.isel.moviedatabaseapp.service.MovieTMDBService

class MovieApplication : Application() {
    @Volatile lateinit var requestQueue: RequestQueue
    @Volatile  lateinit var service : MovieProvider
    @Volatile lateinit var imageLoader : ImageLoader
    lateinit var apiKey : String
    override fun onCreate() {
        super.onCreate()
        apiKey = readAPIKEY()
        service = MovieTMDBService(apiKey)
        requestQueue = Volley.newRequestQueue(this)
        imageLoader = ImageLoader(requestQueue,DefaultCache())
    }

    private fun readAPIKEY():String =  resources.getString(R.raw.api_key)



}
