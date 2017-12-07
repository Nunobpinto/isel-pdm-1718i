package pdm.isel.moviedatabaseapp.services

import android.app.job.JobParameters
import android.app.job.JobService
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.domain.content.MovieContentProvider

class MovieListsJobService : JobService() {

    companion object {
        val JOB_ID = 1234
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartJob(params: JobParameters?): Boolean {

        (application as MovieApplication).movieProvider.getNowPlayingMovies(1, this,
                {
                    movies ->
                    (application as MovieApplication).movieContentProvider.
                            insert(MovieContentProvider.EXHIBITION_URI, movies)
                },
                {
                    { volleyError -> generateErrorWarning(volleyError) }
                })


        (application as MovieApplication).movieProvider.getUpComingMovies(1, this,
                {
                    movies ->
                    (application as MovieApplication).movieContentProvider.
                            insert(MovieContentProvider.UPCOMING_URI, movies)
                },
                {
                    { volleyError -> generateErrorWarning(volleyError) }
                })


        //TODO: fazer pedido assincrono ao volley
        //TODO: guardar dados num repositorio assincronamente}
        TODO("not implemented")
    }
}