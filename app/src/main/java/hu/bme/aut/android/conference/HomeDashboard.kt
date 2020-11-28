package hu.bme.aut.android.conference

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat.START
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.conference.Base.BaseActivity
import kotlinx.android.synthetic.main.app_bar_main.*

class HomeDashboard : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_dashboard)

        val toolbar: Toolbar = findViewById(R.id.toolbarForLogged)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        var navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        var toogle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(toogle)
        toogle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Valóban ki akar lépni?")
                builder.setCancelable(true)
                builder.apply {
                    setNegativeButton(
                        "Nem",
                        DialogInterface.OnClickListener { dialog, id ->
                        }
                    )

                    setPositiveButton(
                        getString(R.string.yes)
                    ) { _, _ ->
                        FirebaseAuth.getInstance().signOut()
                        startActivity(Intent(this@HomeDashboard, LoginActivity::class.java))
                        finish()
                    }
                }
                builder.show()
            }
        }

        drawer.closeDrawer(START)

        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(START)) {
            drawer.closeDrawer(START)
        } else {
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        }
    }
}
