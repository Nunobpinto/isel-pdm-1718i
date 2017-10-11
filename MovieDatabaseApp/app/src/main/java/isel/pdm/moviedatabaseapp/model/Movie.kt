package isel.pdm.moviedatabaseapp.model

data class Movie (val title:String,
                  val id : Int,
                  val rating:Int,
                  val date:String,
                  val posterUrl : String,
                  val overview : String
                  )
