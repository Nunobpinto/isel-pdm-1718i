package pdm.isel.moviedatabaseapp.presentation


import android.graphics.Color
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_start.*
import pdm.isel.moviedatabaseapp.R
import android.R.string.cancel
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler


class StartActivity : BaseLayoutActivity() {

    override  val layout : Int = R.layout.activity_start

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressBar.indeterminateDrawable.setColorFilter(
                Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN)
        val handler = Handler()
        handler.postDelayed( {
            startActivity(Intent(this,HomeActivity::class.java))
        }, 2000)

    }
}
