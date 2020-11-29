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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.conference.Adapter.SectionAdapter
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.model.Section
import hu.bme.aut.filmdatabase.network.SectionNetworkManager
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_section.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeDashboard : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, SectionAdapter.OnFilmSelectedListener { // ktlint-disable max-line-length

    private lateinit var drawer: DrawerLayout
    private lateinit var adapter: SectionAdapter

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

        initRecyclerView()
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

    private fun initRecyclerView() {
        MainRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SectionAdapter(this)
        onFilmAdded("Bűnvadászok")
        MainRecyclerView.adapter = adapter
    }

    fun onFilmAdded(filname: String?) {
        SectionNetworkManager.getSections().enqueue(object : Callback<List<Section>> {
            /**
             * Invoked for a received HTTP response.
             *
             *
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call [Response.isSuccessful] to determine if the response indicates success.
             */
            override fun onResponse(call: Call<List<Section>>, response: Response<List<Section>>) {
                for (x in response.body()!!) {
                    adapter.addFilm(x)
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<List<Section>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onFilmSelected(film: Section?) {
        TODO("Not yet implemented")
    }
}
