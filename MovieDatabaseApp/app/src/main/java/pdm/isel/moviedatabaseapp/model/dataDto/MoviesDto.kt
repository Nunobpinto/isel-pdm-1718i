package pdm.isel.moviedatabaseapp.model.dataDto

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty


class MovieListDto(
        val page:Int,
        @JsonProperty("total_result")
        val totalResult : Int,
        @JsonProperty("total_pages")
        val totalPages:Int,
        val results: Array<MovieDto>,
        val dates : MyDate = MyDate("", "")
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.createTypedArray(MovieDto),
                parcel.readParcelable(MyDate::class.java.classLoader)) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(page)
                parcel.writeInt(totalResult)
                parcel.writeInt(totalPages)
                parcel.writeTypedArray(results, flags)
                parcel.writeParcelable(dates, flags)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<MovieListDto> {
                override fun createFromParcel(parcel: Parcel) = MovieListDto(parcel)

                override fun newArray(size: Int): Array<MovieListDto?> = arrayOfNulls(size)
        }
}

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
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readFloat()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(id)
                parcel.writeString(title)
                parcel.writeString(releaseDate)
                parcel.writeString(poster)
                parcel.writeInt(voteAverage)
                parcel.writeString(overview)
                parcel.writeFloat(popularity)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<MovieDto> {
                override fun createFromParcel(parcel: Parcel) = MovieDto(parcel)

                override fun newArray(size: Int): Array<MovieDto?> = arrayOfNulls(size)
        }
}

data class MyDate(val maximum : String, val minimum : String) : Parcelable {

        constructor(parcel: Parcel) : this(parcel.readString(),parcel.readString())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeStringArray(Array<String>(2) {this.maximum; this.minimum})
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<MyDate> {
                override fun createFromParcel(parcel: Parcel) = MyDate(parcel)
                override fun newArray(size: Int): Array<MyDate?> = arrayOfNulls(size)
        }
}

