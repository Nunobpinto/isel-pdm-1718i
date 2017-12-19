package pdm.isel.moviedatabaseapp.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.preference.PreferenceManager
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.domain.model.FollowedMovie
import pdm.isel.moviedatabaseapp.ui.activity.MovieDetailsActivity
import java.util.*

class UpComingJobService : JobService() {
    @Volatile private var onGoingRequests: MutableList<String> = mutableListOf()
    private val handler = Handler()

    companion object {
        const val MAX_PAGES_ALLOWED = 5
        const val MAX_DELAY_MILIS: Long = 8000
        const val JOB_ID = 1234
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        onGoingRequests.forEach {
            (application as MovieApplication).requestQueue.cancelAll(it)
        }
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        checkFollowedMovies(params)
        updateUpcomingTable(params)
        return true
    }

    private fun checkFollowedMovies(params: JobParameters?) {
        val currDate = parseCurrentDate()
        (application as MovieApplication).localRepository.getFollowedMovies(
                { movies ->
                    movies.forEach {
                        if(it.releaseDate >= currDate && PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications", false))
                            sendNotification(it)
                    }
                },
                { jobFinished(params, true) }
        )
    }

    private fun updateUpcomingTable(params: JobParameters?) {
        (application as MovieApplication).localRepository.deleteTable(
                "UPCOMING",
                { jobFinished(params, true) }
        )
        fillUpcomingTable(1, params)
    }

    private fun fillUpcomingTable(startPage: Int, params: JobParameters?) {
        var page = startPage
        (application as MovieApplication).remoteRepository.getUpComingMovies(
                startPage,
                (application as MovieApplication),
                { movies, tag ->
                    onGoingRequests.add(tag)
                    movies.results.forEach {
                        (application as MovieApplication).remoteRepository.getMovieDetails(
                                it.id,
                                (application as MovieApplication),
                                { movie, tag ->
                                    onGoingRequests.add(tag)
                                    (application as MovieApplication).localRepository.insertMovie(
                                            movie,
                                            "UPCOMING",
                                            { }
                                    )
                                },
                                { jobFinished(params, true) }
                        )
                    }
                    handler.postDelayed({
                        if(++page <= MAX_PAGES_ALLOWED)
                            return@postDelayed fillUpcomingTable(page, params)
                        else
                            jobFinished(params, false)
                    }, MAX_DELAY_MILIS)
                },
                { jobFinished(params, true) }
        )
    }

    private fun sendNotification(movie: FollowedMovie) {
        val intent = Intent(applicationContext, MovieDetailsActivity::class.java)
        intent.putExtra("id", movie.id)
        val pendingIntent = TaskStackBuilder.create(applicationContext)
                .addParentStack(MovieDetailsActivity::class.java)
                .addNextIntent(intent)
                .getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT)


        val mBuilder = Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_icon)
                .setContentTitle(movie.title + " is opening this week!")
                .setContentText("Hurray!")
                .setContentIntent(pendingIntent)
                .setChannelId("followed_movies_channel")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(11, mBuilder.build())
    }

    private fun parseCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "$year-$month-$day"
    }
}