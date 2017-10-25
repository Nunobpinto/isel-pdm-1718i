package pdm.isel.moviedatabaseapp

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import pdm.isel.moviedatabaseapp.service.MovieProvider
import pdm.isel.moviedatabaseapp.service.MovieTMDBService
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

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

    private fun readAPIKEY():String {
        val ip  = resources.openRawResource(R.raw.api_key)
        val buffer  = BufferedReader(InputStreamReader(ip))
        return buffer.readLine()
    }



}
