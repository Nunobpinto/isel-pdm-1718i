package pdm.isel.moviedatabaseapp.presentation

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_references.*
import pdm.isel.moviedatabaseapp.R

class ReferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_references)
        link.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/"))
            startActivity(intent)
        }
        back.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
