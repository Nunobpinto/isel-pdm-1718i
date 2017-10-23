package pdm.isel.moviedatabaseapp.model

import pdm.isel.moviedatabaseapp.model.dataDto.MyDate

data class Movie (val title:String,
                  val id : Int,
                  val rating:Int,
                  val date:String,
                  val posterUrl : String,
                  val overview : String,
                  val popularity : Float
                  )

data class MovieUpcoming(val movies:Array<Movie>,val date:MyDate)