/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 11
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Dashboard

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.conference.Adapter.SectionAdapter
import hu.bme.aut.android.conference.Base.BaseActivity
import hu.bme.aut.android.conference.Dashboard.Lecture.ListLectureFragment
import hu.bme.aut.android.conference.Dashboard.Rooms.ListRoomFragment
import hu.bme.aut.android.conference.Dashboard.Section.ListSectionsFragment
import hu.bme.aut.android.conference.Login.LoginActivity
import hu.bme.aut.android.conference.Network.UserNetworkManager
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.model.User
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeDashboard : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var adapter: SectionAdapter

    companion object {
        var Auth_KEY: String? = null
        var USER: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_dashboard)

        Auth_KEY?.let {
            USER?.email?.let { it1 ->
                UserNetworkManager.getUser(it, it1).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            USER = response.body()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                    }
                })
            }
        }

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

        val headerView = navigationView.getHeaderView(0)
        headerView.userNameTextView.text = USER?.username ?: ""
        headerView.userEmailTextView.text = USER?.email ?: ""

        this.title = getString(R.string.sections)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmant_layout, ListSectionsFragment()).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_sections -> {supportFragmentManager.beginTransaction().replace(
                R.id.fragmant_layout,
                ListSectionsFragment()
            ).commit()
            this.title = getString(R.string.sections)}
            R.id.nav_lectures -> {supportFragmentManager.beginTransaction().replace(
                R.id.fragmant_layout,
                ListLectureFragment()
            ).commit()
            this.title = getString(R.string.lectures)
            }
            R.id.nav_rooms -> { supportFragmentManager.beginTransaction().replace(
                R.id.fragmant_layout,
                ListRoomFragment()
            ).commit()
            this.title = getString(R.string.rooms)}
            R.id.nav_sign_out -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.exit_confirmed))
                builder.setCancelable(true)
                builder.apply {
                    setNegativeButton(
                        getString(R.string.no)
                    ) { _, _ ->
                    }

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

        drawer.closeDrawer(GravityCompat.START)

        return true
    }
}
