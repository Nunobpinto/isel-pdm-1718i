package pdm.isel.moviedatabaseapp.presentation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_movie_details.*
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.model.dataDto.MovieDto

class MovieDetailsActivity : BaseLayoutActivity() {
    override val toolbar: Int? = R.id.my_toolbar
    override val menu: Int? = R.menu.menu
    override  val layout : Int = R.layout.activity_movie_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val movie : MovieDto = intent.getParcelableExtra("movie")

        title_value.text = movie.title
        overview_value.text = movie.overview
        rating_value.text = movie.voteAverage.toString()
        posterImageView.setImageUrl(urlBuilder(movie.poster!!), (application as MovieApplication).imageLoader)
    }


    private fun urlBuilder(path: String) = "http://image.tmdb.org/t/p/w185/$path?$"

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
