package pdm.isel.moviedatabaseapp.services

import android.app.job.JobParameters
import android.app.job.JobService

class UpdateMovieListsJobService : JobService() {

    companion object {
        val JOB_ID = 1234
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}