package pdm.isel.moviedatabaseapp.presentation

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_references.*
import pdm.isel.moviedatabaseapp.R

class ReferencesActivity : BaseLayoutActivity() {
    override val toolbar: Int? = R.id.my_toolbar
    override val menu: Int? = R.menu.menu
    override  val layout : Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        val toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar as Toolbar)

        link.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/"))
            startActivity(intent)
        }
        back.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var intent : Intent?=null
        when(item?.itemId){
            R.id.action_about -> intent = Intent(this,ReferencesActivity::class.java)
            R.id.action_home -> intent = Intent(this,HomeActivity::class.java)
        }
        startActivity(intent!!)
        return true
    }
}
