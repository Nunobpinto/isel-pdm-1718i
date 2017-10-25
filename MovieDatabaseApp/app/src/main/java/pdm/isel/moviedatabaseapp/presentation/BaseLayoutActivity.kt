package pdm.isel.moviedatabaseapp.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.Toolbar
import android.view.Menu

abstract class BaseLayoutActivity : AppCompatActivity() {

    protected open val toolbar: Int? = null

    protected open val menu: Int? = null

    protected abstract val layout : Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        toolbar?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
            supportActionBar?.title = null
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
