package pdm.isel.moviedatabaseapp.presentation

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_movie_list.*
import pdm.isel.moviedatabaseapp.MovieAdapter
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.model.dataDto.MovieDto
import pdm.isel.moviedatabaseapp.model.dataDto.MovieListDto

class MovieListActivity : BaseLayoutActivity() {
    override val toolbar: Int? = R.id.my_toolbar
    override val menu: Int? = R.menu.menu
    override  val layout : Int = R.layout.activity_movie_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val intent = intent
        val movieList : MovieListDto = intent.getParcelableExtra("results")
        val toolbarText: String = intent.getStringExtra("toolbarText")
        this.my_toolbar.title = toolbarText


        val movieAdapter = MovieAdapter(this, R.layout.movie_list_entry_layout, movieList.results, (application as MovieApplication))

        movieListView.adapter = movieAdapter

        movieListView.setOnItemClickListener { parent, view, position, id ->
            run {
                var movie : MovieDto = movieAdapter.getItem(position)
                (application as MovieApplication).let {
                    it.service.getMovieDetails(
                            movie.id,
                            application,
                            {movie->startActivity(createIntent(Intent(this, MovieDetailsActivity::class.java), movie, "Details of " + movie.title))},
                            {VolleyError()})
                }

            }
        }
    }

    private fun createIntent(intent: Intent, movie: MovieDto, s: String): Intent? {
        intent.putExtra("toolbarText", s)
        intent.putExtra("movie", movie)
        return intent
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
