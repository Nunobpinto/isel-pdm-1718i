package pdm.isel.moviedatabaseapp

import android.app.Application
import android.app.ApplicationErrorReport
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.os.BatteryManager
import android.preference.PreferenceManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import pdm.isel.moviedatabaseapp.cache.ImageCache
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
    @Volatile lateinit var preferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        remoteRepository = TMDBMovieRepository(readAPIKEY(), getLanguage())
        localRepository = LocalMovieRepository(this)
        requestQueue = Volley.newRequestQueue(this)
        imageLoader = ImageLoader(requestQueue, ImageCache())
        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        val exhibitionBuilder = JobInfo.Builder(
                NowPlayingJobService.JOB_ID,
                ComponentName(this, NowPlayingJobService::class.java)
        )
        val upcomingBuilder = JobInfo.Builder(
                UpComingJobService.JOB_ID,
                ComponentName(this, UpComingJobService::class.java)
        )

        configureServices(exhibitionBuilder, upcomingBuilder)

        preferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            configureServices(sharedPreferences, exhibitionBuilder, upcomingBuilder)
        }


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
    private fun batteryLevel(): Int {
        var bm : BatteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    private fun configureServices(exhibitionBuilder: JobInfo.Builder, upcomingBuilder: JobInfo.Builder) {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(exhibitionBuilder
                .setPeriodic(TimeUnit.DAYS.toMillis(7))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresBatteryNotLow(true)
                .build()
        )
        jobScheduler.schedule(upcomingBuilder
                .setPeriodic(TimeUnit.DAYS.toMillis(7))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build()
        )
    }

    private fun configureServices(sharedPreferences: SharedPreferences, exhibitionBuilder: JobInfo.Builder, upcomingBuilder: JobInfo.Builder) {
        val network =
                when (sharedPreferences.getString("networkType", null)) {
                    "Only Wi-fi" -> JobInfo.NETWORK_TYPE_UNMETERED
                    "Only Mobile Network" -> JobInfo.NETWORK_TYPE_METERED
                    else -> JobInfo.NETWORK_TYPE_ANY
                }

        val periodic =
                when (sharedPreferences.getString("updateFrequency", null)) {
                    "Daily" -> TimeUnit.DAYS.toMillis(1)
                    "Weekly" -> TimeUnit.DAYS.toMillis(7)
                    else -> TimeUnit.DAYS.toMillis(30)
                }

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        if (batteryLevel() >= sharedPreferences.getString("battery", null).toInt()){
            jobScheduler.schedule(exhibitionBuilder
                    .setPeriodic(periodic)
                    .setRequiredNetworkType(network)
                    .build()
            )
            jobScheduler.schedule(upcomingBuilder
                    .setPeriodic(periodic)
                    .setRequiredNetworkType(network)
                    .build()
            )
        }
    }

    private fun readAPIKEY(): String {
        val ip = resources.openRawResource(R.raw.api_key)
        val buffer = BufferedReader(InputStreamReader(ip))
        return buffer.readLine()
    }

    private fun getLanguage(): String = resources.getString(R.string.language)
}
