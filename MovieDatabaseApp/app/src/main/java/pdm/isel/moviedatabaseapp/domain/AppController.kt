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
            params.app.remoteRepository.getMostPopularMovies(
                    params.page,
                    params.app,
                    params.successCb,
                    { error: VolleyError -> params.errorCb(ProviderException()) }
            )
        }

        fun upcomingMovies(params: ParametersContainer) {
            try {
                val movieList = params.app.localRepository.getUpComingMovies(params.page)
                params.successCb(movieList)
            } catch (e: RepoException) {
                params.errorCb(e)
            }
        }

        fun nowPlaying(params: ParametersContainer) {
            try {
                val movieList = params.app.localRepository.getNowPlayingMovies(params.page)
                params.successCb(movieList)
            } catch (e: RepoException) {
                params.errorCb(e)
            }
        }
    }
}

data class ParametersContainer(
        val app: MovieApplication,
        val page: Int = 1,
        val successCb: (MovieListDto) -> Unit,
        val errorCb: (AppException) -> Unit
)