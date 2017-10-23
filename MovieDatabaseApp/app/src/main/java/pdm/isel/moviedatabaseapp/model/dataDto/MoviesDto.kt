package pdm.isel.moviedatabaseapp.model.dataDto

import com.fasterxml.jackson.annotation.JsonProperty


data class SearchDto(
        val results: Array<MovieDto>,
        val page:Int,
        @JsonProperty("total_result") val totalResult : Int,
        @JsonProperty("total_pages") val totalPages:Int
)

data class MovieDto(
        val id:Int,
        val title:String,
        @JsonProperty("release_date")
        val releaseDate:String,
        @JsonProperty("poster_path")
        val poster:String,
        @JsonProperty("vote_average")
        val voteAverage:Int,
        val overview : String,
        val popularity : Float
)

data class UpComingDto(
        val result: Array<MovieDto>,
        val dates : MyDate
)

data class MyDate(val maximum : String, val minimum : String)
