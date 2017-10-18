package pdm.isel.moviedatabaseapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        search.setOnClickListener({})

        nowPlaying.setOnClickListener({})

        upoming.setOnClickListener({})

        mostPopular.setOnClickListener({})

        about_button.setOnClickListener{
            val intent = Intent(this,AboutActivity::class.java)
            startActivity(intent)
        }

        reference_button.setOnClickListener{
            val intent = Intent(this,ReferencesActivity::class.java)
            startActivity(intent)
        }
    }
}
