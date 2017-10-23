package pdm.isel.moviedatabaseapp.presentation

import android.app.ListActivity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.model.dataDto.MovieListDto

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        searchButton.setOnClickListener({
            onSearchRequested()
        })

        nowPlayingButton.setOnClickListener({
            (application as MovieApplication).let {
                it.service.getNowPlayingMovies(
                        application,
                        {movies->startActivity(createIntent(ListActivity::class.java, movies))})
            }
        })

        upcomingMoviesButton.setOnClickListener({
            (application as MovieApplication).let {
                it.service.getUpComingMovies(
                        application,
                        {movies->startActivity(createIntent(ListActivity::class.java,movies))})
            }
        })

        mostPopularMoviesButton.setOnClickListener({
            (application as MovieApplication).let {
                it.service.getMostPopularMovies(
                        application,
                        {movies->startActivity(createIntent(ListActivity::class.java,movies))})
            }
        })

        aboutButton.setOnClickListener{
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        referencesButton.setOnClickListener{
            val intent = Intent(this, ReferencesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createIntent(destClass: Class<ListActivity>, dto: MovieListDto): Intent? {
        val i = Intent(this, destClass)
        val bundle = Bundle()
        bundle.putParcelableArray("data", dto.results)
        i.putExtra("data", bundle)
        return i
    }
}
