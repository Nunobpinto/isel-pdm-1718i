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
import pdm.isel.moviedatabaseapp.domain.repos.LocalMovieRepository
import pdm.isel.moviedatabaseapp.domain.repos.TMDBMovieRepository
import pdm.isel.moviedatabaseapp.domain.repos.base.ILocalRepository
import pdm.isel.moviedatabaseapp.domain.repos.base.ITMDBMovieRepository
import pdm.isel.moviedatabaseapp.services.NowPlayingJobService
import pdm.isel.moviedatabaseapp.services.UpComingJobService
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit


class MovieApplication : Application() {
    @Volatile lateinit var requestQueue: RequestQueue
    @Volatile lateinit var remoteRepository: ITMDBMovieRepository
    @Volatile lateinit var localRepository: ILocalRepository
    @Volatile lateinit var imageLoader: ImageLoader

    override fun onCreate() {
        super.onCreate()
        remoteRepository = TMDBMovieRepository(readAPIKEY(), getLanguage())
        localRepository = LocalMovieRepository(this)
        requestQueue = Volley.newRequestQueue(this)
        imageLoader = ImageLoader(requestQueue, DefaultCache())

        configureServices()
        //configureNotifications()
    }
/*
    private fun configureNotifications() {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = "followed_movies_channel"

//        val name = getString(R.string.channel_name)
//        val description = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(id, "Followed Movies", importance)

        mChannel.setDescription("Receive notifications reminding you when a movie is released")
        mChannel.enableLights(true)

        mChannel.setLightColor(Color.WHITE)
        mChannel.enableVibration(true)
        mNotificationManager.createNotificationChannel(mChannel)

    }
*/
    private fun configureServices() {
        val exhibitionBuilder = JobInfo.Builder(
                NowPlayingJobService.JOB_ID,
                ComponentName(this, NowPlayingJobService::class.java)
        )

        val upcomingBuilder = JobInfo.Builder(
                UpComingJobService.JOB_ID,
                ComponentName(this, UpComingJobService::class.java)
        )

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(exhibitionBuilder
                .setPeriodic(TimeUnit.MINUTES.toMillis(15))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build()
        )
        jobScheduler.schedule(upcomingBuilder
                .setPeriodic(TimeUnit.MINUTES.toMillis(15))
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
