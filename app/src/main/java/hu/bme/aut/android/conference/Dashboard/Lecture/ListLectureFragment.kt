/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 12
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Dashboard.Lecture

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
import hu.bme.aut.android.conference.Adapter.LectureAdapter
import hu.bme.aut.android.conference.Dashboard.HomeDashboard
import hu.bme.aut.android.conference.Network.LectureNetWorkManager
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.enum.userType
import hu.bme.aut.android.conference.model.Lecture
import kotlinx.android.synthetic.main.fragment_list_sections.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListLectureFragment :
    Fragment(),
    SwipeRefreshLayout.OnRefreshListener,
    LectureAdapter.OnLectureSelectedListener,
    LectureDetailActivity.LecturenAddedListener {

    private lateinit var adapter: LectureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lecture_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initFab()
        if (HomeDashboard.USER?.role ?: userType.USER == userType.USER) {
            detail_fab.visibility = View.GONE
        }
    }

    private fun initRecyclerView() {
        progressbarVisibility(true)
        MainRecyclerView.layoutManager = LinearLayoutManager(this.context)
        adapter = LectureAdapter(this)
        adapter.removeAllLecture()
        onLectureAdded()
        MainRecyclerView.adapter = adapter
    }

    private fun initFab() {
        detail_fab.setOnClickListener {
            LectureDetailActivity.listener = this
            LectureDetailActivity.lecture = Lecture()
            val destination = Intent(context, LectureDetailActivity()::class.java)
            startActivity(destination)
        }
    }

    private fun onLectureAdded() {
        HomeDashboard.Auth_KEY?.let { it1 ->
            LectureNetWorkManager.getAllLecture(it1).enqueue(object : Callback<List<Lecture>> {
                override fun onResponse(
                    call: Call<List<Lecture>>,
                    response: Response<List<Lecture>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.forEach(adapter::addLecture)
                        progressbarVisibility(false)
                    }
                }

                override fun onFailure(call: Call<List<Lecture>>, t: Throwable) {
                    Toast.makeText(context, (getString(R.string.error)), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun progressbarVisibility(isVisible: Boolean) {
        when (isVisible) {
            true -> progressBar.visibility = View.VISIBLE
            false -> progressBar.visibility = View.GONE
        }
    }

    override fun onRefresh() {
        progressbarVisibility(true)
        loadRecyclerViewData()
        progressbarVisibility(false)
    }

    private fun loadRecyclerViewData() {
        swipe_container.isRefreshing = true
        lectureAdded()
        swipe_container.isRefreshing = false
    }

    override fun onLectureSelected(lecture: Lecture) {
        LectureDetailActivity.listener = this
        LectureDetailActivity.lecture = lecture
        val destination = Intent(context, LectureDetailActivity()::class.java)
        startActivity(destination)
    }

    override fun onLongLectureListener(lecture: Lecture) {
        if (HomeDashboard.USER?.role ?: userType.USER != userType.USER) {
            val builder = context?.let { AlertDialog.Builder(it) }
            builder?.setTitle(getString(R.string.deleteSection))
            builder?.setCancelable(true)
            builder?.apply {
                setNegativeButton(
                    getString(R.string.no)
                ) { _, _ ->
                }
                setPositiveButton(
                    getString(R.string.yes)
                ) { _, _ ->
                    lecture.id?.let {
                        HomeDashboard.Auth_KEY?.let { it1 ->
                            LectureNetWorkManager.deleteLecture(
                                it1,
                                it
                            ).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>,
                                                        response: Response<Void>) {
                                    if (response.code() == 404) {
                                        adapter.removeLecture(adapter.getLectureId(lecture))
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

    override fun lectureAdded() {
        initRecyclerView()
        progressbarVisibility(false)
    }
}
