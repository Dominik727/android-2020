/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 12
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Dashboard.Rooms

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import hu.bme.aut.android.conference.Adapter.RoomAdapter
import hu.bme.aut.android.conference.Dashboard.HomeDashboard
import hu.bme.aut.android.conference.Network.RoomNetWorkManager
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.enum.userType
import hu.bme.aut.android.conference.model.Room
import kotlinx.android.synthetic.main.fragment_list_sections.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListRoomFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, RoomAdapter.OnRoomSelectedListener, RoomDetailActivity.roomnAddedListener {

    private lateinit var adapter: RoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initFab()
        if (HomeDashboard.USER?.role ?: userType.USER == userType.USER) {
            detail_fab.visibility = View.GONE
        }
    }

    private fun initFab() {
        detail_fab.setOnClickListener {
            RoomDetailActivity.listener = this
            RoomDetailActivity.room = Room()
            val destination = Intent(context, RoomDetailActivity()::class.java)
            startActivity(destination)
        }
    }

    private fun initRecyclerView() {
        progressbarVisibility(true)
        MainRecyclerView.layoutManager = LinearLayoutManager(this.context)
        adapter = RoomAdapter(this)
        adapter.removeAllRoom()
        onRoomAdded()
        MainRecyclerView.adapter = adapter
    }

    fun progressbarVisibility(isVisible: Boolean) {
        when (isVisible) {
            true -> progressBar.visibility = View.VISIBLE
            false -> progressBar.visibility = View.GONE
        }
    }

    private fun onRoomAdded() {
        HomeDashboard.Auth_KEY?.let { it1 ->
            RoomNetWorkManager.getAllRoom(it1).enqueue(object : Callback<List<Room>> {
                override fun onResponse(
                    call: Call<List<Room>>,
                    response: Response<List<Room>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.forEach(adapter::addRoom)
                        progressbarVisibility(false)
                    }
                }

                override fun onFailure(call: Call<List<Room>>, t: Throwable) {
                    Toast.makeText(context, (getString(R.string.error)), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onRefresh() {
        loadRecyclerViewData()
        progressbarVisibility(false)
    }

    private fun loadRecyclerViewData() {
        swipe_container.isRefreshing = true
        roomAdded()
        swipe_container.isRefreshing = false
    }

    override fun onRoomSelected(room: Room) {
        RoomDetailActivity.listener = this
        RoomDetailActivity.room = room
        val destination = Intent(context, RoomDetailActivity()::class.java)
        startActivity(destination)
    }

    override fun onLongRoomListener(room: Room) {
        if (HomeDashboard.USER?.role ?: userType.USER != userType.USER) {
            val builder = context?.let { AlertDialog.Builder(it) }
            builder?.setTitle(getString(R.string.deleteRoom))
            builder?.setCancelable(true)
            builder?.apply {
                setNegativeButton(
                    getString(R.string.no)
                ) { _, _ ->
                }
                setPositiveButton(
                    getString(R.string.yes)
                ) { _, _ ->
                    room.id?.let {
                        HomeDashboard.Auth_KEY?.let { it1 ->
                            RoomNetWorkManager.deleteRoom(
                                it1,
                                it
                            ).enqueue(object : Callback<Void> {
                                override fun onResponse(
                                    call: Call<Void>,
                                    response: Response<Void>
                                ) {
                                    if (response.code() == 404) {
                                        adapter.removeRoom(adapter.getLectureId(room))
                                        return
                                    }
                                    onRefresh()
                                    Toast.makeText(
                                        context, getString(R.string.delete_unsuccess),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(
                                        context, getString(R.string.delete_unsuccess),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })
                        }
                    }
                }
            }
            builder?.show()
        }
    }

    override fun roomAdded() {
        initRecyclerView()
        progressbarVisibility(false)
    }
}
