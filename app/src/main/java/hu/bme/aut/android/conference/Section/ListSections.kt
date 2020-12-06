/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 5
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Section

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
import hu.bme.aut.android.conference.Adapter.SectionAdapter
import hu.bme.aut.android.conference.HomeDashboard
import hu.bme.aut.android.conference.Network.SectionNetworkManager
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.model.Section
import kotlinx.android.synthetic.main.fragment_list_sections.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListSections :
    Fragment(),
    SectionAdapter.OnSectionSelectedListener,
    SectionDetail.SectionAddedListener,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: SectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_sections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_container.setOnRefreshListener(this)
        initRecyclerView()
        initFab()
    }

    private fun initFab() {
        detail_fab.setOnClickListener {
            SectionDetail.listener = this
            val destination = Intent(context, SectionDetail()::class.java)
            startActivity(destination)
        }
    }

    private fun initRecyclerView() {
        progressbarVisibility(true)
        MainRecyclerView.layoutManager = LinearLayoutManager(this.context)
        adapter = SectionAdapter(this)
        adapter.removeAllSection()
        onSectionAdded()
        MainRecyclerView.adapter = adapter
    }

    private fun onSectionAdded() {
        HomeDashboard.Auth_KEY?.let {
            SectionNetworkManager.getSections(it).enqueue(
                object : Callback<List<Section>> {
                    override fun onResponse(
                        call: Call<List<Section>>,
                        response: Response<List<Section>>
                    ) {
                        response.body()?.forEach(adapter::addSection)
                        progressbarVisibility(false)
                    }

                    override fun onFailure(call: Call<List<Section>>, t: Throwable) {
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

    override fun onSectionSelected(section: Section) {
        Toast.makeText(this.context, section.id.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun OnLongSectionListener(section: Section) {
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
                HomeDashboard.Auth_KEY?.let {
                    section.id?.let { it1 ->
                        SectionNetworkManager.deleteSection(it, it1)
                            .enqueue(object : Callback<Void> {
                                override fun onResponse(
                                    call: Call<Void>,
                                    response: Response<Void>
                                ) {
                                    if (response.code() == 404) {
                                        adapter.removeSection(adapter.getSectionId(section))
                                        return
                                    }
                                    onRefresh()
                                    Toast.makeText(context, getString(R.string.delete_unsuccess), Toast.LENGTH_LONG).show()
                                }

                                override fun onFailure(
                                    call: Call<Void>,
                                    t: Throwable
                                ) {
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

    override fun sectionAdded() {
        initRecyclerView()
        progressbarVisibility(false)
    }

    override fun onRefresh() {
        loadRecyclerViewData()
        progressbarVisibility(false)
    }

    private fun loadRecyclerViewData() {
        swipe_container.isRefreshing = true
        sectionAdded()
        swipe_container.isRefreshing = false
    }
}
