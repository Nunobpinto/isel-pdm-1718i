package pdm.isel.moviedatabaseapp.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.widget.Toast
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R

class UpComingJobService : JobService() {

    companion object {
        val JOB_ID = 1234
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        (application as MovieApplication).remoteRepository.getUpComingMovies(1, this,
                { movies ->
                    (application as MovieApplication).localRepository.insertMovies(movies)
                },
                {
                    Toast.makeText(this, R.string.errorInfo, Toast.LENGTH_LONG).show()
                })
        return true
    }

}