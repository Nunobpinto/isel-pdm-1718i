package pdm.isel.moviedatabaseapp.services

import android.app.job.JobParameters
import android.app.job.JobService
import pdm.isel.moviedatabaseapp.MovieApplication

class NowPlayingJobService : JobService() {
    @Volatile private var onGoingRequests: MutableList<String> = mutableListOf()
    @Volatile private var uniqueId = 0

    companion object {
        const val MAX_PAGES_ALLOWED = 1
        const val JOB_ID = 2000
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        onGoingRequests.forEach {
            (application as MovieApplication).requestQueue.cancelAll(it)
        }
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        uniqueId = 0
        (application as MovieApplication).localRepository.deleteTable(
                "NOW_PLAYING",
                { jobFinished(params, true) }
        )
        fillNowPlayingTable(1, params)
        return true
    }

    private fun fillNowPlayingTable(startPage: Int, params: JobParameters?) {
        var page = startPage
        (application as MovieApplication).remoteRepository.getNowPlayingMovies(
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
                                            "NOW_PLAYING",
                                            { jobFinished(params, true) }
                                    )
                                },
                                { jobFinished(params, true)}
                        )
                    }
                    if(++page <= MAX_PAGES_ALLOWED/*movies.totalPages != null && ++page <= movies.totalPages*/)
                        return@getNowPlayingMovies fillNowPlayingTable(page, params)
                },
                { jobFinished(params, true) }
        )
    }
}