package pdm.isel.moviedatabaseapp.model.dataDto

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty

class MovieListDto(
        val page: Int,
        @JsonProperty("total_result")
        val totalResult: Int,
        @JsonProperty("total_pages")
        val totalPages: Int,
        val results: Array<MovieDto>,
        val dates: MyDate?
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.createTypedArray(MovieDto),
                parcel.readParcelable(MyDate::class.java.classLoader))

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
        val id: Int,
        val title: String,
        val runtime: Int,
        @JsonProperty("release_date")
        val releaseDate: String,
        @JsonProperty("poster_path")
        val poster:String?,
        @JsonProperty("vote_average")
        val voteAverage: Float,
        val overview: String,
        val popularity: Float,
        val genres: Array<Genres>?
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readFloat(),
                parcel.readString(),
                parcel.readFloat(),
                parcel.createTypedArray(Genres))
        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(id)
                parcel.writeString(title)
                parcel.writeInt(runtime)
                parcel.writeString(releaseDate)
                parcel.writeString(poster)
                parcel.writeFloat(voteAverage)
                parcel.writeString(overview)
                parcel.writeFloat(popularity)
                parcel.writeTypedArray(genres, flags)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<MovieDto> {
                override fun createFromParcel(parcel: Parcel) = MovieDto(parcel)

                override fun newArray(size: Int): Array<MovieDto?> = arrayOfNulls(size)
        }
}

data class MyDate(val maximum: String, val minimum: String) : Parcelable {

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

data class Genres(val id: Int, val name: String) : Parcelable{
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(id)
                parcel.writeString(name)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<Genres> {
                override fun createFromParcel(parcel: Parcel): Genres = Genres(parcel)

                override fun newArray(size: Int): Array<Genres?> = arrayOfNulls(size)
        }

}

