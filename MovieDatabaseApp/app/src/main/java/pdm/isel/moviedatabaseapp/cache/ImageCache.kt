package pdm.isel.moviedatabaseapp.cache

import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader
import java.io.File
import android.graphics.BitmapFactory
import java.io.FileOutputStream
import java.io.IOException


class ImageCache : ImageLoader.ImageCache {

    private val MAX_SIZE = 20
    private val cache: LruCache<String, Bitmap> = LruCache(20)
    @Volatile private var counter = 0


    override fun getBitmap(url: String?): Bitmap? {
        /*val file = File(url)
        val bitmap =  BitmapFactory.decodeFile(file.absolutePath)
        if(bitmap != null){
            return bitmap
        }*/
        return cache.get(url)
    }
    override fun putBitmap(url: String, bitmap: Bitmap) {
       // if(counter > MAX_SIZE){
            cache.put(url,bitmap)
        /*}
        else {
            val file = File(url.replace("?","").replace("$","").replace("#",""))
            val stream = FileOutputStream(file)
            //FileOutputStream(file).use{
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.flush()
                stream.close()
                ++counter
            }*/
        }

    }