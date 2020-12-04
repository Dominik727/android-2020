package hu.bme.aut.android.conference

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.android.conference.Adapter.SectionAdapter
import hu.bme.aut.android.conference.Base.BaseActivity

class HomeDashboard : BaseActivity(), NavigationView.OnNavigationItemSelectedListener { // ktlint-disable max-line-length

    private lateinit var drawer: DrawerLayout
    private lateinit var adapter: SectionAdapter

    companion object {
        var Auth_KEY: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_dashboard)

        val toolbar: Toolbar = findViewById(R.id.toolbar3)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toogle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(toogle)
        toogle.syncState()

        supportFragmentManager.beginTransaction().replace(R.id.fragmant_layout, ListSections()).commit() // ktlint-disable max-line-length
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_surgeryes -> supportFragmentManager.beginTransaction().replace(
                R.id.fragmant_layout,
                ListSections()
            ).commit()
            R.id.nav_profile -> supportFragmentManager.beginTransaction().replace(
                R.id.fragmant_layout,
                ListSections()
            ).commit()
            R.id.nav_about -> supportFragmentManager.beginTransaction().replace(
                R.id.fragmant_layout,
                ListSections()
            ).commit()
            R.id.nav_sign_out -> startActivity(Intent(this, LoginActivity::class.java))
        }

        drawer.closeDrawer(GravityCompat.START)

        return true
    }
}