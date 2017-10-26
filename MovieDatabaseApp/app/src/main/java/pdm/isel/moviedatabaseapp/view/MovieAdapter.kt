package pdm.isel.moviedatabaseapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.android.volley.toolbox.NetworkImageView
import pdm.isel.moviedatabaseapp.MovieApplication
import pdm.isel.moviedatabaseapp.R
import pdm.isel.moviedatabaseapp.model.MovieDto

class MovieAdapter(private val ctx: Context, private val resource: Int, private val items: Array<MovieDto>, private val app: MovieApplication)
    : ArrayAdapter<MovieDto>(ctx, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder
        val rowView: View
        if ( convertView == null )  {
            val inflater: LayoutInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(R.layout.movie_list_entry_layout, parent, false)
            holder = fillHolder(rowView)
        }
        else {
            rowView = convertView
            holder = convertView.tag as ViewHolder
        }

        val item = items[position]
        //TODO: limitar o numero de algarismos do voteAverage lança excepção
        holder.extraInfo.text = String.format("(%f/10) %s ", item.voteAverage, item.releaseDate)
        holder.movieTitle.text = item.title
        holder.id = item.id
        //TODO: quando não há foto, é carregada uma aleatoria das que ja existem na lista
        if (item.poster != null )
            holder.imgView.setImageUrl(urlBuilder(item.poster), app.imageLoader)

        return rowView
    }

    private fun urlBuilder(path: String) = "http://image.tmdb.org/t/p/w185/$path?$"

    private fun fillHolder(convertView: View): ViewHolder {
        val holder = ViewHolder(
                convertView.findViewById(R.id.posterImageView) as NetworkImageView,
                convertView.findViewById(R.id.movieTitle) as TextView,
                convertView.findViewById(R.id.extraInfo) as TextView,
                0
        )
        holder.imgView.setDefaultImageResId(R.drawable.default_poster)
        convertView.tag = holder
        return holder
    }
}

class ViewHolder(
        val imgView: NetworkImageView,
        val movieTitle: TextView,
        val extraInfo: TextView,
        var id : Int
)

