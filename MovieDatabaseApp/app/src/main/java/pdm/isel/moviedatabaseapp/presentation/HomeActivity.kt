package pdm.isel.moviedatabaseapp.presentation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_home.*
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.model.dataDto.MovieDto
import pdm.isel.moviedatabaseapp.model.dataDto.MovieListDto

class HomeActivity : BaseLayoutActivity() {

    override val toolbar: Int? = R.id.my_toolbar
    override val menu: Int? = R.menu.menu
    override  val layout : Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchButton.setOnClickListener({
            val query: String = inputEditText.text.toString()
            (application as MovieApplication).let {
                it.service.getMoviesByName(
                        query,
                        application,
                        {movie -> startActivity(createIntent(MovieDetailsActivity::class.java, movie))})
            }
        })

        nowPlayingButton.setOnClickListener({
            (application as MovieApplication).let {
                it.service.getNowPlayingMovies(
                        application,
                        {movies->startActivity(createIntentList(MovieListActivity::class.java, movies))})
            }
        })

        upcomingMoviesButton.setOnClickListener({
            (application as MovieApplication).let {
                it.service.getUpComingMovies(
                        application,
                        {movies->startActivity(createIntentList(MovieListActivity::class.java,movies))})
            }
        })

        mostPopularMoviesButton.setOnClickListener({
            (application as MovieApplication).let {
                it.service.getMostPopularMovies(
                        application,
                        {movies->startActivity(createIntentList(MovieListActivity::class.java,movies))})
            }
        })
    }

    private fun createIntentList(destClass: Class<MovieListActivity>, dto: MovieListDto): Intent? {
        val i = Intent(this, destClass)
        i.putExtra("results", dto)
        return i
    }

    private fun createIntent(destClass: Class<MovieDetailsActivity>,dto:MovieDto) : Intent{
        val i = Intent(this, destClass)
        i.putExtra("movie", dto)
        return i
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var intent : Intent?=null
        when(item?.itemId){
            R.id.action_about -> intent = Intent(this,ReferencesActivity::class.java)
            R.id.action_home -> intent = Intent(this,HomeActivity::class.java)
        }
        startActivity(intent!!)
        return true
    }
}
