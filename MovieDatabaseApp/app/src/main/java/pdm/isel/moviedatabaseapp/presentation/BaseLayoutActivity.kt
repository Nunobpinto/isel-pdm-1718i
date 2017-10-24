package pdm.isel.moviedatabaseapp.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import pdm.isel.moviedatabaseapp.R

abstract class BaseLayoutActivity : AppCompatActivity() {

    protected open val toolbar: Int? = null

    protected open val menu: Int? = null

    protected abstract val layout : Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        toolbar?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        toolbar?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu?.let {
            menuInflater.inflate(it, menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }
}