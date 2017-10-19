package pdm.isel.moviedatabaseapp

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class MovieApplication : Application() {
    lateinit var requestQueue: RequestQueue
    override fun onCreate() {
        super.onCreate()
        requestQueue = Volley.newRequestQueue(this)

    }
}
