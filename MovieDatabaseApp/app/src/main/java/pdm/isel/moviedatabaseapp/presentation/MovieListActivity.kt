package pdm.isel.moviedatabaseapp.presentation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.fasterxml.jackson.databind.deser.Deserializers
import kotlinx.android.synthetic.main.activity_movie_list.*
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

        val movieAdapter: ArrayAdapter<MovieDto> = ArrayAdapter(this, android.R.layout.simple_list_item_1, movieList.results)

        movieListView.adapter = movieAdapter
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
