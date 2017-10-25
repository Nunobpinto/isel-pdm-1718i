package pdm.isel.moviedatabaseapp

import android.graphics.Bitmap
import com.android.volley.toolbox.ImageLoader

class DefaultCache : ImageLoader.ImageCache {
    private val cache: HashMap<String, Bitmap> = HashMap(18)

    override fun getBitmap(url: String?): Bitmap? = cache[url]
    override fun putBitmap(url: String, bitmap: Bitmap)  {
        cache.put(url, bitmap)
    }
}