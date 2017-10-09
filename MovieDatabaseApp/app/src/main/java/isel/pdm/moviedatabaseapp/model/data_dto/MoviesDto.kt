package isel.pdm.moviedatabaseapp.model.data_dto


data class SearchDto(val result: Array<MovieDto>)

data class MovieDto(val id:Int,
                    val title:String,
                    val release_date:String,
                    val vote_average:Int)

data class MovieDetailsDto(val title:String,
                          val vote_average:Int,
                          val release_date:String,
                          val poster_path : String,
                          val overview : String)
