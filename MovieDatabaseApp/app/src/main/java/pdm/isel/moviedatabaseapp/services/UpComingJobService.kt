package pdm.isel.moviedatabaseapp.services

import android.app.Notification
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.domain.model.FollowedMovie
import pdm.isel.moviedatabaseapp.exceptions.AppException
import pdm.isel.moviedatabaseapp.exceptions.RepoException
import java.util.*

class UpComingJobService : JobService() {
    @Volatile private var onGoingRequests: MutableList<String> = mutableListOf()
    @Volatile private var uniqueId = 0

    companion object {
        const val MAX_PAGES_ALLOWED = 1
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
                        if(it.releaseDate.equals(currDate))
                            sendNotification(it)
                    }
                },
                { jobFinished(params, true) }
        )
    }

    private fun updateUpcomingTable(params: JobParameters?) {
        uniqueId = 0
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
                { movies ->
                    movies.results.forEach {
                        (application as MovieApplication).remoteRepository.getMovieDetails(
                                it.id,
                                (application as MovieApplication),
                                { movie ->
                                    (application as MovieApplication).localRepository.insertMovie(
                                            uniqueId++,
                                            movie,
                                            "UPCOMING",
                                            { jobFinished(params, true) }
                                    )
                                },
                                { jobFinished(params, true) }
                        )
                    }
                    if(++page <= MAX_PAGES_ALLOWED/*movies.totalPages != null && ++page <= movies.totalPages*/)
                        return@getUpComingMovies fillUpcomingTable(page, params)
                    else
                        jobFinished(params, false)
                },
                { jobFinished(params, true) }
        )
    }

    private fun sendNotification(movie: FollowedMovie) {
        val mBuilder = Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_icon)
                .setContentTitle(movie.title + " is opening today!")
                .setContentText("Hurray!")
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