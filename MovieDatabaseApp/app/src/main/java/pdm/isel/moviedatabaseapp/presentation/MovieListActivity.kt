package pdm.isel.moviedatabaseapp.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_movie_list.*
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.model.dataDto.MovieDto
import pdm.isel.moviedatabaseapp.model.dataDto.MovieListDto

class MovieListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        val intent = intent
        val movieList : MovieListDto = intent.getParcelableExtra("results")

        val movieAdapter: ArrayAdapter<MovieDto> = ArrayAdapter(this, android.R.layout.simple_list_item_1, movieList.results)

        movieListView.adapter = movieAdapter
    }
}
