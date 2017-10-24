package pdm.isel.moviedatabaseapp

import android.graphics.Bitmap
import com.android.volley.toolbox.ImageLoader

class DefaultCache : ImageLoader.ImageCache {

    override fun getBitmap(url: String?): Bitmap? = null
    override fun putBitmap(url: String?, bitmap: Bitmap?) = Unit
}