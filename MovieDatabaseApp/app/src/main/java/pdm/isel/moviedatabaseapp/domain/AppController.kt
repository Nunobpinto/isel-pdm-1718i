package pdm.isel.moviedatabaseapp.domain

import com.android.volley.VolleyError
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.domain.model.MovieListDto
import pdm.isel.moviedatabaseapp.exceptions.*

class AppController {

    companion object {
        fun actionHandler(action: String, params: ParametersContainer) = when (action) {
            "mostPopularMovies" -> mostPopularMovies(params)
            "upcomingMovies" -> upcomingMovies(params)
            "nowPlaying" -> nowPlaying(params)
            "movieDetails" -> movieDetails(params)
            else -> throw UnsupportedOperationException("Action not supported!")
        }

        private fun movieDetails(params: ParametersContainer) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        fun mostPopularMovies(params: ParametersContainer) {
            //TODO: get data from remote repo!!
            params.app.remoteRepository.getMostPopularMovies(
                    params.page,
                    params.app,
                    params.successCb,
                    { error: VolleyError -> params.errorCb(ProviderException()) }
            )
        }

        fun upcomingMovies(params: ParametersContainer) {
            //TODO: get data from local repo!!
            params.app.remoteRepository.getUpComingMovies(
                    params.page,
                    params.app,
                    params.successCb,
                    { error: VolleyError -> params.errorCb(ProviderException()) }
            )
        }

        fun nowPlaying(params: ParametersContainer) {
            //TODO: get data from local repo!!
            params.app.remoteRepository.getNowPlayingMovies(
                    params.page,
                    params.app,
                    params.successCb,
                    { error: VolleyError -> params.errorCb(ProviderException()) }
            )
        }
    }
}

data class ParametersContainer(
        val app: MovieApplication,
        val page: Int = 1,
        val successCb: (MovieListDto) -> Unit,
        val errorCb: (AppException) -> Unit
)