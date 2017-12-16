package pdm.isel.moviedatabaseapp.services

import android.app.job.JobParameters
import android.app.job.JobService
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.exceptions.AppException
import pdm.isel.moviedatabaseapp.exceptions.ProviderException
import pdm.isel.moviedatabaseapp.exceptions.RepoException

class NowPlayingJobService : JobService() {
    @Volatile private var uniqueId = 0

    companion object {
        const val MAX_PAGES_ALLOWED = 2
        const val JOB_ID = 2000
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        uniqueId = 0
        (application as MovieApplication).localRepository.deleteTable(
                "NOW_PLAYING",
                { error -> handleError(RepoException(error.message.toString())) }
        )
        fillNowPlayingTable(1)
        return true
    }

    private fun fillNowPlayingTable(startPage: Int) {
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
                                            { error -> handleError(error) }
                                    )
                                },
                                { error ->
                                    handleError(ProviderException(error.message.toString()))
                                }
                        )
                    }
                    if(++page <= MAX_PAGES_ALLOWED/*movies.totalPages != null && ++page <= movies.totalPages*/)
                        return@getNowPlayingMovies fillNowPlayingTable(page)
                },
                { error ->
                    handleError(ProviderException(error.message.toString()))
                }
        )
    }

    private fun handleError(error: AppException) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}