package pdm.isel.moviedatabaseapp

import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import pdm.isel.moviedatabaseapp.cache.DefaultCache
import pdm.isel.moviedatabaseapp.domain.providers.MovieProvider
import pdm.isel.moviedatabaseapp.domain.providers.MovieTMDBProvider
import pdm.isel.moviedatabaseapp.services.MovieListsJobService
import java.io.BufferedReader
import java.io.InputStreamReader

class MovieApplication : Application() {
    @Volatile lateinit var requestQueue: RequestQueue
    @Volatile lateinit var provider: MovieProvider
    @Volatile lateinit var imageLoader: ImageLoader
    lateinit var lang: String
    lateinit var apiKey: String

    override fun onCreate() {
        super.onCreate()
        apiKey = readAPIKEY()
        lang = getLanguage()
        provider = MovieTMDBProvider(apiKey, lang)
        requestQueue = Volley.newRequestQueue(this)
        imageLoader = ImageLoader(requestQueue, DefaultCache())
        val builder = JobInfo.Builder(
                MovieListsJobService.JOB_ID,
                ComponentName(this, MovieListsJobService::class.java)
        )
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder
                .setMinimumLatency(1000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build()
        )
    }

    private fun readAPIKEY(): String {
        val ip = resources.openRawResource(R.raw.api_key)
        val buffer = BufferedReader(InputStreamReader(ip))
        return buffer.readLine()
    }

    private fun getLanguage(): String = resources.getString(R.string.language)
}
