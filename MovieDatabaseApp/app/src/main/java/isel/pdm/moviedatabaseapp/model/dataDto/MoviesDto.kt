package isel.pdm.moviedatabaseapp.model.dataDto


data class SearchDto(val result: Array<MovieDto>,
                     val page:Int,
                     val total_result : Int,
                     val total_pages:Int)

data class MovieDto(val id:Int,
                    val title:String,
                    val release_date:String,
                    val poster_path:String,
                    val vote_average:Int)

